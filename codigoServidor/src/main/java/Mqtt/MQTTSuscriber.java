package mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.eclipse.paho.client.mqttv3.*;
import java.sql.*;
import db.Topics;
import logic.Log;

import org.eclipse.paho.client.mqttv3.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;

public class MQTTSuscriber implements MqttCallback {

    // Logger configurado específicamente para MQTT, tal como está en tu log4j2.xml
    private static final Logger logger = LogManager.getLogger("logmqtt"); // Log para MQTT

    private static Connection connection;

    public static void main(String[] args) {
        try {
            // Establecer conexión con la base de datos
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "usuario", "contraseña");
            logger.info("Conexión con la base de datos establecida.");

            // Iniciar el suscriptor MQTT
            String broker = "tcp://172.20.10.2:1883";
            String clientId = MqttClient.generateClientId();
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("ubicua");
            options.setPassword("ubicua".toCharArray());
            client.setCallback(new MQTTSuscriber());

            // Conectar al broker
            client.connect(options);
            logger.info("Conexión al broker MQTT establecida.");

            // Suscribirse al tópico de sensores
            client.subscribe("station1/#");
            logger.info("Suscrito al tópico: station1/#");

        } catch (MqttException | SQLException e) {
            logger.error("Error en la conexión o en la suscripción: ", e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        logger.warn("Conexión perdida. Intentando reconectar...", cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Mostrar mensaje recibido
        logger.info("Tópico recibido: {} - Mensaje: {}", topic, message.toString());

        // Parsear el mensaje recibido
        String sensorData = message.toString();

        // Insertar en la base de datos
        storeDataInDatabase(topic, sensorData);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // No es necesario en este caso
    }

    private void storeDataInDatabase(String topic, String data) {
        try {
            // Preparar la sentencia SQL para insertar los datos
            String sql = "INSERT INTO LecturaSensores (idSensor, valor, fechaHora) VALUES (?, ?, NOW())";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Extraer idSensor del tópico (asumimos que el tópico es algo como "station1/light")
            String[] parts = topic.split("/");
            int sensorId = getSensorId(parts[1]); // Ejemplo: "light" -> 1

            preparedStatement.setInt(1, sensorId);
            preparedStatement.setInt(2, Integer.parseInt(data));

            // Ejecutar la consulta
            preparedStatement.executeUpdate();
            logger.info("Datos insertados en la base de datos con éxito.");
        } catch (SQLException e) {
            logger.error("Error al insertar datos en la base de datos: ", e);
        }
    }

    // Método para mapear el nombre del sensor a su id
    private int getSensorId(String sensorName) {
        switch (sensorName) {
            case "hum": return 1;
            case "temp": return 2;
            case "ruido": return 3;
            case "luz": return 4;
            case "distancia": return 5;
            // agregar más sensores si es necesario
            default: return 0;
        }
    }
}
