package ca.bc.gov.hlth.pcbdi.batch.writer;

import static ca.bc.gov.hlth.pcbdi.util.Constants.EMPLOYMENT_STATUS_CURRENT;
import static ca.bc.gov.hlth.pcbdi.util.Constants.REPORTING_LEVEL_CLINIC_INITIATIVE;
import static ca.bc.gov.hlth.pcbdi.util.Constants.REPORTING_LEVEL_PCN;
import static ca.bc.gov.hlth.pcbdi.util.Constants.REPORTING_LEVEL_PCN_COMMUNITY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ca.bc.gov.hlth.pcbdi.batch.model.clinic.ClinicRecord;
import ca.bc.gov.hlth.pcbdi.batch.model.clinic.ClinicRecordDetail;
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
    	
    	List<? extends ClinicRecord> records = chunk.getItems();
    	
    	List<ClinicRecord> aggregatedRecords = new ArrayList<>();
    	
    	logger.info("Found {} records", records.size());
    			
		
    	// Process PCN Community Level
		Map<String, List<ClinicRecord>> pcnCommunityRecords = records.stream().filter(
				record -> StringUtils.equals(record.getSelectTheReportingLevel(), REPORTING_LEVEL_PCN_COMMUNITY))
				.collect(Collectors.groupingBy(ClinicRecord::getPcnCommunity));    	
    	
    	pcnCommunityRecords.forEach((key, value) -> {
    		
    		// Grab the PCN Community details from the first record
    		ClinicRecord clinic = aggregateClinicRecord(value);
    		
    		aggregatedRecords.add(clinic);
    		
    	});
    	logger.info("Added {} PCN Communities", pcnCommunityRecords.size());
    	
    	// Process PCN Level
		Map<String, List<ClinicRecord>> pcnRecords = records.stream().filter(
				record -> StringUtils.equals(record.getSelectTheReportingLevel(), REPORTING_LEVEL_PCN))
				.collect(Collectors.groupingBy(ClinicRecord::getPcnName));    	
    	
		pcnRecords.forEach((key, value) -> {
    		
    		// Grab the PCN details from the first record
			ClinicRecord clinic = aggregateClinicRecord(value);
    		
    		aggregatedRecords.add(clinic);
    		
    	});
		logger.info("Added {} PCNs", pcnRecords.size());
  
    	// Process Clinic/Initiative Level
		Map<String, List<ClinicRecord>> clinicInitiativeRecords = records.stream().filter(
				record -> StringUtils.equals(record.getSelectTheReportingLevel(), REPORTING_LEVEL_CLINIC_INITIATIVE))
				.collect(Collectors.groupingBy(ClinicRecord::getClinicName));    	
    	
		clinicInitiativeRecords.forEach((key, value) -> {
    		
    		// Grab the Clinic/Initiative details from the first record
			ClinicRecord clinic = aggregateClinicRecord(value);
    		
    		aggregatedRecords.add(clinic);
    		
    	});
		logger.info("Added {} Clinic/Initiatives", clinicInitiativeRecords.size());
    	
    	// Process each aggregated record
        aggregatedRecords.forEach(clinicRecord -> {
            try {
                submitClinicRecord(clinicRecord);    
            } catch (Exception e) {
                logger.error("Retrying submission due to error", e);

                // Wait and try again in case of network issues
                submitClinicRecord(clinicRecord);
            }
            
        });
        logger.info("Processed {} records ", aggregatedRecords.size());
    }
    
    private ClinicRecord aggregateClinicRecord(List<ClinicRecord> clinicRecords) {

		// Grab the PCN Community details from the first record
		Iterator<ClinicRecord> it = clinicRecords.iterator();
    	ClinicRecord parent = it.next();
		
    	// 1. Add each detail/practitioner
		while (it.hasNext()) {
			// Add each child record
			ClinicRecord clinic = it.next();
			parent.getClinicRecordDetails().addAll(clinic.getClinicRecordDetails());
		}
		
		
		// 2. Remove records which don't meet the following condition
		// if RECORD_TYPE = ""Current Status"" and FTE=0 and if the PCN_HR_CHANGE_RECORD_ID is not in REFERENCED_HR_RECORD , then employmentStatus = ""Current""
		List<ClinicRecordDetail> details = parent.getClinicRecordDetails();
		Iterator<ClinicRecordDetail> it2 = details.iterator();
		
		while (it2.hasNext()) {
			ClinicRecordDetail detail = it2.next();
			if (StringUtils.equals(detail.getEmploymentStatus(), EMPLOYMENT_STATUS_CURRENT) && detail.getFteEquivalent().compareTo(BigDecimal.ZERO) == 0 && isClinicIdReferenced(detail, details)) {
				it2.remove();
			}
		}
		
		// Remove records that don't have a calculated Employment Status
		Iterator<ClinicRecordDetail> it3 = details.iterator();
		while (it3.hasNext()) {
			ClinicRecordDetail detail = it3.next();
			if (StringUtils.isBlank(detail.getEmploymentStatus())) {
				it3.remove();
			}
			
		}
		
		return parent;
    }
    
	private Boolean isClinicIdReferenced(ClinicRecordDetail detail, List<ClinicRecordDetail> allDetails) {
    	return allDetails.stream().anyMatch(d -> {
    		return StringUtils.equals(d.getReferencedHrRecord(), detail.getLegacyWebformId()) && !StringUtils.equals(d.getLegacyWebformId(), detail.getLegacyWebformId());
    	});
    }
    
    private void submitClinicRecord(ClinicRecord clinicRecord) {
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
