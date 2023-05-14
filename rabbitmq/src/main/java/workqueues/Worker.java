package workqueues;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Worker {
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
        System.out.println("开始接受消息");
        channel.basicConsume(TASK_QUEUE_NAME,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"UTF-8");
                System.out.println("收到消息:"+message);
                try{
                    doWork(message);
                }finally {
                    System.out.println("消息处理完成");
                }
            }
        });

    }


    private static void doWork(String task){
        char[] chars = task.toCharArray();
        for (char ch:chars){
            if(ch == '.'){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
