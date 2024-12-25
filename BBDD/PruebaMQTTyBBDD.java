import org.eclipse.paho.client.mqttv3.*;
import java.sql.*;

public class MQTTClient {

    private static final String BROKER = "tcp://172.20.10.2:1883"; // Dirección de tu servidor MQTT
    private static final String TOPIC = "station1/sensors";  // Tópico al que se publican los datos

    // Credenciales de conexión a la base de datos
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ubicomp"; // Ajusta si es necesario
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "contraseña";

    public static void main(String[] args) {
        try {
            // Conectar al broker MQTT
            MqttClient client = new MqttClient(BROKER, MqttClient.generateClientId());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("ubicua");
            options.setPassword("ubicua".toCharArray());
            client.connect(options);
            System.out.println("Conectado al broker MQTT");

            // Suscribirse al tópico
            client.subscribe(TOPIC, (topic, message) -> {
                String payload = new String(message.getPayload());
                System.out.println("Mensaje recibido: " + payload);

                // Parsear los datos JSON
                String[] sensorData = payload.split(",");
                int lightValue = Integer.parseInt(sensorData[0].split(":")[1].trim());
                float temperature = Float.parseFloat(sensorData[1].split(":")[1].trim());
                float humidity = Float.parseFloat(sensorData[2].split(":")[1].trim());

                // Insertar los datos en la base de datos
                insertDataToDB(lightValue, temperature, humidity);
            });

            System.out.println("Esperando mensajes...");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Método para insertar los datos en la base de datos
    private static void insertDataToDB(int lightValue, float temperature, float humidity) {
        try (Connection con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "INSERT INTO sensor_data (light_value, temperature, humidity) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, lightValue);
                stmt.setFloat(2, temperature);
                stmt.setFloat(3, humidity);
                stmt.executeUpdate();
                System.out.println("Datos insertados correctamente en la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
