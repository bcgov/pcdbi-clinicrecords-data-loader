package ca.bc.gov.hlth.pcbdi.batch.model.submission;

public abstract class Data {
    private Boolean lateEntry = Boolean.FALSE;
    private String submissionId;

    public Boolean getLateEntry() {
        return lateEntry;
    }

    public void setLateEntry(Boolean lateEntry) {
        this.lateEntry = lateEntry;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

}
