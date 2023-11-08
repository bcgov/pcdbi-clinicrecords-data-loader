package ca.bc.gov.hlth.pcbdi.batch.processor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;

import ca.bc.gov.hlth.pcbdi.batch.model.clinic.ClinicRecord;

public class ClinicRecordProcessor implements ItemProcessor<ClinicRecord, ClinicRecord> {
    private static final String CURRENT_STATE = "Current State";

    @Override
    public ClinicRecord process(ClinicRecord item) throws Exception {
        // Select only records with Record Type = "Current Status"
        return StringUtils.equals(item.getRecordType(), CURRENT_STATE) ? item : null;
    }

}
