package ca.bc.gov.hlth.pcbdi.batch.model.financial;

import java.util.Date;

public class QuarterReportingDate {
    public Date endDate;
    public int quarter;
    public Date startDate;
    public Date submissionDueDate;
    public Date validationDueDate;
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public int getQuarter() {
        return quarter;
    }
    public void setQuarter(int quarter) {
        this.quarter = quarter;
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
