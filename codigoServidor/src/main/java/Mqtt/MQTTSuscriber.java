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

public class MQTTSuscriber implements MqttCallback {
    private static Connection connection;
    public static void main(String[] args) {
        try {
            // Establecer conexión con la base de datos
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/biblioteca", "usuario", "contraseña");

            // Iniciar el suscriptor MQTT
            String broker = "tcp://172.20.10.2:1883";
            String clientId = MqttClient.generateClientId();
            MqttClient client = new MqttClient(broker, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("ubicua");
            options.setPassword("ubicua".toCharArray());
            client.setCallback(new MQTTSuscriber());

            client.connect(options);

            // Suscribirse al tópico de sensores
            client.subscribe("station1/#");

        } catch (MqttException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("Conexión perdida. Intentando reconectar...");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // Mostrar mensaje recibido
        System.out.println("Tópico: " + topic + " Mensaje: " + message.toString());

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

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para mapear el nombre del sensor a su id
    private int getSensorId(String sensorName) {
        switch (sensorName) {
            case "light": return 1;
            // Agregar más casos según los sensores que estés utilizando
            default: return 0;
        }
    }
}

