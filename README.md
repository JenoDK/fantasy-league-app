# Spring Boot application with Vaadin and Spring Security
A basic demo application to showcase Spring Boot in combination with Spring Security and Vaadin

## Spring security config
See the SpringSecurityConfig class for more customization

## Authentication
Authentication is done programmatically in LoginUI 
For the sake of the demo the [UserDetailsService](http://www.baeldung.com/spring-security-authentication-with-a-database)
will be in memory.

## Redirect
After successful authentication the user will be redirected to the MainUI where the [SpringViewProvider](https://vaadin.com/api/vaadin-spring/com/vaadin/spring/navigator/SpringViewProvider.html)
takes over.
<br/>
<br/>
One more issue I didn't figure out is the redirect path is "/" and changing this will break stuff.

## Views
Define more views in State and by defining a 
[View](https://vaadin.com/api/com/vaadin/navigator/View.html) 
with the [@SpringView](https://vaadin.com/api/vaadin-spring/com/vaadin/spring/annotation/SpringView.html) annotation.

## Run
```bash
./gradlew bootRun
```

