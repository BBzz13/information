package spring.boot.user;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Yue
 */
public class UserTest {

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/frame");
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        // 1.2.建立连接
        Connection connection = connectionFactory.newConnection();

        // 2.创建通道Channel
        Channel channel = connection.createChannel();

        // 3.创建队列
        // public com.rabbitmq.client.AMQP.Queue.DeclareOk queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
        // queue:队列名称;
        // durable:是否持久化，当mq重启后，还在
        // exclusive：是否独占连接，只能有一个消费者监听这个队列，当Connection关闭时，是否删除队列
        // autoDelete 是否自动删除，当没有consumer时，是否自动删除
        // arguments 配置参数信息
        String queueName = "simple.queue";
        //如果没有queueName队列，会自动创建队列，如果有，则不创建
        channel.queueDeclare(queueName, false, false, false, null);

        // 4.发送消息
        String message = "hello, rabbitmq!";
        /*
        basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
        exchange:交换机名称，简单模式下交换机使用默认的
        routingKey：路由名称
        props：配置信息
        body：发送的消息数据
         */
        channel.basicPublish("", queueName, null, message.getBytes());
        System.out.println("发送消息成功：【" + message + "】");

        // 5.关闭通道和连接
        channel.close();
        connection.close();

    }
}
