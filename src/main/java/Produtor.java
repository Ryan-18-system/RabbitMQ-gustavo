import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Produtor {

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        try (
                Connection connection = connectionFactory.newConnection();
                Channel canal = connection.createChannel();
        ) {
            String mensagem = "Olá Ryan Nóbrega Brandão"+"................";
            String NOME_FILA = "plica";

            //(queue, passive, durable, exclusive, autoDelete, arguments)
            canal.queueDeclare(NOME_FILA, true, false, false, null);
            canal.basicPublish("",NOME_FILA, MessageProperties.PERSISTENT_TEXT_PLAIN, mensagem.getBytes());

            System.out.println(" [x] Sent '" + mensagem + "'");
            // ​(exchange, routingKey, mandatory, immediate, props, byte[] body)
//            canal.basicPublish("", NOME_FILA, false, false, null, mensagem.getBytes());

        }
    }
}


