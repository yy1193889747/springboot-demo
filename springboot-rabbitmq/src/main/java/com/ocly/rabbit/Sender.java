package com.ocly.rabbit;

import com.ocly.modle.User;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by cy
 * 2017/12/12 10:06
 */
@Component
public class Sender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String context = "hello world";
        System.out.println("sender>>>" + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }


    public void topicSendone() {
        String context = "hello topicSendone";
        System.out.println("sender>>>" + context);
        this.rabbitTemplate.convertAndSend("topicexchange","topic.one",context);
    }

    public void topicSendTwo() {
        String context = "hello topicSendTwo";
        System.out.println("sender>>>" + context);
        this.rabbitTemplate.convertAndSend("topicexchange","topic.two",context);
    }

}
