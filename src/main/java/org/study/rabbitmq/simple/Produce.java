package org.study.rabbitmq.simple;

import com.rabbitmq.client.*;
import org.study.rabbitmq.utils.Rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Steven
 */
public class Produce {


    public static void main(String[] args) {

        Connection connection = null;
        try {
            //1 创建连接工厂
            connection = Rabbitmq.getConnection();

            //2.创建通道
            Channel channel = connection.createChannel();

            //3.声明队列
            channel.queueDeclare(Rabbitmq.QUEUE_NAME, true, false, false, null);

            // 4.要发送的消息
            String message = "Hello World!";

            // 5.开启发送成功的通知
            channel.confirmSelect();

            // 设置回发送成功回调监听器
            channel.addConfirmListener(new ConfirmCallback() {
                @Override
                public void handle(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("收到消息");
                }
            }, new ConfirmCallback() {
                @Override
                public void handle(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("没有收到消息");
                }
            });

            // 4.发送消息
            channel.basicPublish("", Rabbitmq.QUEUE_NAME, null, message.getBytes());
            System.out.println(" [Message ] Sent '" + message + "'");
            System.in.read();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != connection) {
                    connection.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
