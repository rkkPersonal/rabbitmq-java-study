package org.study.rabbitmq.simple;

import com.rabbitmq.client.*;
import org.study.rabbitmq.utils.Rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Steven
 */
public class Consumer {

    public static void main(String[] args) {

        Connection connection = null;
        try {
            //1 创建连接工厂
            connection = Rabbitmq.getConnection();

            //2.创建通道
            Channel channel = connection.createChannel();

            //3.声明队列，如果不写会无法消费，必须声明队列
            channel.queueDeclare(Rabbitmq.QUEUE_NAME, true, false, false, null);

            // 消息成功收到消息回调通知
            DeliverCallback deliverCallback = new DeliverCallback() {
                @Override
                public void handle(String msgTag, Delivery delivery) throws IOException {
                    String message = new String(delivery.getBody(), "UTF-8");

                    System.out.println("消费者收到消息成功" + message);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };

            channel.basicConsume(Rabbitmq.QUEUE_NAME, deliverCallback, (s) -> {
                System.out.println("取消发送消息");
            });
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
