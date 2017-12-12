package com.ocly.rabbit;

import com.ocly.modle.User;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by cy
 * 2017/12/12 13:58
 */
@Component
@RabbitListener(queues = "hello")
public class Receivertow {

    @RabbitHandler
    public void process(String user) {
        System.out.println("receive2>>>>>" + user);
    }
}
