package ca.bc.gov.hlth.pcbdi.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.hlth.pcbdi.batch.model.clinic.ClinicRecord;
import ca.bc.gov.hlth.pcbdi.batch.model.financial.ReportingDates;
import ca.bc.gov.hlth.pcbdi.batch.model.ha.HaSubmission;
import ca.bc.gov.hlth.pcbdi.batch.model.submission.DeleteMultipleSubmissions;
import ca.bc.gov.hlth.pcbdi.batch.model.submission.Submission;
import ca.bc.gov.hlth.pcbdi.batch.model.submission.SubmissionRequest;
import ca.bc.gov.hlth.pcbdi.batch.model.submission.SubmissionResponse;
import ca.bc.gov.hlth.pcbdi.batch.model.version.FormVersion;
import ca.bc.gov.hlth.pcbdi.batch.model.version.FormVersions;
import jakarta.annotation.PostConstruct;

@Service
public class ChefsService {

    private static final Logger logger = LoggerFactory.getLogger(ChefsService.class);
    
    @Value("${chefs.forms.clinicRecords.formId}")
    private String clinicRecordsFormId;
    
    @Value("${chefs.forms.clinicRecords.apiKey}")
    private String clinicRecordsApikey;
    
    @Value("${chefs.forms.haHierarchy.formId}")
    private String haHierarchyFormId;
    
    @Value("${chefs.forms.haHierarchy.apiKey}")
    private String haHierarchyApikey;
    
    @Value("${chefs.forms.reportingDates.formId}")
    private String reportingDatesFormId;
    
    @Value("${chefs.forms.reportingDates.apiKey}")
    private String reportingDatesApikey;
    
    private String submissionpath = "forms/%s/submissions?deleted=false&draft=false";
    
    private String haSubmissionPath = submissionpath + "&fields=healthAuthority,communities";
    
    private String reportingDatesSubmissionPath = submissionpath + "&fields=fiscalYear,periodReportingDates";
    
    private String formSubmissionPath = "forms/%s/versions/%s/submissions";
    
    private String deleteSubmissionPath = "/submissions/%s";
    
    private String deleteMultipleSubmissionsPath = "/submissions/%s/%s/submissions";
    
    private String publishedFormVersionPath = "forms/%s/version";
    
    private String clinicRecordsFormVersion;
    
    @Autowired
    private WebClient chefsWebClient;
    
    @PostConstruct
    public void init() {
        Optional<FormVersion> opt = getPublishedFormVersion(clinicRecordsFormId, clinicRecordsApikey).getBody().getVersions().stream().filter((version) -> version.getPublished()).findAny();
        clinicRecordsFormVersion  = opt.orElseThrow(() -> new BatchConfigurationException("Could not find latest version of Clinic Records form")).getId();
    }
    
    public ResponseEntity<List<HaSubmission>> getHealthAuthorities() {
        String path = String.format(haSubmissionPath, haHierarchyFormId);
        return chefsWebClient.get()
                .uri(path)
                .headers(header -> header.setBasicAuth(haHierarchyFormId, haHierarchyApikey))
                .retrieve()
                .toEntityList(HaSubmission.class)
                .block();
    }
    
    public ResponseEntity<List<ReportingDates>> getReportingDates() {
        String path = String.format(reportingDatesSubmissionPath, reportingDatesFormId);
        return chefsWebClient.get()
                .uri(path)
                .headers(header -> header.setBasicAuth(reportingDatesFormId, reportingDatesApikey))
                .retrieve()
                .toEntityList(ReportingDates.class)
                .block();
    }
    
    public ResponseEntity<List<SubmissionResponse>> getSubmissions(LocalDateTime createdAtFrom, LocalDateTime createdAtTo) {
        String path = String.format(submissionpath, clinicRecordsFormId);
        if (createdAtFrom != null) {
            path = path + "&createdAt=" + convertDateTime(createdAtFrom).toString();            
        }
        if (createdAtTo != null) {
            path = path + "&createdAt=" + convertDateTime(createdAtTo).toString();            
        }
        return chefsWebClient.get()
                .uri(path)
                .headers(header -> header.setBasicAuth(clinicRecordsFormId, clinicRecordsApikey))
                .retrieve()
                .toEntityList(SubmissionResponse.class)
                .block();
    }
    
    private ZonedDateTime convertDateTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC);
    }
    
    public ResponseEntity<Submission> deleteSubmission(String submissionId) {
        
        // The path requires a formSubmissionId even though it's part of the body so just pick the first one        
        String path = String.format(deleteSubmissionPath, submissionId);
        // Technically a DELETE op shouldn't have a body so we have to use .method to allow it
        return chefsWebClient.delete()
                .uri(path)
                // TODO (weskubo-cgi) Delete requests aren't working with API key
                .headers(header -> header.setBasicAuth(clinicRecordsFormId, clinicRecordsApikey))
                .retrieve()
                .toEntity(Submission.class)
                .block();
    }
    
    public ResponseEntity<List<Submission>> deleteMultipleSubmissions(List<String> submissionIds) {
        DeleteMultipleSubmissions deleteMultipleSubmissions = new DeleteMultipleSubmissions();
        deleteMultipleSubmissions.setSubmissionIds(submissionIds);
        
        // The path requires a formSubmissionId even though it's part of the body so just pick the first one        
        String path = String.format(deleteMultipleSubmissionsPath, submissionIds.get(0), clinicRecordsFormId);
        // Technically a DELETE op shouldn't have a body so we have to use .method to allow it
        return chefsWebClient.method(HttpMethod.DELETE)
                .uri(path)
                // TODO (weskubo-cgi) Delete requests aren't working with API key
                .headers(header -> header.setBasicAuth(clinicRecordsFormId, clinicRecordsApikey))
                .bodyValue(deleteMultipleSubmissions)
                .retrieve()
                .toEntityList(Submission.class)
                .block();
    }
    
    public Submission createClinicRecord(ClinicRecord clinicRecord) {
        
        Submission submission = new Submission();
        submission.setData(clinicRecord);
        SubmissionRequest submissionRequest = new SubmissionRequest(submission);


        String path = String.format(formSubmissionPath, clinicRecordsFormId, clinicRecordsFormVersion);
        try {
            return chefsWebClient.post()
                    .uri(path)
                    .headers(header -> header.setBasicAuth(clinicRecordsFormId, clinicRecordsApikey))
                    .bodyValue(submissionRequest)
                    .retrieve()
                    .bodyToMono(Submission.class)
                    .block();    
        } catch (Exception e) {
            
            try {
                String obj = new ObjectMapper().writeValueAsString(submissionRequest);
                logger.error("Could not process submission {}", obj);
            } catch (JsonProcessingException jpe) {
                // Ignore as this is just a secondary error
            }
            throw e;
        }
        
    }
    
    private ResponseEntity<FormVersions> getPublishedFormVersion(String formid, String apiKey) {
        String path = String.format(publishedFormVersionPath, formid);
        return chefsWebClient.get()
                .uri(path)
                .headers(header -> header.setBasicAuth(formid, apiKey))
                .retrieve()
                .toEntity(FormVersions.class)
                .block();
    }

}
