package com.victornobrega;

import com.rabbitmq.client.*;

public class EmitLogDirect {

    private static final String EXCHANGE_NAME = "direct_logs";
    private static final String FULL_NAME = "Victor Eduardo Japyassu Nobrega";

    public static void main(String[] argv) throws Exception {
        //Criacao de uma factory de conexao, responsavel por criar as conexoes
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //configurações do Qeue Menager
        connectionFactory.setUsername("mqadmin");
        connectionFactory.setPassword("Admin123XX_");
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            String severity = getSeverity(argv);
            String message = getMessage(argv);

            int dividirMensagem = message.length() / 2;
            String modifiedMessage = message.substring(0, dividirMensagem) + FULL_NAME + message.substring(dividirMensagem);
            channel.basicPublish(EXCHANGE_NAME, severity, null, modifiedMessage.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + severity + "':'" + modifiedMessage + "'");
        }

    }

    private static String getSeverity(String[] strings) {
        if (strings.length < 1)
            return "info";
        return strings[0];
    }

    private static String getMessage(String[] strings) {
        if (strings.length < 2)
            return "Hello World!";
        return joinStrings(strings, " ", 1);
    }

    private static String joinStrings(String[] strings, String delimiter, int startIndex) {
        int length = strings.length;
        if (length == 0) return "";
        if (length <= startIndex) return "";
        StringBuilder words = new StringBuilder(strings[startIndex]);
        for (int i = startIndex + 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}
