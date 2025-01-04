package Mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import Logic.Log;
import Database.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MQTTSubscriber implements MqttCallback {
    
    private static MqttClient client;
    private ConnectionDB dbConnection;

    public MQTTSubscriber() {
        this.dbConnection = new ConnectionDB();
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
    
    private static void connectClient(MQTTBroker broker) throws MqttException {
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
    public void messageArrived(String topic, MqttMessage message) {
        Log.logmqtt.info("Tópico recibido: {} - Mensaje: {}", topic, message.toString());
        String sensorData = message.toString();
        Connection connection = null;
        try {
            connection = dbConnection.obtainConnection(true);
            storeDataInDatabase(connection, topic, sensorData);
            dbConnection.closeTransaction(connection);
        } catch (Exception e) {
            Log.logmqtt.error("Error processing message: {}", e.getMessage());
            if (connection != null) {
                dbConnection.cancelTransaction(connection);
            }
        } finally {
            if (connection != null) {
                dbConnection.closeConnection(connection);
            }
        }
    }
    
    private void storeDataInDatabase(Connection connection, String topic, String data) throws SQLException {
        String[] parts = topic.split("/");
        if (parts.length != 3) {
            Log.logmqtt.warn("Invalid topic format: {}", topic);
            return;
        }
        String topic_espacio = parts[0];
        String topic_ID = parts[1];
        String topic_comp = parts[2];

        if ("ocupado".equals(topic_comp)) {
            updateDataOcupacion(connection, topic_espacio, topic_ID, data);
        } else {
            insertLectura(connection, topic_espacio, topic_ID, topic_comp, data);
        }
    }
    
    private void updateDataOcupacion(Connection connection, String topic_espacio, String topic_ID, String data) throws SQLException {
        String sql = "cubiculo".equals(topic_espacio) 
            ? "UPDATE Cubiculos SET ocupado = ? WHERE idCubiculo = ?"
            : "UPDATE Salas SET ocupada = ? WHERE idSala = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, Integer.parseInt(data));
            preparedStatement.setInt(2, Integer.parseInt(topic_ID));

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                Log.logmqtt.info("Ocupación de {} {} actualizada en la base de datos con éxito", topic_espacio, topic_ID);
            } else {
                Log.logmqtt.warn("No se encontró el {} con ID {} para actualizar", topic_espacio, topic_ID);
            }
        }
    }

    private void insertLectura(Connection connection, String topic_espacio, String topic_ID, String topic_comp, String data) throws SQLException {
        String sql = "INSERT INTO LecturaSensores (idSensor, valor, idCubiculo, idSala, fechaHora) VALUES (?, ?, ?, ?, NOW())";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int sensorId = getSensorId(topic_comp);
            preparedStatement.setInt(1, sensorId);
            preparedStatement.setInt(2, Integer.parseInt(data));
            if ("cubiculo".equals(topic_espacio)) {
                preparedStatement.setInt(3, Integer.parseInt(topic_ID));
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
            } else {
                preparedStatement.setNull(3, java.sql.Types.INTEGER);
                preparedStatement.setInt(4, Integer.parseInt(topic_ID));
            }
            preparedStatement.executeUpdate();
            Log.logmqtt.info("Lectura de {} {} insertada en la base de datos con éxito.", topic_espacio, topic_ID);
        }
    }

    private int getSensorId(String sensorName) {
        return switch (sensorName) {
            case "hum" -> 1;
            case "temp" -> 2;
            case "ruido" -> 3;
            case "luz" -> 4;
            case "ocupado" -> 5;
            default -> 0;
        };
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // No es necesario implementar para un suscriptor
    }
}
