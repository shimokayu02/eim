package sample.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Validator {

    private final static Logger LOG = LogManager.getLogger(Validator.class);

    /**
     *
     * @param bean
     * @return
     */
    public static Object checkFields(Object bean) {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        javax.validation.Validator validator = vf.getValidator();
        Set<ConstraintViolation<Object>> result = validator.validate(bean);
        if (!result.isEmpty()) {
            LOG.warn(String.format("Validation: %s, (detail) %s", bean.getClass().getName(), result));
            throw new ConstraintViolationException(result);
        }
        return bean;
    }

}
