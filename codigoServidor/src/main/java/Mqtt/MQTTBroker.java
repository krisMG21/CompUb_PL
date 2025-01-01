package mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;

public class MQTTBroker {
    private static final int qos = 2;
    private static final String broker = "tcp://192.168.10.134:1883"; // Ns si hay que cambiarlo
    private static final String clientId = "BibliotecaUAH"; 
    private static final String username = "ubicua";
    private static final String password = "ubicua";
    
    public MQTTBroker() {
    }

    public static int getQos() {
        return qos;
    }
    
    public static String getBroker() {
        return broker;
    }

    public static String getClientId() {
        return clientId;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}