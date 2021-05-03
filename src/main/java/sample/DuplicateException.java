package sample;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DuplicateException extends RuntimeException {

    private final Logger logger = LogManager.getLogger(this.getClass());

    public DuplicateException(String field, String message) {
        super(retMessage(field, message));
        logger.warn("Duplicate: {}", message); // e.g. Duplicate: a@a は既に登録されているメールアドレスです。
    }

    private static String retMessage(String field, String message) {
        return String.format("{\"%s\":\"%s\"}", field, message); // e.g. {"mail":"a@a は既に登録されているメールアドレスです。"}
    }

}
