package fila_nao_duravel_mensagem_nao_persistente;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.rabbitmq.client.*;

public class Produtor {
    private static final String NOME_FILA = "fila_nao_duravel_nao_persistente";
    private static final int NUMERO_DE_MENSAGENS = 1000000; // Número de mensagens a serem enviadas

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try (
                Connection connection = connectionFactory.newConnection();
                Channel canal = connection.createChannel();
        ) {
            canal.queueDeclare(NOME_FILA, false, false, false, null); // Fila não durável e mensagem não persistente

            for (int i = 1; i <= NUMERO_DE_MENSAGENS; i++) {
                String mensagem = i + "-" + System.currentTimeMillis(); // Mensagem com número e timestamp
                canal.basicPublish("", NOME_FILA, null, mensagem.getBytes());
                System.out.println(" [x] Sent '" + mensagem + "'");
            }
        }
    }
}
