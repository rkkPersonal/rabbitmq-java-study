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
    public static final String ROUTING_QUEUE_NAME = "routing.order";
    public static final String ROUTING_KEY="#.order";
    public static final String DIRECT_EXCHANGE_NAME = "routing-exchange";

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
