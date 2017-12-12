package com.ocly.rabbit;

import com.ocly.modle.User;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Created by cy
 * 2017/12/12 14:28
 */
@Component
@RabbitListener(queues = "user")
public class ObjReceive {

    @RabbitHandler
    public void process(User user){
        System.out.println("objreceive:"+user.toString());
    }
}
