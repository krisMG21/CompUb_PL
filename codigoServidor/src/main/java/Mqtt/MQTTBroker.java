package mqtt;

public class MQTTBroker {

    private static int qos = 2;
    private static final String broker = "tcp://localhost:1883"; // Ns si hay que cambiarlo
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
        return password;
    }

    public static String getPassword() {
        return password;
    }
    
}
