package com.hsf302;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.hsf302.pojo")
@EnableJpaRepositories("com.hsf302.repository")
public class ProjectHsf302Application {

    public static void main(String[] args) {
        SpringApplication.run(ProjectHsf302Application.class, args);
    }

}
