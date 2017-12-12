package com.ocly;

import com.ocly.modle.User;
import com.ocly.rabbit.ObjSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application {
    @Autowired
    private ObjSender sender;

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

    @GetMapping(value = "/rabbit")
    public void hello() {
        User user = new User();
        user.setAge(18);
        user.setName("ocly");
        sender.send(user);
    }
}
