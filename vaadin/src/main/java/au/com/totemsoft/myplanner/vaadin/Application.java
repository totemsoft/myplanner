package au.com.totemsoft.myplanner.vaadin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import au.com.totemsoft.myplanner.config.ServiceConfig;
import au.com.totemsoft.myplanner.security.SecurityConfig;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication(
    exclude = ErrorMvcAutoConfiguration.class
)
@ComponentScan(
    basePackages = { "au.com.totemsoft.myplanner.vaadin.config" },
    basePackageClasses = {
        SecurityConfig.class,
        ServiceConfig.class
    }
)
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
