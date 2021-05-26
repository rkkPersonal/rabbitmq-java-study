package org.study.rabbitmq.fanout.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.study.rabbitmq.utils.Rabbitmq;
import org.study.rabbitmq.utils.Type;

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
            channel.exchangeDeclare(Rabbitmq.FANOUT_EXCHANGE_NAME, Type.FANOUT.name().toLowerCase(),true);

            //3.声明队列，如果不写会无法消费，必须声明队列
            channel.queueDeclare(Rabbitmq.FANOUT_QUEUE_NAME, true, false, false, null);
            channel.queueBind(Rabbitmq.FANOUT_QUEUE_NAME,Rabbitmq.FANOUT_EXCHANGE_NAME, "");

            // 消息成功收到消息回调通知
            DeliverCallback deliverCallback = new DeliverCallback() {
                @Override
                public void handle(String msgTag, Delivery delivery) throws IOException {
                    String message = new String(delivery.getBody(), "UTF-8");

                    System.out.println("消费者收到消息成功" + message);

                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }
            };

            channel.basicConsume(Rabbitmq.FANOUT_QUEUE_NAME, false,deliverCallback, (s) -> {
                System.out.println("取消发送消息");
            });

      /*      channel.basicConsume(Rabbitmq.ROUTING_QUEUE_NAME, false,
                    new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag,
                                                   Envelope envelope,
                                                   AMQP.BasicProperties properties,
                                                   byte[] body)
                                throws IOException
                        {
                            long deliveryTag = envelope.getDeliveryTag();
                            // positively acknowledge all deliveries up to
                            // this delivery tag
                            System.out.println("收到："+envelope.getDeliveryTag());
                            channel.basicAck(deliveryTag, true);
                        }
                    });*/

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
