package fila_duravel_mensagem_persistente;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Produtor {

    private static final int NUMERO_DE_MENSAGENS = 1000000;

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        try (
                Connection connection = connectionFactory.newConnection();
                Channel canal = connection.createChannel();
        ) {
            for (int i = 1; i <= NUMERO_DE_MENSAGENS; i++) {
                String mensagem = i + "-" + getCurrentTimestamp();
                String NOME_FILA = "fila_duravel_persistente";

                canal.queueDeclare(NOME_FILA, true, false, false, null);
                canal.basicPublish("", NOME_FILA, MessageProperties.PERSISTENT_TEXT_PLAIN, mensagem.getBytes());

                System.out.println(" [x] Sent '" + mensagem + "'");
            }
        }
    }

    private static String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(new Date());
    }
}
