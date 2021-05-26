package org.study.rabbitmq.fanout.routing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import org.study.rabbitmq.utils.Rabbitmq;
import org.study.rabbitmq.utils.Type;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Steven
 */
public class Produce {

;
    public static void main(String[] args) {

        Connection connection = null;
        try {
            //1 创建连接工厂
            connection = Rabbitmq.getConnection();

            //2.创建通道
            final Channel channel = connection.createChannel();

            // 创建交换器
            channel.exchangeDeclare(Rabbitmq.FANOUT_EXCHANGE_NAME, Type.FANOUT.name().toLowerCase(), true);

            //3.声明队列
            channel.queueDeclare(Rabbitmq.FANOUT_QUEUE_NAME, true, false, false, null);

            // 绑定队列到交换器
            channel.queueBind(Rabbitmq.FANOUT_QUEUE_NAME, Rabbitmq.FANOUT_EXCHANGE_NAME,"");

            // 4.要发送的消息
            String message = "Hello World-fanoutKey!";

            // 5.开启发送成功的通知
            channel.confirmSelect();

            // 设置回发送成功回调监听器
            channel.addConfirmListener(new ConfirmCallback() {
                @Override
                public void handle(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("成功发送消息到broker");
                }
            }, new ConfirmCallback() {
                @Override
                public void handle(long deliveryTag, boolean multiple) throws IOException {
                    System.out.println("发送消息到broker失败");
                }
            });

            for (int i = 0; i < 10; i++) {

                // 4.发送消息
                channel.basicPublish(Rabbitmq.FANOUT_EXCHANGE_NAME, "", null, message.getBytes());
                System.out.println(" [Message ] Sent '" + message + "'");
                Thread.sleep(2000);
            }

            System.in.read();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {

        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
