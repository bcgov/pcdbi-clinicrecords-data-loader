package ca.bc.gov.hlth.pcbdi.batch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class DeleteClinicRecordsTasklet implements Tasklet {
    public static final Logger logger = LoggerFactory.getLogger(DeleteClinicRecordsTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        logger.info("Deleting records");
        // Query submissions
        // Soft delete submission
        return RepeatStatus.FINISHED;
    }

}
