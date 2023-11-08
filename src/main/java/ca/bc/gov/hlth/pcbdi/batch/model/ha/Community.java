package ca.bc.gov.hlth.pcbdi.batch.model.ha;

import java.util.ArrayList;

public class Community {
    public ArrayList<Pcn> pcn;
    public String communityName;
    public int hsiarServicePlanGapAnalysis;

    public ArrayList<Pcn> getPcn() {
        return pcn;
    }

    public void setPcn(ArrayList<Pcn> pcn) {
        this.pcn = pcn;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

}