package ca.bc.gov.hlth.pcbdi.batch.model.clinic;

import java.util.ArrayList;
import java.util.List;

import ca.bc.gov.hlth.pcbdi.batch.model.submission.Data;

public class ClinicRecord extends Data {
	private String pcnName;
	private String clinicID;
	private String clinicName;
	private String clinicType;
	private String pcnCommunity;
	private String initiativeType;
	private String healthAuthority;
	private String selectTheReportingLevel;

	private List<ClinicRecordDetail> clinicRecordDetails = new ArrayList<>();

	public String getPcnName() {
		return pcnName;
	}

	public void setPcnName(String pcnName) {
		this.pcnName = pcnName;
	}

	public String getClinicID() {
		return clinicID;
	}

	public void setClinicID(String clinicID) {
		this.clinicID = clinicID;
	}

	public String getClinicName() {
		return clinicName;
	}

	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}

	public String getClinicType() {
		return clinicType;
	}

	public void setClinicType(String clinicType) {
		this.clinicType = clinicType;
	}

	public String getPcnCommunity() {
		return pcnCommunity;
	}

	public void setPcnCommunity(String pcnCommunity) {
		this.pcnCommunity = pcnCommunity;
	}

	public String getInitiativeType() {
		return initiativeType;
	}

	public void setInitiativeType(String initiativeType) {
		this.initiativeType = initiativeType;
	}

	public String getHealthAuthority() {
		return healthAuthority;
	}

	public void setHealthAuthority(String healthAuthority) {
		this.healthAuthority = healthAuthority;
	}

	public String getSelectTheReportingLevel() {
		return selectTheReportingLevel;
	}

	public void setSelectTheReportingLevel(String selectTheReportingLevel) {
		this.selectTheReportingLevel = selectTheReportingLevel;
	}

	public List<ClinicRecordDetail> getClinicRecordDetails() {
		return clinicRecordDetails;
	}

	public void setClinicRecordDetails(List<ClinicRecordDetail> clinicRecordDetails) {
		this.clinicRecordDetails = clinicRecordDetails;
	}

}
