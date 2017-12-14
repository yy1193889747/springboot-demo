package com.ocly;

import com.ocly.modle.User;
import com.ocly.rabbit.ObjSender;
import com.ocly.rabbit.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@SpringBootApplication
public class Application {
    @Autowired
    private ObjSender sender;
    @Autowired
    private Sender send;

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

    @GetMapping(value = "/topic/one")
    public void hellotopic() {
        send.topicSendOne();
    }

    @GetMapping(value = "/topic/two")
    public void topictwo() {
        send.topicSendTwo();
    }

    @GetMapping("/")
    public ModelAndView mailTemplate(ModelMap model) {
        model.addAttribute("name","ocly");

        return new ModelAndView("emailTemplate",model);
    }

}
