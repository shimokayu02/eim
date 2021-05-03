package sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SkipException extends Exception {

    private final Logger logger = LogManager.getLogger(this.getClass());

    public SkipException(String message) {
        super(message);
        logger.warn(message);
    }
}
