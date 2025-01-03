package Mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import Database.Topics;
import Logic.Log;

public class MQTTSuscriber implements MqttCallback {

    public void suscribeTopic(MQTTBroker broker, String topic) {
        Log.logmqtt.debug("Suscribe to topics");
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient sampleClient = new MqttClient(MQTTBroker.getBroker(), MQTTBroker.getClientId(), persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(MQTTBroker.getUsername());
            connOpts.setPassword(MQTTBroker.getPassword().toCharArray());
            connOpts.setCleanSession(true);
            Log.logmqtt.debug("Mqtt Connecting to broker: " + MQTTBroker.getBroker());
            sampleClient.connect(connOpts);
            Log.logmqtt.debug("Mqtt Connected");
            sampleClient.setCallback(this);

            sampleClient.subscribe(topic);
            Log.logmqtt.info("Subscribed to {}", topic);

        } catch (MqttException me) {
            Log.logmqtt.error("Error suscribing topic: {}", me);
        } catch (Exception e) {
            Log.logmqtt.error("Error suscribing topic: {}", e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.logmqtt.info("{}: {}", topic, message.toString());
        Topics newTopic = new Topics();
        newTopic.setValue(message.toString());
        if (topic.startsWith("cubiculo")){ // Lectura de un cubiculo 
            String[] componentes = topic.split("/"); // divide el topic en un array, por ejemplo divide a cubiculo/1/luz en ["cubiculo","1","luz"]
            if (componentes[1].equals("1")){ // esto solo es un ejemplo, hay que mirar en la bbdd si este ID de cubiculo existe, se debería hacer un checkeo anterior en la bbdd para ver si existe
                switch(componentes[2]){
                    case "temp":
                        //asdasd
                        break;
                    case "hum":
                        //asdasd
                        break;
                    case "luz":
                        // aqui en realidad debe haber un insert a la bbdd de este valor, en la tabla correspondiente
                        Log.logmqtt.info("El cubiculo " + componentes[1] + " ha leido en el sensor de luz el valor " + message.toString());
                        break;
                    case "sonido":
                        //asdasd
                        break;
                    case "ocupado":
                        //asdasd
                        break;
                    case "distancia":
                        //asdasd
                        break;
                    case "pulsaciones":
                        //asdasd
                        break;
                    default:
                        //Error, el topic al que ha enviado no es valido
                }
            }
        } else if (topic.startsWith("sala")){ // Lectura de un cubiculo 
            String[] componentes = topic.split("/"); // divide el topic en un array, por ejemplo divide a sala/1/temp en ["sala","1","temp"]
            if (componentes[1].equals("1")){ // esto solo es un ejemplo, hay que mirar en la bbdd si este ID de sala existe, se debería hacer un checkeo anterior en la bbdd para ver si existe
                switch(componentes[2]){
                    case "temp":
                        // aqui en realidad debe haber un insert a la bbdd de este valor, en la tabla correspondiente
                        Log.logmqtt.info("La sala " + componentes[1] + " ha leido en el sensor de temperatura el valor " + message.toString());
                        break;
                    case "hum":
                        //asdasd
                        break;
                    default:
                        //Error, el topic al que ha enviado no es valido
                }
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }
}
