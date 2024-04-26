# Test task for PUMB

## Description
Task is to app which can accept files only in xml and csv type files.
Also it has service to validate files and save it to DB. For this app I pick 
PostgresSQL as Database. Controller(/files/uploads) has two methods get and post. 
With method get u can get all entities in DB and filter by certain filed 
and sort by any field. 

## Used Technologies

* Spring Boot
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

## How to launch project without Docker
First you need to clone the repository:<br>
`git clone https://github.com/timmawv/TestTaskPUMB.git` <br>
After that you have to enter this directory and enter this command <br>
`mvn clean package` <br>
with skipping test <br>
`mvn clean package -Dmaven.test.skip` <br>
with skipping test for Window shell<br>
`mvn clean package --% -Dmaven.test.skip` <br>
Also if you don't have maven you can use maven wrapper <br>
`./mvnw clean package` <br>
After packaging project you can launch it <br> 
`java -jar target/TestTaskPUMB-0.0.1-SNAPSHOT.jar` <br>
Also this app has a liquibase, so the only thing you need is to create a database with
name `animals_db` and open it on port 5432. And liquibase will create table for data.

## How to launch project with Docker
First you need to clone the repository:<br>
`git clone https://github.com/timmawv/TestTaskPUMB.git` <br>
This app has a Dockerfile so you have to built an image use this command <br>
`docker build -t animals_uploader .` <br>
Then you just need to enter <br>
`docker compose up -d` <br>
It will create a docker containers with the app, so you can test it on `localhost:8080/files/uploads` <br>
If you want to run only image without a docker compose us this command. Also you can set you db_url connection <br>
`docker run --rm  -e DB_URL=jdbc:postgresql://postgres_db:5432/animals_db --name animals -p 8080:8080 animals_uploader`