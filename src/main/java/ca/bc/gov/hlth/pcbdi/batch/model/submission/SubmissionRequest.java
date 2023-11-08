package ca.bc.gov.hlth.pcbdi.batch.model.submission;

public class SubmissionRequest {
    private String createdBy = "pcdbi-clinicrecords-data-loader";
    private Boolean draft = Boolean.FALSE;
    private Submission submission;

    public SubmissionRequest(Submission submission) {
        super();
        this.submission = submission;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getDraft() {
        return draft;
    }

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

}
