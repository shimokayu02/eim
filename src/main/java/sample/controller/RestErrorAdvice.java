package sample.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import sample.DuplicateException;
import sample.model.Define;

/**
 * 例外処理 の AOP アドバイスを差し込む Interceptor。
 */
@ControllerAdvice(annotations = RestController.class)
public class RestErrorAdvice extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSourceImpl messageSource;

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LogManager.getLogger(RestErrorAdvice.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return result(HttpStatus.INTERNAL_SERVER_ERROR, messageSource.getMessage(Define.Exception));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException enfe) {
        logger.error(enfe.getMessage(), enfe);
        return result(HttpStatus.BAD_REQUEST, messageSource.getMessage(Define.EntityNotFound));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException cve) {
        List<String> warnList = new ArrayList<>();
        Map<String, String> warnsMap = new HashMap<>();
        cve.getConstraintViolations().stream().forEach(x -> {
            warnsMap.put(String.valueOf(x.getPropertyPath()), x.getMessage());
        });
        try {
            warnList.add(objectMapper.writeValueAsString(warnsMap));
        } catch (JsonProcessingException jpe) {
            logger.error(jpe);
        }
        return new ResponseEntity<>(warnList.stream().collect(Collectors.joining(", ")), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<Object> handleDuplicateException(DuplicateException de) {
        return new ResponseEntity<>(de.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ade) {
        logger.error(ade.getMessage(), ade);
        return result(HttpStatus.UNAUTHORIZED, messageSource.getMessage(Define.AccessDenied));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Object> handleIOException(IOException ioe) {
        return handleException(ioe);
    }

    /** {@inheritDoc} */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        return super.handleMissingServletRequestParameter(ex, headers, status, request);
    }

    @Component
    public static class MessageSourceImpl {

        @Autowired
        public MessageSource messageSource() {
            ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();
            rbms.addBasenames("messages");
            rbms.setDefaultEncoding("UTF-8");
            return rbms;
        }

        /**
         *
         * @param code
         * @return
         */
        public String getMessage(String code) {
            return messageSource().getMessage(code, new Object[0], LocaleContextHolder.getLocale());
        }
    }

    public static class ErrorHolder {

        @JsonProperty("error_code")
        private int errorCode;

        @JsonProperty("message")
        private String messageSource;

        public ErrorHolder(int errorCode, String messageSource) {
            this.errorCode = errorCode;
            this.messageSource = messageSource;
        }
    }

    private ResponseEntity<Object> result(HttpStatus status, String message) {
        return new ResponseEntity<>(new ErrorHolder(status.value(), message), status);
    }

}
