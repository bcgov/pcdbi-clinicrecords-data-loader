package ca.bc.gov.hlth.pcbdi.batch.model.version;

import java.util.List;

public class FormVersions {
    private String name;
    private List<FormVersion> versions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FormVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<FormVersion> versions) {
        this.versions = versions;
    }

}
