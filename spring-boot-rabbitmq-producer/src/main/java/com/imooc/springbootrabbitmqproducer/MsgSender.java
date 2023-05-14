package com.imooc.springbootrabbitmqproducer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MsgSender {

    @Autowired
    private AmqpTemplate rabbitmqTemplate;

    public void send1(){
        String message = "this is message 1,routing key is dog.red";
        System.out.println("发送了："+message);
        this.rabbitmqTemplate.convertAndSend("bootExchange","dog.red",message);
    }

    public void send2(){
        String message = "this is message 2,routing key is dog.black";
        System.out.println("发送了："+message);
        this.rabbitmqTemplate.convertAndSend("bootExchange","dog.black",message);
    }
}
