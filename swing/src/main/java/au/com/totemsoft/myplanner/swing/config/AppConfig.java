package au.com.totemsoft.myplanner.swing.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
    basePackageClasses = {SwingConfig.class}
)
public class AppConfig {

}