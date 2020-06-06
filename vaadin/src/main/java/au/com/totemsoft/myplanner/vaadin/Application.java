package au.com.totemsoft.myplanner.vaadin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import au.com.totemsoft.myplanner.config.ServiceConfig;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication(
    exclude = ErrorMvcAutoConfiguration.class
)
@ComponentScan(
    basePackages = { "au.com.totemsoft.myplanner.vaadin.config" },
    basePackageClasses = {
        ServiceConfig.class // from core module
    }
)
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
