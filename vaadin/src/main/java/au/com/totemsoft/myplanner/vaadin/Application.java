package au.com.totemsoft.myplanner.vaadin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "au.com.totemsoft.myplanner.vaadin.config",
    "com.argus.financials.config" // @see com.argus.financials.config.ServiceConfig
})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
