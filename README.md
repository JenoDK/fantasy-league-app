# Spring Boot application with Vaadin and Spring Security
A basic demo application to showcase Spring Boot in combination with Spring Security and Vaadin

## Notable dependencies used
* spring-boot with data-jpa, security and web version 2.0.0.RELEASE
* vaadin-spring-boot-starter
* com.vaadin:vaadin-bom:8.3.1
* org.liquibase:liquibase-core:3.5.5
* io.reactivex.rxjava2:rxjava:2.1.10

## Run
```bash
./gradlew bootRun
```

##### v0.2
* UserRepository and RoleRepository
* Integration of these repo's with spring-security authentication
* User registration
* Basic db initialization with liquibase

##### v0.1
* Basic setup with spring-boot, vaadin and spring-security.<br/>
* Users are kept in-memory.<br/>
* Redirects to UI's and MainUI has a SpringViewProvider to handle views inside MainUI