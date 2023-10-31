package ca.bc.gov.hlth.pcbdi.batch.model.ha;

import java.util.ArrayList;
import java.util.Date;

public class HaSubmission {
    public String confirmationId;
    public Date createdAt;
    public String formId;
    public String formSubmissionStatusCode;
    public String submissionId;
    public boolean deleted;
    public String createdBy;
    public String formVersionId;
    public String healthAuthority;
    public ArrayList<Community> communities;
    public boolean lateEntry;

    public String getHealthAuthority() {
        return healthAuthority;
    }

    public void setHealthAuthority(String healthAuthority) {
        this.healthAuthority = healthAuthority;
    }

    public ArrayList<Community> getCommunities() {
        return communities;
    }

    public void setCommunities(ArrayList<Community> communities) {
        this.communities = communities;
    }

}
