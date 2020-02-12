# wk-app

## Setup
Basic setup done with [vaadin-spring-security-demo](https://github.com/JenoDK/vaadin-spring-security-demo)

Make the file `application-dev.properties` with the contents
```$xslt
spring.datasource.username = root
spring.datasource.password = root
spring.datasource.url = jdbc:mysql://localhost:3306/wkApp
```
for your db connection.

## Run
```bash
# Run Vaadin compile in order to get correct widgetsets and styling and run spring boot after use:
./gradlew vaadinRun

# Run vaadin compile (will seup widgetset and styling):
./gradlew vaadinCompile

# Run spring boot:
./gradlew bootRun
```