package ca.bc.gov.hlth.pcbdi.batch.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import ca.bc.gov.hlth.pcbdi.batch.model.clinic.ClinicRecord;
import ca.bc.gov.hlth.pcbdi.batch.model.clinic.ClinicRecordDetail;
import ca.bc.gov.hlth.pcbdi.batch.model.financial.ReportingDates;
import ca.bc.gov.hlth.pcbdi.batch.model.ha.HaSubmission;
import ca.bc.gov.hlth.pcbdi.service.ChefsService;

public class ClinicRecordFieldSetMapper implements FieldSetMapper<ClinicRecord> {

    private List<HaSubmission> haSubmissions;

    private List<ReportingDates> reportingDates;
    
    private static final String INITIATIVE_TYPE_FNPCI = "FNPCI";

    private static final String INITIATIVE_TYPE_CHC = "CHC";
    private static final String INITIATIVE_TYPE_FNPCC = "FNPCC";
    private static final String INITIATIVE_TYPE_NPPCC = "NPPCC";
    private static final String INITIATIVE_TYPE_PCN = "PCN";
    private static final String INITIATIVE_TYPE_UPCC = "UPCC";
    
    private static final String EMPLOYMENT_STATUS_CURRENT = "Current";
    private static final String EMPLOYMENT_STATUS_DEPARTED = "Departed";
    
    private static final String RECORD_TYPE_CURRENT_STATE = "Current State";
    private static final String RECORD_TYPE_DEPARTURE = "Departure";
    
    private static final String REPORTING_LEVEL_CLINIC_INITIATIVE = "Clinic/Initiative";
    private static final String REPORTING_LEVEL_PCN= "PCN";
    private static final String REPORTING_LEVEL_PCN_COMMUNITY = "PCN Community";
    
    private static final String PRACTITIONER_TYPE_FAMILY_PHYSICIAN = "Family Physician";
    private static final String PRACTITIONER_TYPE_NURSE_PRACTITIONER = "Nurse Practitioner";
    private static final String PRACTITIONER_TYPE_REGISTERED_NURSE = "Registered Nurse";
    
    private static final String KEYWORD_GROUP = "Group";
    
    private static final Integer DATE_RANGE_MONTHS = 18;
    
    private static final DateTimeFormatter inputDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 

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
    	String practitionerName = fieldSet.readString("PRACTITIONER_NAME");
    	String practitionerType = fieldSet.readString("PRACTITIONER_TYPE");

        ClinicRecord record = new ClinicRecord();
        record.setPcnName(fieldSet.readString("PCN_NAME"));
        record.setClinicID(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        
        String clinicName = fieldSet.readString("PCN_CLINIC_NAME");
        record.setClinicName(clinicName);
        
        String clinicType = fieldSet.readString("PCN_CLINIC_TYPE");
        record.setClinicType(clinicType);
        
        String pcnCommunity = fieldSet.readString("PCN_COMMUNITY_NAME");
        record.setPcnCommunity(pcnCommunity);
        
        record.setInitiativeType(deriveInitiativeType(fieldSet.readString("PCN_INITIATIVE_TYPE")));
        
        record.setHealthAuthority(deriveHealthAuthority(pcnCommunity));
        record.setSelectTheReportingLevel(deriveReportingLeval(record));
        
        ClinicRecordDetail detail = new ClinicRecordDetail();
        detail.setNotes(fieldSet.readString("NOTES"));
        detail.setPeriod(transformPeriod(fieldSet.readString("PCN_REPORTING_PERIOD_NAME")));
        detail.setDuration(fieldSet.readString("DURATION"));

        detail.setDateHired(transformDate(fieldSet.readString("EFFECTIVE_DATE")));

        detail.setGroupRole(deriveGroupRole(practitionerName, practitionerType));
        
        String fiscalYear = transformFiscalYear(fieldSet.readString("FISCAL_YEAR"));
        detail.setFiscalYear(fiscalYear);

        String fte = fieldSet.readString("FTE");
        if (StringUtils.isNotBlank(fte)) {
        	detail.setFteEquivalent(new BigDecimal(fte));
        }

        detail.setLegacyWebformId(fieldSet.readString("PCN_HR_CHANGE_RECORD_ID"));
        detail.setPaymentModality(fieldSet.readString("PAYMENT_MODALITY"));
        detail.setEmploymentStatus(deriveEmploymentStatus(detail, fieldSet));
        detail.setPractitionerRole(practitionerType);
        detail.setPractitionerType(derivePractitionerType(practitionerName));
        
        detail.setRecordCreatedDate(transformDate(fieldSet.readString("CREATED_DATE")));
        populatePractitionerName(detail, practitionerName, practitionerType);
        
        detail.setPractitionerBillingNumber(fieldSet.readString("PRACTITIONER_BILLING_NUMBER"));
        detail.setAdditionalGroupDetails(deriveAdditionalGroupDetails(record, practitionerName));
        detail.setDateEmploymentStatusChanged(deriveDateEmploymentStatusChanged(detail, fieldSet.readString("CHANGED_DATE")));        
        detail.setPractitionerBillingNumberNotAvailable(deriveBillingNumberNA(detail));
        
        System.out.println(detail);

        record.getClinicRecordDetails().add(detail);

        return record;
    }
    
    private String deriveEmploymentStatus(ClinicRecordDetail detail, FieldSet fieldSet) {
    	String recordType = fieldSet.readString("RECORD_TYPE");
    	String referencedHrRecord = fieldSet.readString("REFERENCED_HR_RECORD");

    	
    	if (StringUtils.equals(recordType, RECORD_TYPE_CURRENT_STATE)) {
        	// 1. if RECORD_TYPE = ""Current State"" and FTE <> 0, then employmentStatus = ""Current"" 
    		if (detail.getFteEquivalent() != null && detail.getFteEquivalent().compareTo(BigDecimal.ZERO) > 0) {
    			return EMPLOYMENT_STATUS_CURRENT;
    		}    			
        	// 2. if RECORD_TYPE = ""Current Status"" and FTE=0 and if the PCN_HR_CHANGE_RECORD_ID is not in REFERENCED_HR_RECORD , then employmentStatus = ""Current""
    		if ((detail.getFteEquivalent() == null || detail.getFteEquivalent().compareTo(BigDecimal.ZERO) == 0 ) && !StringUtils.equals(detail.getLegacyWebformId(), referencedHrRecord)) {
    			return EMPLOYMENT_STATUS_CURRENT;
    		}
    		
    	}
    	// 3. if RECORD_TYPE = "Departure" and CHANGED_DATE >=Dec 01, 2022 then employmentStatus = "Departed" 
    	else if (StringUtils.equals(recordType, RECORD_TYPE_DEPARTURE)) {
        	LocalDate cutoffDate = LocalDate.now().minusMonths(DATE_RANGE_MONTHS);    	
        	LocalDate changedDate = LocalDate.parse(fieldSet.readString("CHANGED_DATE"), inputDateFormat);
    		if (!changedDate.isBefore(cutoffDate)) {
    			return EMPLOYMENT_STATUS_DEPARTED;	 
    		 }
    		
    	}
    	return null;
    }
    
    private void populatePractitionerName(ClinicRecordDetail detail, String practitionerName, String practitionerType) {
    	String firstName = "";
    	String lastName = "";    	
    	if (StringUtils.containsIgnoreCase(practitionerName, KEYWORD_GROUP)) {
        	// If PRACTITIONER_NAME LIKE 'GROUP' then practitionerFirstName = "PRACTITIONER_TYPE"
    		firstName = practitionerType;

        	// If PRACTITIONER_NAME LIKE 'GROUP' then practitionerLastName = "Group""
    		lastName = KEYWORD_GROUP;
    	} else {
        	// Names are typically John Doe but some are formatted as Doe, John.

        	if (StringUtils.contains(practitionerName, ",")) {
        		lastName = StringUtils.substringBefore(practitionerName, ",");
        		firstName = StringUtils.substringAfter(practitionerName, ",");
        	} else {
        		firstName = StringUtils.substringBeforeLast(practitionerName, " ");
        		lastName = StringUtils.substringAfterLast(practitionerName, " ");
        	}    		
    	}
    	

    	detail.setPractitionerFirstName(firstName);
    	detail.setPractitionerLastName(lastName);    	

    	String fullName = lastName + ", " + firstName;
    	detail.setPractitionerName(fullName);
    }

    /**
     * Derived based on PCN_INITIATIVE_TYPE
     * @param pcnInitiativeType
     * @return
     */
    private String deriveInitiativeType(String pcnInitiativeType) {
        // 1. if PCN_INITIATIVE_TYPE = "CHC" or "UPCC" or "PCN" or "NPPCC" then initiativeType = PCN_INITIATIVE_TYPE
        // 2. if PCN_INITIATIVE_TYPE = “FNPCI"" then initiativeType = ""FNPCC""
        // 3. if PCN_INITIATIVE_TYPE is Blank then initiativeType = ""PCN"""
        switch (pcnInitiativeType) {
        case INITIATIVE_TYPE_CHC:
        case INITIATIVE_TYPE_UPCC:
        case INITIATIVE_TYPE_PCN:
        case INITIATIVE_TYPE_NPPCC:
        	return pcnInitiativeType;        	
        case INITIATIVE_TYPE_FNPCI:
        	return INITIATIVE_TYPE_FNPCC;
        default:
        	return INITIATIVE_TYPE_PCN;        		
        }

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
    
    private String deriveReportingLeval(ClinicRecord clinicRecord) {
    	// 1. if PCN_Clinic_Name is not Blank then 'selectTheReportingLevel' = "Clinic/Initiative"
    	if (StringUtils.isNotBlank(clinicRecord.getClinicName())) {
    		return REPORTING_LEVEL_CLINIC_INITIATIVE;
    	} else {
        	// 2. if (PCN_Clinic_Name is Blank and PCN_NAME is not Blank) then 'selectTheReportingLevel' = "PCN"
    		if (StringUtils.isNotBlank(clinicRecord.getPcnName())) {
    			return REPORTING_LEVEL_PCN;
    		}
        	// 3. if (PCN_Clinic_Name is Blank and PCN_NAME is Blank) then 'selectTheReportingLevel' = "PCN Community""
    		else {
    			return REPORTING_LEVEL_PCN_COMMUNITY;
    		}
    		
    	}

    }
    
    private Boolean deriveBillingNumberNA(ClinicRecordDetail detail) {
    	// 1.if PRACTITIONER_TYPE = 'Family Physician' and  PRACTITIONER_BILLING_NUMBER is blank then practitionerBillingNumberNotAvailable = TRUE
    	if (StringUtils.equals(detail.getPractitionerRole(), PRACTITIONER_TYPE_FAMILY_PHYSICIAN) && StringUtils.isBlank(detail.getPractitionerBillingNumber())) {
    		return Boolean.TRUE;
    	}
    	// 2.if PRACTITIONER_TYPE = 'Nurse Practitioner' and  PRACTITIONER_BILLING_NUMBER is blank then practitionerBillingNumberNotAvailable = TRUE
    	if (StringUtils.equals(detail.getPractitionerRole(), PRACTITIONER_TYPE_NURSE_PRACTITIONER) && StringUtils.isBlank(detail.getPractitionerBillingNumber())) {
    		return Boolean.TRUE;
    	}
    	// 3. if PRACTITIONER_TYPE = 'Registered Nurse' and  PRACTITIONER_BILLING_NUMBER is blank then practitionerBillingNumberNotAvailable = TRUE"
    	if (StringUtils.equals(detail.getPractitionerRole(), PRACTITIONER_TYPE_REGISTERED_NURSE) && StringUtils.isBlank(detail.getPractitionerBillingNumber())) {
    		return Boolean.TRUE;
    	}
    		
    	return Boolean.FALSE;
    }
    
    private String deriveGroupRole(String practitionerName, String practitionerType) {
    	// If PRACTITIONER_NAME LIKE 'GROUP' then groupRole = 'PRACTITIONER_TYPE'
    	return isGroup(practitionerName) ? practitionerType : null;
    }
    
    private String deriveAdditionalGroupDetails(ClinicRecord clinicRecord, String practitionerName) {
    	// If PRACTITIONER_NAME LIKE 'GROUP' then additionalGroupDetails = 'PRACTITIONER_NAME'
    	return isGroup(practitionerName) ? practitionerName : null;
    }
    
    private String deriveDateEmploymentStatusChanged(ClinicRecordDetail detail, String changedDate) {
    	// 1. If the employmentStatus = "Departed", then dateEmploymentStatusChanged = CHANGED_DATE"
    	return StringUtils.equals(detail.getEmploymentStatus(), EMPLOYMENT_STATUS_DEPARTED) ? changedDate : ""; 
    }
    
    private String derivePractitionerType(String practitionerName) {
    	// If PRACTITIONER_NAME LIKE 'GROUP' then practitionerType = "group", else "individual"
    	return isGroup(practitionerName) ? "group" : "individual";
    }
    
    private Boolean isGroup(String practitionerName) {
    	return StringUtils.containsIgnoreCase(practitionerName, "GROUP");
    }

    private String transformFiscalYear(String fiscalYear) {
        // Transform from "yyyy/yyyy" to "yyyy/yy". E.g. 2023/2024 to 2023/24
        return StringUtils.substring(fiscalYear, 0, 5) + StringUtils.substring(fiscalYear, 7, 9);
    }

    private String transformPeriod(String period) {
        // Extract period from PCN_REPORTING_PERIOD_NAME as a number. E.g. FY23 P7 to 7
        return StringUtils.substring(period, 6, 8);
    }
    
    private String transformDate(String date) {
    	LocalDate localDate = LocalDate.parse(date, inputDateFormat);
    	
    	return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

}
