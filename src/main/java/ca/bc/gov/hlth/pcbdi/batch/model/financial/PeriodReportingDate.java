package ca.bc.gov.hlth.pcbdi.batch.model.financial;

import java.util.Date;

public class PeriodReportingDate {
    public int period;
    public Date endDate;
    public Date startDate;
    public Date submissionDueDate;
    public Date validationDueDate;

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getSubmissionDueDate() {
        return submissionDueDate;
    }

    public void setSubmissionDueDate(Date submissionDueDate) {
        this.submissionDueDate = submissionDueDate;
    }

    public Date getValidationDueDate() {
        return validationDueDate;
    }

    public void setValidationDueDate(Date validationDueDate) {
        this.validationDueDate = validationDueDate;
    }

}