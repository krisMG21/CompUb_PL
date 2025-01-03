package Logic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {

    public static Logger log = LogManager.getLogger("log");
    public static Logger logdb = LogManager.getLogger("logdb");
    public static Logger logmqtt = LogManager.getLogger("logmqtt");
}
