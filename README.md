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



SENDGRID_API_KEY=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;SENDGRID_EMAIL_FROM=avocat.sys@gmail.com;SPRING_PROFILES_ACTIVE=staging