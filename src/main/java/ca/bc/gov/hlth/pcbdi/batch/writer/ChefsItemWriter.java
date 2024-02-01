package ca.bc.gov.hlth.pcbdi.batch.writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ca.bc.gov.hlth.pcbdi.batch.model.clinic.ClinicRecord;
import ca.bc.gov.hlth.pcbdi.service.ChefsService;

@Component
public class ChefsItemWriter implements ItemWriter<ClinicRecord> {
    
    @Value("${chefs.wait:0}")
    private Integer chefsWait;

    private ChefsService chefsService;

    private static final Logger logger = LoggerFactory.getLogger(ChefsItemWriter.class);

    public ChefsItemWriter(ChefsService chefsService) {
        this.chefsService = chefsService;
    }

    @Override
    public void write(Chunk<? extends ClinicRecord> chunk) throws Exception {
        chunk.getItems().forEach(clinicRecord -> {
            try {
                processClinicRecord(clinicRecord);    
            } catch (Exception e) {
                logger.error("Retrying submission due to error", e);

                // Wait and try again in case of network issues
                processClinicRecord(clinicRecord);
            }
            
        });
        logger.info("Processed {} records ", chunk.size());
    }
    
    private void processClinicRecord(ClinicRecord clinicRecord) {
        // Add a wait to reduce server load and prevent 429 errors from CHEFS
        // CHEFS will return a 429 if over 20 requests are sent in a 1 minute time frame
        if (chefsWait != null && chefsWait != 0) {
            try {
                Thread.sleep(chefsWait);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.debug("Processing clinic {}", clinicRecord);       
        chefsService.createClinicRecord(clinicRecord);    
    }

}
