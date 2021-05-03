package sample.model.type;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ValidationException;

/**
 * 役職レベル定数。
 */
public enum PositionLevelType {
    /** 部長 */
    DEPARTMENT_MANAGER(40, "部長"),
    /** 課長 */
    SECTION_MANAGER(30, "課長"),
    /** 主任 */
    GROUP_MANAGER(20, "主任"),
    /** その他 */
    OTHER(10, "");

    private final int value;

    private final String viewName;

    PositionLevelType(int value, String viewName) {
        this.value = value;
        this.viewName = viewName;
    }

    public int getValue() {
        return value;
    }

    public String getViewName() {
        return viewName;
    }

    public static Map<Integer, String> positionMap() {
        return Arrays.stream(PositionLevelType.values())
                .collect(Collectors.toMap(PositionLevelType::getValue, PositionLevelType::getViewName));
    }

    public static int get(Integer positionValue) {
        for (PositionLevelType positionLevel : values()) {
            if (positionLevel.getValue() == positionValue) {
                return positionLevel.getValue();
            }
        }
        throw new ValidationException(String.format("%d is an unexpected value of position at program.", positionValue));
    }

}