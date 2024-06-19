package ca.bc.gov.hlth.pcbdi.batch.processor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import ca.bc.gov.hlth.pcbdi.batch.model.clinic.ClinicRecord;

public class ClinicRecordProcessor implements ItemProcessor<ClinicRecord, ClinicRecord> {

	@Override
	public ClinicRecord process(ClinicRecord item) throws Exception {
		// Exclude records that don't have a calculated Employment Status
		return StringUtils.isNotBlank(item.getClinicRecordDetails().get(0).getEmploymentStatus()) ? item : null;
	}

}
