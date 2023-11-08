package ca.bc.gov.hlth.pcbdi.batch.model.financial;

import java.time.LocalDate;

public class InterimReportingDate {
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-ddThh:mm:ss")
//    @JsonDeserialize(using = LocalDateDeserializer.class)
//    @JsonSerialize(using = LocalDateSerializer.class)
    public String endDate;

    public int interim;

    public String startDate;

    public String submissionDueDate;

    public String validationDueDate;

    public LocalDate getEndLocalDate() {
        return stringDateToLocalDate(endDate);
    }

    public LocalDate getStartLocalDate() {
        return stringDateToLocalDate(startDate);
    }

    private LocalDate stringDateToLocalDate(String date) {
        // Date format is yyyy-MM-ddThh:mm:ss
        return LocalDate.of(Integer.parseInt(date.substring(0, 4)), Integer.parseInt(date.substring(5, 7)),
                Integer.parseInt(date.substring(8, 10)));
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getInterim() {
        return interim;
    }

    public void setInterim(int interim) {
        this.interim = interim;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getSubmissionDueDate() {
        return submissionDueDate;
    }

    public void setSubmissionDueDate(String submissionDueDate) {
        this.submissionDueDate = submissionDueDate;
    }

    public String getValidationDueDate() {
        return validationDueDate;
    }

    public void setValidationDueDate(String validationDueDate) {
        this.validationDueDate = validationDueDate;
    }

}