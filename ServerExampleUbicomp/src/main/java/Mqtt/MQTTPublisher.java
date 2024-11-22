package mqtt;

import logic.Log;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTPublisher {

    /**
     *
     * @param broker
     * @param topic
     * @param content
     */
    public static void publish(MQTTBroker broker, String topic, String content) {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient sampleClient = new MqttClient(MQTTBroker.getBroker(), MQTTBroker.getClientId(), persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(MQTTBroker.getUsername());
            connOpts.setPassword(MQTTBroker.getPassword().toCharArray());
            connOpts.setCleanSession(true);
            Log.logmqtt.info("Connecting to broker: " + MQTTBroker.getBroker());
            sampleClient.connect(connOpts);
            Log.logmqtt.info("Connected");
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(MQTTBroker.getQos());
            sampleClient.publish(topic, message);
            Log.logmqtt.info("Message published");
            sampleClient.disconnect();
            Log.logmqtt.info("Disconnected");

        } catch (MqttException me) {
            Log.logmqtt.error("Error on publishing value: {}", me);
        } catch (Exception e) {
            Log.logmqtt.error("Error on publishing value: {}", e);
        }
    }
}
