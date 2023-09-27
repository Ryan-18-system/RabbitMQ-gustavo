package roteamento;

import com.rabbitmq.client.*;

public class Produtor {
    private static final String EXCHANGE_NAME = "exchange_direct";
    private static final String[] SEVERITIES = { "info", "warning", "error" };

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try (
                Connection connection = connectionFactory.newConnection();
                Channel channel = connection.createChannel();
        ) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            for (String severity : SEVERITIES) {
                String message = "Log message with severity: " + severity;
                channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
                System.out.println(" [x] Sent '" + message + "' with severity '" + severity + "'");
            }
        }
    }
}
