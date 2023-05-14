package workqueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class NewTask {

    private final static String TASK_QUEUE_NAME = "task_queue";


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
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        for (int i = 0; i < 10; i++) {
            String  message;
            if(i%2==0){
                message = i + "...";
            }else{
                message = String.valueOf(i);
            }
            //message = i + "...";
            channel.basicPublish("",TASK_QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("发送了消息:"+message);
        }

        channel.close();
        connection.close();
    }
}
