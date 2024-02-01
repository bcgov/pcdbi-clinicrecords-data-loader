package ca.bc.gov.hlth.pcbdi.batch.model.clinic;

import java.math.BigDecimal;

import ca.bc.gov.hlth.pcbdi.batch.model.submission.Data;

public class ClinicRecord extends Data {
    private String clinicRecordId;
    private String healthAuthority;
    private String pcnCommunity;
    private String pcnName;
    private String initiativeType;
    private String clinicName;
    private String clinicType;

    /** Practitioner Details */
    private String practitionerName;
    private String practitionerBillingNumber;
    private String practitionerRole;

    /** Employment Details */
    private String duration;
    private BigDecimal fteEquivalent;
    private String paymentModality;
    private String effectiveDate;
    private String period;
    private String fiscalYear;

    private String notes;

    /** Metadata */
    private String recordType;

    public String getClinicRecordId() {
        return clinicRecordId;
    }

    public void setClinicRecordId(String clinicRecordId) {
        this.clinicRecordId = clinicRecordId;
    }

    public String getClinicType() {
        return clinicType;
    }

    public void setClinicType(String clinicType) {
        this.clinicType = clinicType;
    }

    public String getHealthAuthority() {
        return healthAuthority;
    }

    public void setHealthAuthority(String healthAuthority) {
        this.healthAuthority = healthAuthority;
    }

    public String getPcnCommunity() {
        return pcnCommunity;
    }

    public void setPcnCommunity(String pcnCommunity) {
        this.pcnCommunity = pcnCommunity;
    }

    public String getPcnName() {
        return pcnName;
    }

    public void setPcnName(String pcnName) {
        this.pcnName = pcnName;
    }

    public String getInitiativeType() {
        return initiativeType;
    }

    public void setInitiativeType(String initiativeType) {
        this.initiativeType = initiativeType;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getPractitionerName() {
        return practitionerName;
    }

    public void setPractitionerName(String practitionerName) {
        this.practitionerName = practitionerName;
    }

    public String getPractitionerBillingNumber() {
        return practitionerBillingNumber;
    }

    public void setPractitionerBillingNumber(String practitionerBillingNumber) {
        this.practitionerBillingNumber = practitionerBillingNumber;
    }

    public String getPractitionerRole() {
        return practitionerRole;
    }

    public void setPractitionerRole(String practitionerRole) {
        this.practitionerRole = practitionerRole;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public BigDecimal getFteEquivalent() {
        return fteEquivalent;
    }

    public void setFteEquivalent(BigDecimal fteEquivalent) {
        this.fteEquivalent = fteEquivalent;
    }

    public String getPaymentModality() {
        return paymentModality;
    }

    public void setPaymentModality(String paymentModality) {
        this.paymentModality = paymentModality;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    @Override
    public String toString() {
        return "ClinicRecord [clinicRecordId=" + clinicRecordId + ", healthAuthority=" + healthAuthority + ", pcnCommunity=" + pcnCommunity
                + ", pcnName=" + pcnName + ", initiativeType=" + initiativeType + ", clinicName=" + clinicName + ", clinicType="
                + clinicType + ", practitionerName=" + practitionerName + ", practitionerBillingNumber=" + practitionerBillingNumber
                + ", practitionerRole=" + practitionerRole + ", duration=" + duration + ", fteEquivalent=" + fteEquivalent
                + ", paymentModality=" + paymentModality + ", effectiveDate=" + effectiveDate + ", period=" + period + ", fiscalYear="
                + fiscalYear + ", notes=" + notes + ", recordType=" + recordType + "]";
    }

}
