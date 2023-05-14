package com.imooc.springbootrabbitmqconsumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "queue1")
public class Receiver1 {

    @RabbitHandler
    public void process(String message){
        System.out.println("Receiver1:"+message);
    }
}
