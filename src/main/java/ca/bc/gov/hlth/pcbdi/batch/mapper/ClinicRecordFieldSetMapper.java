package ca.bc.gov.hlth.pcbdi.batch.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import ca.bc.gov.hlth.pcbdi.batch.model.clinic.ClinicRecord;
import ca.bc.gov.hlth.pcbdi.batch.model.financial.ReportingDates;
import ca.bc.gov.hlth.pcbdi.batch.model.ha.HaSubmission;
import ca.bc.gov.hlth.pcbdi.service.ChefsService;

public class ClinicRecordFieldSetMapper implements FieldSetMapper<ClinicRecord> {

    private List<HaSubmission> haSubmissions;

    private List<ReportingDates> reportingDates;
    
    private static final String CLINIC_NAME_LUMA = "Luma";
    
    private static final String CLINIC_TYPE_CHC = "CHC";
    private static final String CLINIC_TYPE_NPPCC = "NP Clinic";
    private static final String CLINIC_TYPE_UPCC = "Urgent and Primary Care Center (UPCC)";

    private static final String INITIATIVE_TYPE_CHC = "CHC";
    private static final String INITIATIVE_TYPE_FNPCC = "FNPCC";
    private static final String INITIATIVE_TYPE_NPPCC = "NPPCC";
    private static final String INITIATIVE_TYPE_PCN = "PCN";
    private static final String INITIATIVE_TYPE_UPCC = "UPCC";

    public ClinicRecordFieldSetMapper(ChefsService haService) {
        super();
        haSubmissions = haService.getHealthAuthorities().getBody();
        if (haSubmissions.isEmpty()) {
            throw new BatchConfigurationException("Health Authority Submissions not available");
        }
        
        reportingDates = haService.getReportingDates().getBody();
        if (reportingDates.isEmpty()) {
            throw new BatchConfigurationException("Fiscal Year Reporting Dates not available");
        }
    }

    @Override
    public ClinicRecord mapFieldSet(FieldSet fieldSet) throws BindException {

        ClinicRecord record = new ClinicRecord();
        
        String clinicName = fieldSet.readString("PCN_CLINIC_NAME");
        record.setClinicName(clinicName);
        
        record.setClinicRecordId(fieldSet.readString("PCN_HR_CHANGE_RECORD_ID"));
        
        String clinicType = fieldSet.readString("PCN_CLINIC_TYPE");
        record.setClinicType(clinicType);
        
        record.setDuration(fieldSet.readString("DURATION"));
        
        String effectiveDate = fieldSet.readString("EFFECTIVE_DATE");
        record.setEffectiveDate(effectiveDate);
        
        String fiscalYear = transformFiscalYear(fieldSet.readString("FISCAL_YEAR"));
        record.setFiscalYear(fiscalYear);
        
        record.setFteEquivalent(new BigDecimal(fieldSet.readString("FTE")));

        // Derive based on the PCN_COMMUNITY_NAME and Health Authority Hierarchy
        // submissions in CHEFS
        String pcnCommunity = fieldSet.readString("PCN_COMMUNITY_NAME");
        record.setHealthAuthority(deriveHealthAuthority(pcnCommunity));


        String initiativeType = fieldSet.readString("PCN_INITIATIVE_TYPE");
        if (StringUtils.isBlank(initiativeType)) {
            initiativeType = deriveInitiativeType(clinicType, clinicName);;
        }
        record.setInitiativeType(initiativeType);

        record.setNotes(fieldSet.readString("NOTES"));
        record.setPaymentModality(fieldSet.readString("PAYMENT_MODALITY"));
        record.setPcnCommunity(pcnCommunity);
        record.setPcnName(fieldSet.readString("PCN_NAME"));
        record.setPeriod(transformPeriod(fieldSet.readString("PCN_REPORTING_PERIOD_NAME")));
        record.setPractitionerBillingNumber(fieldSet.readString("PRACTITIONER_BILLING_NUMBER"));
        record.setPractitionerName(fieldSet.readString("PRACTITIONER_NAME"));
        record.setPractitionerRole(fieldSet.readString("PRACTITIONER_TYPE"));
        record.setRecordType(fieldSet.readString("RECORD_TYPE"));

        return record;
    }

    /**
     * Derived based on PCN_CLINIC_TYPE - Not available in HealthIdeas, needs to be derived.
     * @param pcnClinicType
     * @param clinicName
     * @return
     */
    private String deriveInitiativeType(String pcnClinicType, String clinicName) {
        String initiativeType = null;
        // if PCN_CLINIC_TYPE = "CHC" then initiativeType = "CHC"
        // if PCN_CLINIC_TYPE = "Urgent and Primary Care Center (UPCC)" then initiativeType = "UPCC",
        // if PCN_CLINIC_TYPE = “NP Clinic" then initiativeType = "NPPCC"
        // if clinicName ="Luma" then initiativeType = "FNPCC"
        // if PCN_CLINIC_TYPE Not In ("CHC", "Urgent and Primary Care Center (UPCC)", "NP Clinic") or clinicName like "Luma", then initiativeType = "PCN" 

        if (StringUtils.equals(pcnClinicType, CLINIC_TYPE_CHC)) {
            initiativeType = INITIATIVE_TYPE_CHC;
        } else if (StringUtils.equals(pcnClinicType, CLINIC_TYPE_UPCC)) {
            initiativeType = INITIATIVE_TYPE_UPCC;
        } else if (StringUtils.equals(pcnClinicType, CLINIC_TYPE_NPPCC)) {
            initiativeType = INITIATIVE_TYPE_NPPCC;
        } else if (StringUtils.equals(clinicName, CLINIC_NAME_LUMA)){
            initiativeType = INITIATIVE_TYPE_FNPCC;
        } else {
            initiativeType = INITIATIVE_TYPE_PCN;
        }
        return initiativeType;
    }

    private String deriveHealthAuthority(String input) {
        String healthAuthority = null;
        for (HaSubmission haSubmission : haSubmissions) {
            if (haSubmission.getCommunities().stream().anyMatch(community -> StringUtils.equals(community.getCommunityName(), input))) {
                healthAuthority = haSubmission.getHealthAuthority();
                break;
            }
        }

        return healthAuthority;
    }

    private String transformFiscalYear(String fiscalYear) {
        // Transform from "yyyy/yyyy" to "yyyy/yy". E.g. 2023/2024 to 2023/24
        return StringUtils.substring(fiscalYear, 0, 5) + StringUtils.substring(fiscalYear, 7, 9);
    }

    private String transformPeriod(String period) {
        // Extract period from PCN_REPORTING_PERIOD_NAME as a number. E.g. FY23 P7 to 7
        return StringUtils.substring(period, 6, 8);
    }

}
