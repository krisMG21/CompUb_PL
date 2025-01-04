package Mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

//import Database.Topics;
import Logic.Log;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class MQTTSubscriber implements MqttCallback {
    
    private static MqttClient client;
    private static Connection connection;
    
    public MQTTSubscriber(String dbUrl, String dbUser, String dbPassword) {
        try {
            // Establecer la conexión a la base de datos
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            Log.logmqtt.info("Conexión a la base de datos establecida con éxito.");
        } catch (SQLException e) {
            Log.logmqtt.error("Error al establecer la conexión con la base de datos: {}", e.getMessage());
            throw new RuntimeException("No se pudo establecer la conexión con la base de datos", e);
        }
    }

    // Método para cerrar la conexión
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                Log.logmqtt.info("Conexión a la base de datos cerrada con éxito.");
            } catch (SQLException e) {
                Log.logmqtt.error("Error al cerrar la conexión con la base de datos: {}", e.getMessage());
            }
        }
    }

    public void subscribeTopic(MQTTBroker broker, String topic) {
        Log.logmqtt.debug("Suscribe to topics");

        try {
            connectClient(broker);
            Log.logmqtt.debug("Mqtt Connected");
            client.setCallback(this);

            client.subscribe(topic);
            Log.logmqtt.info("Suscribed to {}", topic);

        } catch (MqttException me) {
            Log.logmqtt.error("Error suscribing topic: {}", me);
        } catch (Exception e) {
            Log.logmqtt.error("Error suscribing topic: {}", e);
        }
    }
    
    private static void connectClient(MQTTBroker broker) throws MqttException{
        MemoryPersistence persistence = new MemoryPersistence();
        client = new MqttClient(broker.getBroker(), broker.getClientId(), persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName(broker.getUsername());
        connOpts.setPassword(broker.getPassword().toCharArray());
        connOpts.setCleanSession(true);
        Log.logmqtt.debug("Mqtt Connecting to broker: " + broker.getBroker());
        client.connect(connOpts);
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.logmqtt.warn("Conexión perdida. Intentando reconectar...", cause);
        try {
            MQTTBroker broker = new MQTTBroker();
            connectClient(broker);
        } catch (MqttException me){
            Log.logmqtt.error("Error reconnecting to client: {}", me);
        }
    }
    
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Mostrar mensaje recibido
        Log.logmqtt.info("Tópico recibido: {} - Mensaje: {}", topic, message.toString());

        // Parsear el mensaje recibido
        String sensorData = message.toString();

        // Insertar en la base de datos
        storeDataInDatabase(topic, sensorData);
    }
    
    private void storeDataInDatabase(String topic, String data) {
        String[] parts = topic.split("/");
        String topic_espacio = parts[0]; // e.g., "cubiculo" or "sala"
        String topic_ID = parts[1];       // e.g., "1" (the ID)
        String topic_comp = parts[2];      // e.g., "ocupado" or "light"

        if ("ocupado".equals(topic_comp)) {
            updateDataOcupacion(topic_espacio, topic_ID, data);
        } else {
            insertLectura(topic_espacio, topic_ID, topic_comp, data);
        }
        
    }
    
    private void updateDataOcupacion(String topic_espacio, String topic_ID, String data) {
        String sql = "UPDATE Cubiculos SET ocupado = ? WHERE idCubiculo = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, Integer.parseInt(data));
            preparedStatement.setInt(2, Integer.parseInt(topic_ID));

            // Ejecutar la consulta
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                Log.logmqtt.info("Ocupación de {} {} actualizada en la base de datos con éxito",
                        topic_espacio,
                        topic_ID);
            } else {
                Log.logmqtt.warn("No se encontró el {} con ID {} para actualizar",
                        topic_espacio,
                        topic_ID);
            }
        } catch (SQLException e) {
            Log.logmqtt.error("Error al actualizar ocupaciones en la base de datos: {}", e.getMessage());
        }
    }

    
    private void insertLectura(String topic_espacio, String topic_ID, String topic_comp, String data){
        try {
            // Preparar la sentencia SQL para insertar los datos
            String sql = "INSERT INTO LecturaSensores (idSensor, valor, idCubiculo, idSala, fechaHora) VALUES (?, ?, ?, ?, NOW())";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Extraer idSensor del tópico (asumimos que el tópico es algo como "station1/light")
            int sensorId = getSensorId(topic_comp); // Ejemplo: "light" -> 1

            preparedStatement.setInt(1, sensorId);
            preparedStatement.setInt(2, Integer.parseInt(data));

            if ("cubiculo".equals(topic_espacio)) {
                preparedStatement.setInt(3, Integer.parseInt(topic_ID));
                preparedStatement.setInt(4, 0);
            } else {
                preparedStatement.setInt(3, 0);
                preparedStatement.setInt(4, Integer.parseInt(topic_ID));
            }

            // Ejecutar la consulta
            preparedStatement.executeUpdate();
            Log.logmqtt.info("Lectura de {} {} insertada en la base de datos con éxito.",
                    topic_espacio,
                    topic_ID);
        } catch (SQLException e) {
            Log.logmqtt.error("Error al insertar lectura en la base de datos: ", e);
        }
    }

    // Método para mapear el nombre del sensor a su id
    private int getSensorId(String sensorName) {
        return switch (sensorName) {
            case "hum" -> 1;
            case "temp" -> 2;
            case "ruido" -> 3;
            case "luz" -> 4;
            case "ocupado" -> 5;
            default -> 0;
        }; // agregar más sensores si es necesario
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // No necesario en nuestro caso
    }
}
