package sample.model.type;

/**
 * 性別の定数クラス。
 */
public enum SexType {

    MALE((short) 0, "男"), FEMALE((short) 1, "女");

    private final Short value;

    private final String viewName;

    SexType(Short value, String viewName) {
        this.value = value;
        this.viewName = viewName;
    }

    public Short getValue() {
        return value;
    }

    public String getViewName() {
        return viewName;
    }
}