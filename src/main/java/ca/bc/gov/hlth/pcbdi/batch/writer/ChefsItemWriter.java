package ca.bc.gov.hlth.pcbdi.batch.writer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import ca.bc.gov.hlth.pcbdi.batch.model.clinic.ClinicRecord;
import ca.bc.gov.hlth.pcbdi.service.ChefsService;

public class ChefsItemWriter implements ItemWriter<ClinicRecord> {
    
    private ChefsService chefsService;
    
    private static final Logger logger = LoggerFactory.getLogger(ChefsItemWriter.class);

    public ChefsItemWriter(ChefsService chefsService) {
        this.chefsService = chefsService;
    }

    @Override
    public void write(Chunk<? extends ClinicRecord> chunk) throws Exception {
        int count = 1;
        for (ClinicRecord clinicRecord: chunk.getItems()) {
        //chunk.getItems().forEach(clinicRecord -> {
            logger.debug("Processing clinic {}", clinicRecord);
            logger.info("Procesing count {}", count);
            chefsService.createClinicRecord(clinicRecord);
            count++;
        //});
    }
    }
        
    
}
