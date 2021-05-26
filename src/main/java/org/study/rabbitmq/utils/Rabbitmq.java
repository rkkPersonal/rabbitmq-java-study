package org.study.rabbitmq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Steven
 */
public class Rabbitmq {

    public static final String QUEUE_NAME = "simple-queue-1";

    /**
     * direct mode
     */
    public static final String ROUTING_QUEUE_NAME = "routing.order";
    public static final String ROUTING_KEY = "#.order";
    public static final String DIRECT_EXCHANGE_NAME = "routing-exchange";

    /**
     * topic mode
     */
    public static final String TOPIC_KEY = "*.user";
    public static final String TOPIC_QUEUE_NAME = "topic.user";
    public static final String TOPIC_EXCHANGE_NAME = "topic-exchange";

    /**
     * fanout mode
     */
    public static final String FANOUT_QUEUE_NAME = "wechat-group-fanout";
    public static final String FANOUT_EXCHANGE_NAME = "fanout-exchange";

    public static Connection getConnection() throws TimeoutException, IOException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.43.200");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");
        Connection connection = connectionFactory.newConnection();
        return connection;
    }


}
