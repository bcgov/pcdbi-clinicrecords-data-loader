package ca.bc.gov.hlth.pcbdi.batch.model.submission;

import java.util.List;

public class DeleteMultipleSubmissions {
    private List<String> submissionIds;

    public List<String> getSubmissionIds() {
        return submissionIds;
    }

    public void setSubmissionIds(List<String> submissionIds) {
        this.submissionIds = submissionIds;
    }

}
