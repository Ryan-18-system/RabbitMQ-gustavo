package fila_duravel_mensagens_nao_persistentes;

import com.rabbitmq.client.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Consumidor {
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection conexao = connectionFactory.newConnection();
        Channel canal = conexao.createChannel();

        String NOME_FILA = "fila_duravel_mensagem_nao_persistente";
        canal.queueDeclare(NOME_FILA, true, false, false, null);
        canal.basicQos(1);

        String FILA_RESULTADO = "fila_resultado";
        canal.queueDeclare(FILA_RESULTADO, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            String[] parts = message.split("-");
            int numero = Integer.parseInt(parts[0]);

            System.out.println(" [x] Received '" + message + "'");
            if (numero == 1 || numero == 1000000) {
                try {
                    String timestamp = getCurrentTimestamp();
                    long timeDiff = calculateTimeDifference(timestamp, parts[1]);

                    System.out.println(" [x] Time Difference (ms): " + timeDiff);

                    canal.basicPublish("", FILA_RESULTADO, null, Long.toString(timeDiff).getBytes());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(" [x] Done");
            canal.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        boolean autoAck = false;
        canal.basicConsume(NOME_FILA, autoAck, deliverCallback, consumerTag -> {
        });
    }

    private static String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(new Date());
    }

    private static long calculateTimeDifference(String timestamp1, String timestamp2) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date1 = dateFormat.parse(timestamp1);
        Date date2 = dateFormat.parse(timestamp2);
        return date2.getTime() - date1.getTime();
    }
}
