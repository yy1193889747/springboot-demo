package com.ocly.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by cy
 * 2017/12/12 17:17
 */
@Component
@RabbitListener(queues = "topic.one")
public class TopicOneReceive {

    @RabbitHandler
    public void process(String str) {
        System.out.println("topicone>>receive>>" + str);
    }
}
