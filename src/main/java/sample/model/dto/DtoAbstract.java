package sample.model.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import sample.util.Validator;

public abstract class DtoAbstract implements Serializable {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    /** String型 で受け取った Form の empty ("") ないし "null" を null に整形します。 */
    protected static Object requestForm(Object reqForm) {
        Map<String, Object> sourceMap = objectMapper.convertValue(reqForm, new TypeReference<Map<String, Object>>() {});
        Map<String, Object> destinationMap = new HashMap<>();
        sourceMap.entrySet().stream().forEach(x -> {
            destinationMap.put(x.getKey(), convert(x.getValue()));
        });
        reqForm = objectMapper.convertValue(destinationMap, reqForm.getClass());
        return Validator.checkFields(reqForm);
    }

    private static Object convert(Object value) {
        if (value instanceof String && (value.equals("") || value.equals("null"))) {
            return null;
        }
        return value;
    }

}
