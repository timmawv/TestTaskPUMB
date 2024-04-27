# Test task for PUMB

## Description
Task is to app which can accept files only in xml and csv type files.
Also it has service to validate files and save it to DB. For this app I pick 
PostgresSQL as Database. Controller(/files/uploads) has two methods get and post. 
With method get u can get all entities in DB and filter by certain field 
and sort by any field. 

## Used Technologies

* Spring Boot
* Postman
* Liquibase - migration for db
* PostgresSQL
* Docker
* Criteria API
* OpenCSV - for parsing csv files
* JAXB - for parsing xml files
* Lombok 
* MapStruct

## Swagger 
Swagger is available after launch app by this address:
`http://localhost:8080/swagger-ui/index.html`

## How to launch project with Docker
First you need to clone the repository:<br>
`git clone https://github.com/timmawv/TestTaskPUMB.git` <br>
Just put this command and app will start<br>
`docker compose up -d`<br>
Run Dockerfile<br>
`docker run --rm  -e DB_URL=jdbc:postgresql://localhost:5432/animals_db --name animals -p 8080:8080 animals_uploader`

## How to launch project without Docker
First you need to clone the repository:<br>
`git clone https://github.com/timmawv/TestTaskPUMB.git` <br>
Then build project to jar<br>
`mvn clean package -Dmaven.test.skip`<br>
After packaging project you can launch it <br>
`java -jar target/TestTaskPUMB-0.0.1-SNAPSHOT.jar`

## Postman collection for testing
[Open postman collection](postman_test_collection/Animals.postman_collection.json)