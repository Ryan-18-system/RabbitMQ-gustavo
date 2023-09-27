package roteamento;

import com.rabbitmq.client.*;

public class ConsumidorError {
    private static final String EXCHANGE_NAME = "exchange_direct";

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection conexao = connectionFactory.newConnection();
        Channel canal = conexao.createChannel();

        canal.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = canal.queueDeclare().getQueue();

        canal.queueBind(queueName, EXCHANGE_NAME, "error");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };

        canal.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
