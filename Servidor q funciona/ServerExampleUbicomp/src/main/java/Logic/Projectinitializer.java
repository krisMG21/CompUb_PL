package logic;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import mqtt.MQTTBroker;
import mqtt.MQTTPublisher;
import mqtt.MQTTSuscriber;

/**
 * ES: Clase encargada de inicializar el sistema y de lanzar el hilo de
 * previsión meteorológica EN: Class in charge of initializing the thread of
 * weather forecast
 */
@WebListener
public class Projectinitializer implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    @Override
    /**
     * ES: Metodo empleado para detectar la inicializacion del servidor	<br>
     * EN: Method used to detect server initialization
     *
     * @param sce <br>
     * ES: Evento de contexto creado durante el arranque del servidor	<br>
     * EN: Context event created during server launch
     */
    public void contextInitialized(ServletContextEvent sce) {
        Log.log.info("-->Suscribe Topics<--");
        MQTTBroker broker = new MQTTBroker();
        MQTTSuscriber suscriber = new MQTTSuscriber();
        suscriber.suscribeTopic(broker, "test");
        MQTTPublisher.publish(broker, "test", "Hello from Tomcat :)");
    }
}
