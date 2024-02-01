package ca.bc.gov.hlth.pcbdi.batch.model.financial;

import java.util.ArrayList;
import java.util.Date;

public class ReportingDates {

    public String confirmationId;
    public Date createdAt;
    public String formId;
    public String formSubmissionStatusCode;
    public String submissionId;
    public boolean deleted;
    public String createdBy;
    public String formVersionId;
    public String fiscalYear;
    public ArrayList<PeriodReportingDate> periodReportingDates;
    public boolean lateEntry;

    public String getConfirmationId() {
        return confirmationId;
    }

    public void setConfirmationId(String confirmationId) {
        this.confirmationId = confirmationId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFormSubmissionStatusCode() {
        return formSubmissionStatusCode;
    }

    public void setFormSubmissionStatusCode(String formSubmissionStatusCode) {
        this.formSubmissionStatusCode = formSubmissionStatusCode;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getFormVersionId() {
        return formVersionId;
    }

    public void setFormVersionId(String formVersionId) {
        this.formVersionId = formVersionId;
    }

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public ArrayList<PeriodReportingDate> getPeriodReportingDates() {
        return periodReportingDates;
    }

    public void setPeriodReportingDates(ArrayList<PeriodReportingDate> periodReportingDates) {
        this.periodReportingDates = periodReportingDates;
    }

    public boolean isLateEntry() {
        return lateEntry;
    }

    public void setLateEntry(boolean lateEntry) {
        this.lateEntry = lateEntry;
    }

}
