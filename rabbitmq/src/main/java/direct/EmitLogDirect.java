package direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        String message = "hello world2!";
        channel.basicPublish(EXCHANGE_NAME,"info",null,message.getBytes("UTF-8"));
        System.out.println("发送了消息,等级为info,消息内容："+message);


        channel.basicPublish(EXCHANGE_NAME,"warning",null,message.getBytes("UTF-8"));
        System.out.println("发送了消息,等级为warning,消息内容："+message);

        channel.basicPublish(EXCHANGE_NAME,"error",null,message.getBytes("UTF-8"));
        System.out.println("发送了消息,等级为error,消息内容："+message);


        channel.close();
        connection.close();
    }
}
