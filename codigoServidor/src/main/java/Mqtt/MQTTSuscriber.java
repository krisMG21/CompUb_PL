package mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class MQTTSuscriber implements MqttCallback {

    // Logger configurado específicamente para MQTT, tal como está en tu log4j2.xml
    private static final Logger logger = LogManager.getLogger("logmqtt"); // Log para MQTT
    private static Connection connection;
    private static MqttClient client;

    public static void main(String[] args) {
        try {
            // Establecer conexión con la base de datos
            connection = DriverManager.getConnection("jdbc:mysql://192.168.1.50:3306/biblioteca", "ubicua", "ubicua");
            logger.info("Conexión con la base de datos establecida.");

            // Iniciar el suscriptor MQTT
            MQTTBroker broker = new MQTTBroker();
            String clientId = MqttClient.generateClientId();
            client = new MqttClient(broker.getBroker(), clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(broker.getUsername());
            options.setPassword(broker.getPassword().toCharArray());
            client.setCallback(new MQTTSuscriber());

            // Conectar al broker
            client.connect(options);
            logger.info("Conexión al broker MQTT establecida.");

            // Suscribirse al tópico de sensores
            client.subscribe("#");
            logger.info("Suscrito al todos los tópicos");

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
        String[] parts = topic.split("/");
        String topic_espacio = parts[0]; // e.g., "cubiculo" or "sala"
        String topic_ID = parts[1];       // e.g., "1" (the ID)
        String topic_comp = parts[2];      // e.g., "ocupado" or "light"

        if ("ocupado".equals(topic_comp)) {
            insertDataOcupacion(topic_espacio, topic_ID, data);
        } else {
            insertLectura(topic_espacio, topic_ID, topic_comp, data);
        }
        
    }
    
    private void insertDataOcupacion(String topic_espacio, String topic_ID, String data){
        String tabla = "cubiculo".equals(topic_espacio) ? "Cubiculos" : "Salas";
        String sql = "INSERT INTO " + tabla + " (idSala, ocupado) VALUES (?, ?)";
        
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1, Integer.parseInt(topic_ID));
            preparedStatement.setInt(2, Integer.parseInt(data));

            // Ejecutar la consulta
            preparedStatement.executeUpdate();
            logger.info("Ocupación de {} {} insertado en la base de datos con éxito",
                    topic_espacio,
                    topic_ID);
        } catch (SQLException e){
            logger.error("Error al insertar ocupaciones en la base de datos");
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
            logger.info("Lectura de {} {} insertada en la base de datos con éxito.",
                    topic_espacio,
                    topic_ID);
        } catch (SQLException e) {
            logger.error("Error al insertar lectura en la base de datos: ", e);
        }
    }

    // Método para mapear el nombre del sensor a su id
    private int getSensorId(String sensorName) {
        switch (sensorName) {
            case "hum": return 1;
            case "temp": return 2;
            case "ruido": return 3;
            case "luz": return 4;
            case "ocupado": return 5;
            // agregar más sensores si es necesario
            default: return 0;
        }
    }

    public void suscribeTopic(MQTTBroker broker, String topic) {
        try {
            // Subscribe to the specified topic using the MQTT client
            client.subscribe(topic);
            logger.info("Suscrito al tópico: {}", topic);
        } catch (MqttException e) {
            logger.error("Error al suscribirse al tópico {}: ", topic, e);
    }
}
}
