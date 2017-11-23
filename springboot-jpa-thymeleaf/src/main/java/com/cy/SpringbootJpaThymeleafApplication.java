package com.cy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@Controller
@SpringBootApplication
public class SpringbootJpaThymeleafApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootJpaThymeleafApplication.class, args);
    }

    @GetMapping(value = "/")
    public String index() {

        return "redirect:/list";

    }
}
