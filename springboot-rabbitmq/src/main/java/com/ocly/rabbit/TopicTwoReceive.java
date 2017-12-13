package com.ocly.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by cy
 * 2017/12/12 17:19
 */
@Component
@RabbitListener(queues = "topic.two")
public class TopicTwoReceive {
    @RabbitHandler
    public void process(String str) {
        System.out.println("topictwo>>receive>>" + str);
    }
}
