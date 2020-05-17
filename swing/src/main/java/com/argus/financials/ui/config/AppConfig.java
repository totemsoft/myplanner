package com.argus.financials.ui.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
    "com.argus.financials.ui.config" // SwingConfig
})
public class AppConfig {

}