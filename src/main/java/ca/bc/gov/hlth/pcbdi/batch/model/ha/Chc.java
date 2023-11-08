package ca.bc.gov.hlth.pcbdi.batch.model.ha;

import java.util.ArrayList;

//import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
//import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
public class Chc{
 public String chcName;
 public ArrayList<ChcClinic> chcClinic;
}