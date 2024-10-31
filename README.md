Web-application for accounting and organization of treatments in a health care facility.

Applied:
- Java 21
- SpringBoot 3.2.5
- MySQL
- Gradle 8.7
- Docker Compose

For starting:
1) Use DockerCompose for connection to the database. Docker settings see in compose.yaml.
2) Configure mail messages to doctors in src.main.resources.application.properties by entering the data of the required mail service for sending messages. Change e-mail for notification in "src.main.java.lemon.hospitaltable.table.services.NotificationService".
3) If necessary, change the spring.datasource database access data in src.main.resources.application.properties.
4) Create application-secrets.properties in "src.main.resources" with the following your data:
   openai.api.key=...
   spring.mail.password=...
   spring.security.oauth2.client.registration.google.client-id=...
   spring.security.oauth2.client.registration.google.client-secret=...
   prompt.recommendation-request-message="... ${patient} ... ${symptoms} ... ${doctors} ... ".
5) The application by default creates a test database with randomly generated in advance: 5 departments, 13 doctors, 14 wards, 2000 patients and 2000 treatments. If you need to add a certain number of random patients and treatments, use the generators in the "main.java.lemon.hospitaltable.generators" folder by entering the required values ​​in the variables (number, dates, names, diagnoses, etc.). Add the generated sql queries (generated in the same folder with the names insert_patients.sql and insert_treatments.sql) to "main.resources.db.migration" in the following versions, changing the names according to firefly regulations. Attention, treatment is generted without taking into account the capacity of the wards.
6) Starting application is at the src.main.java.lemon.hospitaltable.table.TableApplication.


Introduced:
* Ability to add/edit hospital departments;
* The possibility of adding/editing wards with the specified
* number, number of beds, floor and compartment;
* The possibility of adding/editing doctors (personal data) in the relevant departments;
* The possibility of registering the patient and specifying which ward he/she is scheduled to stay in and the attending physician (creation of treatment);
* When all beds in the department are occupied, the system does not allow such registration with a corresponding message;
* The possibility of viewing available beds in departments with employment statistics in % ratio;
* The possibility of transferring patients between departments;
* Notification of the doctor by mail in case of adding a new patient to him or changing the treatment of an existing one;
* Annual/quarterly report by hospital, department, doctor;
* Recommendation to which department and to which doctor it is better to appoint a given patient, taking into account the problem he encountered and his personal data;
* A feedback system for patients where they can rate doctors.
* Some unit tests.