package ca.bc.gov.hlth.pcbdi.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import ca.bc.gov.hlth.pcbdi.batch.model.clinic.ClinicRecord;

public class ClinicRecordProcessor implements ItemProcessor<ClinicRecord, ClinicRecord> {

	@Override
	public ClinicRecord process(ClinicRecord item) throws Exception {
		// Include all records for now since they may be required for comparison
		// Handle blank Employment Status later
		return item;
	}

}
