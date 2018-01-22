package com.ocly;

import com.ocly.modle.User;
import com.ocly.rabbit.ObjSender;
import com.ocly.rabbit.Sender;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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

    @Bean
    public TomcatEmbeddedServletContainerFactory tomcatEmbedded() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            if ((connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>)) {
                //-1 means unlimited
                ((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setMaxSwallowSize(-1);
            }
        });
        return tomcat;
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

    @GetMapping("/email")
    public ModelAndView mailTemplate(ModelMap model) {
        model.addAttribute("name", "ocly");

        return new ModelAndView("emailTemplate", model);
    }

    @PostMapping(value = "/test")
    public String helloall(String name, String age) {
        return "Hello " + age + " de " + name;
    }
}
