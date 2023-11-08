# pcdbi-clinicrecords-data-loader
Spring Boot Batch app to load CHEFS Clinic Record data from Health Ideas

## Overview
The Webforms data loader is used to load data from Health Ideas into CHEFS. The data is available in the Clinic Form and used in FTE Validation.

## Development
### Build
mvn clean package

### Run
mvn spring-boot:run

## TODO
* Add a configurable wait between requests to minimize the load on CHEFS
* Look into using the Upload multiple draft Submissions API

## Running the data loader

### Eclipse
1. Update the values in src/main/resources/application-dev.yaml as described below
2. Login to CHEFS and delete all previous submission for the correct Clinic Records form. E.g. (DEV) Clinic Records in Dev
3. Verify that no submissions are listed
4. Right click on project in Eclipse. Select Run As -> Spring Boot App
5. Verify the results in console when complete
6. Stop the application

### Standalone

### Prerequisities
1. Download and install Java 17. Ensure that java/bin is added to your classpath

### Execution
1. Update the values in config/application.properties as described below
2. Login to CHEFS and delete all previous submission for the correct Clinic Records form. E.g. (DEV) Clinic Records in Dev
3. Verify that no submissions are listed
4. Open a command prompt and run the following:
> java -jar pcdbi-clinicrecords-data-loader-1.0.0.jar
5. Verify the results in console when complete
6. Stop he application using CTRL+C

### Properties
<pre>
file:
  input: Input file location
chunk:
  size: Number of records to process at a time
chefs:
  url: https://submit.digital.gov.bc.ca/app/api/v1/
  wait: Amount of time to wait between requests. Recommend 3100 to get around CHEFS limitations.
  forms:
    clinicRecords:
      formId: Form Id in Prod
      apiKey: Api Key in Prod
    haHierarchy:
      formId: Form Id in Prod 
      apiKey: Api Key in Prod 
    reportingDates:
      formId:  Form Id in Prod
      apiKey:  Api Key in Prod
</pre>
