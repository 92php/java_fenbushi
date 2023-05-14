package direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 接受三个等级的日志
 */
public class ReceiveLogDirect1 {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //生成一个随机的临时的queue
        String queueName = channel.queueDeclare().getQueue();
        //一个交换机同时绑定3个queue
        channel.queueBind(queueName,EXCHANGE_NAME,"info");
        channel.queueBind(queueName,EXCHANGE_NAME,"warning");
        channel.queueBind(queueName,EXCHANGE_NAME,"error");

        System.out.println("开始接受消息");

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("收到消息:" + message);
            }
        };
        channel.basicConsume(queueName,true,defaultConsumer);
    }
}
