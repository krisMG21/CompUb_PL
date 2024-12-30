package mqtt;

import logic.Log;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTTPublisher {

    /**
     * Método para publicar un mensaje en un tópico MQTT.
     *
     * @param broker   El broker MQTT al que se conecta
     * @param topic    El tópico al que se publica el mensaje
     * @param content  El contenido del mensaje
     */
    public static void publish(MQTTBroker broker, String topic, String content) {
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            // Log de intento de conexión al broker MQTT
            Log.logmqtt.info("Intentando conectar al broker: {}", broker.getBroker());
            MqttClient sampleClient = new MqttClient(broker.getBroker(), broker.getClientId(), persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(broker.getUsername());
            connOpts.setPassword(broker.getPassword().toCharArray());
            connOpts.setCleanSession(true);

            // Conexión al broker MQTT
            sampleClient.connect(connOpts);
            Log.logmqtt.info("Conexión establecida con el broker: {}", broker.getBroker());

            // Creación del mensaje MQTT
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(broker.getQos());

            // Publicación del mensaje en el tópico
            sampleClient.publish(topic, message);
            Log.logmqtt.info("Mensaje publicado en el tópico: {}", topic);

            // Desconexión del cliente MQTT
            sampleClient.disconnect();
            Log.logmqtt.info("Desconectado del broker");

        } catch (MqttException me) {
            // Log de error si ocurre un MqttException
            Log.logmqtt.error("Error al publicar el mensaje: {}", me.getMessage(), me);
        } catch (Exception e) {
            // Log de error si ocurre una excepción general
            Log.logmqtt.error("Error inesperado al publicar el mensaje: {}", e.getMessage(), e);
        }
    }
}