package sample.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Checker {

    private final static Logger LOG = LogManager.getLogger(Checker.class);

    /**
     *
     * @param bean
     * @return
     */
    public static Object chkFields(Object bean) {
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        Validator validator = vf.getValidator();
        Set<ConstraintViolation<Object>> result = validator.validate(bean);
        if (!result.isEmpty()) {
            LOG.warn(String.format("Validation: %s, (detail) %s", bean.getClass().getName(), result));
            throw new ConstraintViolationException(result);
        }
        return bean;
    }

    /**
     *
     * @param yyyyMMdd
     * @return
     */
    public static boolean chkYmd(String yyyyMMdd) {
        if (StringUtils.isEmpty(yyyyMMdd)) {
            return false;
        }
        String rYmd = "^[0-9]{4}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$";
        String tmpYmd = yyyyMMdd.replaceAll("(?<=^.{4})|(?<=^.{6})", "-");
        if (!yyyyMMdd.matches(rYmd) || !tmpYmd.equals(java.sql.Date.valueOf(tmpYmd).toString())) {
            LOG.warn(String.format("input of \"%s\" as yyyyMMdd", yyyyMMdd));
            return false;
        }
        return true;
    }

}
