package helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv {
    private final static String QUEUE_NAME = "hello";


    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工程
        ConnectionFactory factory = new ConnectionFactory();
        //设置rabbitmq地址
        factory.setHost("127.0.0.1");
        factory.setUsername("admin");
        factory.setPassword("123456");
        //建立连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //接受消息并消费
        channel.basicConsume(QUEUE_NAME,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"UTF-8");
                System.out.println("收到消息:"+message);
            }
        });


    }
}
