package com.ocly.rabbit;

import com.ocly.modle.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cy
 * 2017/12/12 10:06
 */
@Component
public class ObjSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(User user) {
        System.out.println("senderobj>>>" + user.toString());
        this.rabbitTemplate.convertAndSend("user", user);
    }


}
