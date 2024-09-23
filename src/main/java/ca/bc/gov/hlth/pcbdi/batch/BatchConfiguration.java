package ca.bc.gov.hlth.pcbdi.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

import ca.bc.gov.hlth.pcbdi.batch.listener.Step1ExecutionListener;
import ca.bc.gov.hlth.pcbdi.batch.mapper.ClinicRecordFieldSetMapper;
import ca.bc.gov.hlth.pcbdi.batch.model.clinic.ClinicRecord;
import ca.bc.gov.hlth.pcbdi.batch.processor.ClinicRecordProcessor;
import ca.bc.gov.hlth.pcbdi.batch.tasklet.DeleteClinicRecordsTasklet;
import ca.bc.gov.hlth.pcbdi.batch.writer.ChefsItemWriter;
import ca.bc.gov.hlth.pcbdi.service.ChefsService;

@Configuration
public class BatchConfiguration {
    
    @Value("${chunk.size:500000}")
    private Integer chunkSize;

    @Value("file:${file.input}")
    private Resource inputCsv;

    @Autowired
    private ChefsService chefsService;

    @Bean
    public ItemReader<ClinicRecord> reader() throws UnexpectedInputException, ParseException {
        FlatFileItemReader<ClinicRecord> reader = new FlatFileItemReader<ClinicRecord>();
        reader.setLinesToSkip(1);
        reader.setRecordSeparatorPolicy(new DefaultRecordSeparatorPolicy());
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        String[] tokens = { "PCN_COMMUNITY_ID", "PCN_COMMUNITY_NAME", "APPROVAL_DATE_PCN_COMMUNITY", "PCN_ID", "PCN_NAME",
                "ANNOUNCEMENT_DATE_PCN", "PCN_CLINIC_ID", "PCN_CLINIC_NAME", "PCN_INITIATIVE_TYPE", "PCN_HR_CHANGE_RECORD_ID", "EFFECTIVE_DATE", "RECORD_TYPE",
                "REFERENCED_HR_RECORD", "PRACTITIONER_TYPE", "PRACTITIONER_BILLING_NUMBER", "PRACTITIONER_NAME", "DURATION", "FTE",
                "PAYMENT_MODALITY", "NOTES", "PCN_CLINIC_TYPE", "ADDRESS_STREET", "ADDRESS_CITY", "ADDRESS_PROVINCE", "ADDRESS_COUNTRY",
                "ADDRESS_POSTAL_CODE", "PCN_REPORTING_PERIOD_NAME", "FISCAL_YEAR", "FISCAL_QUARTER", "CREATED_DATE", "CHANGED_DATE" };

        tokenizer.setNames(tokens);
        reader.setResource(inputCsv);

        DefaultLineMapper<ClinicRecord> lineMapper = new DefaultLineMapper<ClinicRecord>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new ClinicRecordFieldSetMapper(chefsService));
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public ItemProcessor<ClinicRecord, ClinicRecord> processor() {
        return new ClinicRecordProcessor();
    }

    @Bean
    public ChefsItemWriter writer() {
        return new ChefsItemWriter(chefsService);
    }

    @Bean
    public Step1ExecutionListener stepListener() {
        return new Step1ExecutionListener();
    }

    @Bean
    protected Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step1", jobRepository).<ClinicRecord, ClinicRecord>chunk(chunkSize, transactionManager).reader(reader())
                .processor(processor()).writer(writer()).listener(stepListener()).build();
    }

    @Bean
    protected Step step2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("step2", jobRepository).tasklet(deleteClinicRecordsTasklet(), transactionManager).build();
    }

    @Bean
    protected Tasklet deleteClinicRecordsTasklet() {
        return new DeleteClinicRecordsTasklet();
    }

    @Bean(name = "clinicRecordsImportJob")
    public Job job(JobRepository jobRepository, JobExecutionListener listener, @Qualifier("step1") Step step1, @Qualifier("step2") Step step2) {
        return new JobBuilder("clinicRecordsImportJob", jobRepository).preventRestart().listener(listener).start(step1).next(step2).build();
    }

}
