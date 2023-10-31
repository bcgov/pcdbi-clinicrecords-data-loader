# pcdbi-clinicrecords-data-loader
Spring batch app to load CHEFS Clinic Record data from Health Ideas

## Overview
The Webforms data loader is used to load data from Health Ideas into CHEFS. The data is available in the Clinic Form and used in FTE Validation.

## Development
mvn clean package

mvn spring-boot:run

## TODO
Record count


## Production
1. Download and install Java 17. Ensure that java/bin is added to your classpath
2. Update the values in config/application.properties folder with the following values:
server:
  port: 9090
file:
  input: <Input file location?
chefs:
  url: https://submit.digital.gov.bc.ca/app/api/v1/
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

2. Run the application
Open a command prompt and run the following:

java -jar webforms-data-loader-0.0.1-SNAPSHOT.jar

Once the process is complete, you must manually stop it: CTRL+C
