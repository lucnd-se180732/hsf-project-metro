package com.hsf302.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.hsf302")
@EnableJpaRepositories(basePackages = "com.hsf302.repository")
@EntityScan(basePackages = "com.hsf302.pojo")
public class ProjectHsf302Application {

    public static void main(String[] args) {
        SpringApplication.run(ProjectHsf302Application.class, args);
    }

}
