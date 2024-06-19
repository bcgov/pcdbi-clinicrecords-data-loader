package ca.bc.gov.hlth.pcbdi.batch.model.clinic;

import java.math.BigDecimal;

public class ClinicRecordDetail {

	private String notes;
	private String period;
	private String duration;
	private String dateHired;
	private String groupRole;
	private String speciality;
	private String fiscalYear;
	private BigDecimal fteEquivalent;
	private String otherSpeciality;
	private String legacyWebformId;
	private String paymentModality;
	private String employmentStatus;
	private String practitionerName;
	private String practitionerRole;
	private String practitionerType;
	private String recordCreatedDate;
	private String practitionerLastName;
	private String practitionerFirstName;
	private String practitionerBillingNumber;
	private String additionalGroupDetails;
	private String dateEmploymentStatusChanged;
	private Boolean practitionerBillingNumberNotAvailable;

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getDateHired() {
		return dateHired;
	}

	public void setDateHired(String dateHired) {
		this.dateHired = dateHired;
	}

	public String getGroupRole() {
		return groupRole;
	}

	public void setGroupRole(String groupRole) {
		this.groupRole = groupRole;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(String fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public BigDecimal getFteEquivalent() {
		return fteEquivalent;
	}

	public void setFteEquivalent(BigDecimal fteEquivalent) {
		this.fteEquivalent = fteEquivalent;
	}

	public String getOtherSpeciality() {
		return otherSpeciality;
	}

	public void setOtherSpeciality(String otherSpeciality) {
		this.otherSpeciality = otherSpeciality;
	}

	public String getLegacyWebformId() {
		return legacyWebformId;
	}

	public void setLegacyWebformId(String legacyWebformId) {
		this.legacyWebformId = legacyWebformId;
	}

	public String getPaymentModality() {
		return paymentModality;
	}

	public void setPaymentModality(String paymentModality) {
		this.paymentModality = paymentModality;
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public String getPractitionerName() {
		return practitionerName;
	}

	public void setPractitionerName(String practitionerName) {
		this.practitionerName = practitionerName;
	}

	public String getPractitionerRole() {
		return practitionerRole;
	}

	public void setPractitionerRole(String practitionerRole) {
		this.practitionerRole = practitionerRole;
	}

	public String getPractitionerType() {
		return practitionerType;
	}

	public void setPractitionerType(String practitionerType) {
		this.practitionerType = practitionerType;
	}

	public String getRecordCreatedDate() {
		return recordCreatedDate;
	}

	public void setRecordCreatedDate(String recordCreatedDate) {
		this.recordCreatedDate = recordCreatedDate;
	}

	public String getPractitionerLastName() {
		return practitionerLastName;
	}

	public void setPractitionerLastName(String practitionerLastName) {
		this.practitionerLastName = practitionerLastName;
	}

	public String getPractitionerFirstName() {
		return practitionerFirstName;
	}

	public void setPractitionerFirstName(String practitionerFirstName) {
		this.practitionerFirstName = practitionerFirstName;
	}

	public String getPractitionerBillingNumber() {
		return practitionerBillingNumber;
	}

	public void setPractitionerBillingNumber(String practitionerBillingNumber) {
		this.practitionerBillingNumber = practitionerBillingNumber;
	}

	public String getAdditionalGroupDetails() {
		return additionalGroupDetails;
	}

	public void setAdditionalGroupDetails(String additionalGroupDetails) {
		this.additionalGroupDetails = additionalGroupDetails;
	}

	public String getDateEmploymentStatusChanged() {
		return dateEmploymentStatusChanged;
	}

	public void setDateEmploymentStatusChanged(String dateEmploymentStatusChanged) {
		this.dateEmploymentStatusChanged = dateEmploymentStatusChanged;
	}

	public Boolean getPractitionerBillingNumberNotAvailable() {
		return practitionerBillingNumberNotAvailable;
	}

	public void setPractitionerBillingNumberNotAvailable(Boolean practitionerBillingNumberNotAvailable) {
		this.practitionerBillingNumberNotAvailable = practitionerBillingNumberNotAvailable;
	}

	@Override
	public String toString() {
		return "ClinicRecordDetail [notes=" + notes + ", period=" + period + ", duration=" + duration + ", dateHired="
				+ dateHired + ", groupRole=" + groupRole + ", speciality=" + speciality + ", fiscalYear=" + fiscalYear
				+ ", fteEquivalent=" + fteEquivalent + ", otherSpeciality=" + otherSpeciality + ", legacyWebformId="
				+ legacyWebformId + ", paymentModality=" + paymentModality + ", employmentStatus=" + employmentStatus
				+ ", practitionerName=" + practitionerName + ", practitionerRole=" + practitionerRole
				+ ", practitionerType=" + practitionerType + ", recordCreatedDate=" + recordCreatedDate
				+ ", practitionerLastName=" + practitionerLastName + ", practitionerFirstName=" + practitionerFirstName
				+ ", practitionerBillingNumber=" + practitionerBillingNumber + ", additionalGroupDetails="
				+ additionalGroupDetails + ", dateEmploymentStatusChanged=" + dateEmploymentStatusChanged
				+ ", practitionerBillingNumberNotAvailable=" + practitionerBillingNumberNotAvailable + "]";
	}

}
