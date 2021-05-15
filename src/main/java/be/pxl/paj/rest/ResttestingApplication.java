package be.pxl.paj.rest;

import be.pxl.paj.rest.domain.Employee;
import be.pxl.paj.rest.domain.EmployeeBuilder;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@SpringBootApplication
public class ResttestingApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResttestingApplication.class, args);
    }

}
