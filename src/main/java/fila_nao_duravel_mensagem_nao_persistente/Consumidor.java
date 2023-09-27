package fila_nao_duravel_mensagem_nao_persistente;

import com.rabbitmq.client.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.rabbitmq.client.*;

public class Consumidor {
    private static final String NOME_FILA = "fila_nao_duravel_nao_persistente";

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection conexao = connectionFactory.newConnection();
        Channel canal = conexao.createChannel();

        canal.queueDeclare(NOME_FILA, false, false, false, null); // Fila não durável e mensagem não persistente
        canal.basicQos(1);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            long timestamp = Long.parseLong(message.split("-")[1]);
            long currentTime = System.currentTimeMillis();
            long timeDifference = currentTime - timestamp;

            System.out.println(" [x] Received '" + message + "'");
            System.out.println(" [x] Time difference (ms): " + timeDifference);
            canal.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        boolean autoAck = false;
        canal.basicConsume(NOME_FILA, autoAck, deliverCallback, consumerTag -> {});
    }
}
