package ca.bc.gov.hlth.pcbdi.batch.listener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ca.bc.gov.hlth.pcbdi.batch.model.submission.SubmissionResponse;
import ca.bc.gov.hlth.pcbdi.service.ChefsService;

@Component
public class ClinicRecordsJobExecutionListener implements JobExecutionListener {
	private static final Logger logger = LoggerFactory.getLogger(ClinicRecordsJobExecutionListener.class);
	
	private static final LocalDateTime MIN_CREATED_DATE = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
	private static final LocalDateTime MAX_CREATED_DATE = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
	
	private static final Integer CHUNK_SIZE = 50;
	
	@Autowired
	private ChefsService chefsService;
	
	@AfterJob
	@Transactional
	public void afterJob(JobExecution jobExecution) {
        logger.info("Job completed with status " + jobExecution.getStatus());
 
        LocalDateTime startDateTime = jobExecution.getStartTime();
        
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
		    // TODO (weskubo-cgi) CHEFS does not currently support DELETE with BasicAuth so make it a manual step in CHEFS for now
            // Remove all records created before the job run
//            List<SubmissionResponse> submissions = chefsService.getSubmissions(MIN_CREATED_DATE, startDateTime).getBody();
//            
//            List<List<SubmissionResponse>> subChunks = ListUtils.partition(submissions, CHUNK_SIZE);
//            
//            subChunks.forEach(chunk -> {
//                List<String> submissionIds = chunk.stream().map(submission -> submission.getSubmissionId()).collect(Collectors.toList());
//                chefsService.deleteMultipleSubmissions(submissionIds);
//            });
//            
//            
//            logger.info("Deleting {} old submissions", submissions.size());	    
		}
		if (jobExecution.getStatus() == BatchStatus.FAILED) {
	          // TODO (weskubo-cgi) CHEFS does not currently support DELETE with BasicAuth so make it a manual step in CHEFS for now
			logger.error("Job failed. Rolling back data.");

//            // Remove all records created during the run
//            // Just use start of day just to be safe
//            List<SubmissionResponse> submissions = chefsService.getSubmissions(startDateTime, MAX_CREATED_DATE).getBody();
//            logger.info("Deleting {} new submissions", submissions.size());
//            
//            List<List<SubmissionResponse>> subChunks = ListUtils.partition(submissions, CHUNK_SIZE);
//            
//            subChunks.forEach(chunk -> {
//                List<String> submissionIds = chunk.stream().map(submission -> submission.getSubmissionId()).collect(Collectors.toList());
//                chefsService.deleteMultipleSubmissions(submissionIds);
//            });
	    }
	}
	
}
