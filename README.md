# SofiS


## Build

### run database
`
docker run --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=postgres -d postgres
`

### run tests in staging
`
mvn test -Dspring.profiles.active=staging -f pom.xml
`

### run install in staging
`
mvn install -Dspring.profiles.active=staging -f pom.xml
`

## Jacoco - Code Covearge
Access directory `/target/site/index.html`



