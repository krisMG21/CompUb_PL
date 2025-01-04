package Mqtt;

public class MQTTBroker {

    private int qos = 2;
    private final String broker = "tcp://172.20.10.3:1883";
    private final String clientId = "BibliotecaUAH"; 
    private final String username = "ubicua";
    private final String password = "ubicua";
    
    public MQTTBroker() {
    }

    public int getQos() {
        return qos;
    }

    public String getBroker() {
        return broker;
    }

    public String getClientId() {
        return clientId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
}
