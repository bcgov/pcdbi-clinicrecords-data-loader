package ca.bc.gov.hlth.pcbdi.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class Step1ExecutionListener implements StepExecutionListener {
    private static final Logger logger = LoggerFactory.getLogger(Step1ExecutionListener.class);

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        logger.info("Read {} records from input file", stepExecution.getReadCount());
        logger.info("Skipped {} records with non-matching RECORD_TYPE", stepExecution.getFilterCount());
        logger.info("Skipped {} records with invalid RECORD_TYPE", stepExecution.getWriteSkipCount());
        logger.info("Wrote {} records to CHEFS", stepExecution.getWriteCount());

//        logger.error("------------------------------------------------------------------------------------");
//        logger.error("StepExecutionListener - afterStep:getCommitCount=" + stepExecution.getCommitCount());
//        logger.error("StepExecutionListener - afterStep:getFilterCount=" + stepExecution.getFilterCount());
//        logger.error("StepExecutionListener - afterStep:getProcessSkipCount=" + stepExecution.getProcessSkipCount());
//        logger.error("StepExecutionListener - afterStep:getReadCount=" + stepExecution.getReadCount());
//        logger.error("StepExecutionListener - afterStep:getReadSkipCount=" + stepExecution.getReadSkipCount());
//        logger.error("StepExecutionListener - afterStep:getRollbackCount=" + stepExecution.getRollbackCount());
//        logger.error("StepExecutionListener - afterStep:getWriteCount=" + stepExecution.getWriteCount());
//        logger.error("StepExecutionListener - afterStep:getWriteSkipCount=" + stepExecution.getWriteSkipCount());
//        logger.error("StepExecutionListener - afterStep:getStepName=" + stepExecution.getStepName());
//        logger.error("StepExecutionListener - afterStep:getSummary=" + stepExecution.getSummary());
//        logger.error("StepExecutionListener - afterStep:getStartTime=" + stepExecution.getStartTime());
//        logger.error("StepExecutionListener - afterStep:getStartTime=" + stepExecution.getEndTime());
//        logger.error("StepExecutionListener - afterStep:getLastUpdated=" + stepExecution.getLastUpdated());
//        logger.error("StepExecutionListener - afterStep:getExitStatus=" + stepExecution.getExitStatus());
//        logger.error("StepExecutionListener - afterStep:getFailureExceptions=" + stepExecution.getFailureExceptions());
//        logger.error("------------------------------------------------------------------------------------");

        return null;
    }
}
