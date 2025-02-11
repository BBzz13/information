package spring.boot.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerTest {
    public static void main(String[] args) throws IOException, TimeoutException {
        // 1.建立连接
        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("/frame");
        factory.setUsername("admin");
        factory.setPassword("admin");
        // 1.2.建立连接
        Connection connection = factory.newConnection();

        // 2.创建通道Channel
        Channel channel = connection.createChannel();

        // 3.创建队列
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);

        // 4.订阅消息
        //basicConsume(String queue, boolean autoAck, Consumer callback)
        //queue：队列名称
        //autoAck：是否自动确认
        //callback：回调对象
        channel.basicConsume(queueName, true, new DefaultConsumer(channel){
            /*
                回调方法，当收到消息后，会自动执行该方法
                1.consumerTag：标识
                2.envelope：获取一些信息
                3.properties：配置信息
                4.body：数据
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 5.处理消息
                String message = new String(body);
                System.out.println("接收到消息：【" + message + "】");
            }
        });
        System.out.println("等待接收消息。。。。");
    }
}
