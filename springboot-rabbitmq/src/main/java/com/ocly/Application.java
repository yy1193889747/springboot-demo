package com.ocly;

import com.ocly.rabbit.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.awt.SunHints;

@RestController
@SpringBootApplication
public class Application {
    @Autowired
    private Sender sender;
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @GetMapping(value ="/rabbit")
    public void hello() {
        sender.send();
    }
}
