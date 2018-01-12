package com.threater.seating.startup;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * The Class ApplicationConfiguration.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.threater.seating")
public class ApplicationConfiguration {
}
