package sample.model.type.employee;

import javax.persistence.AttributeConverter;

import sample.model.type.employee.StatusTypeConverter.EmployeeStatusType;

public class StatusTypeConverter implements AttributeConverter<EmployeeStatusType, String> {

    /**
     * Enum of employee status_type. (従業員状態種別)
     */
    public static enum EmployeeStatusType {

        Active, Inactive, AccountLock, XXX, YYY;

        public boolean isActive() {
            return this == Active;
        }

        public boolean isInactive() {
            return this == Inactive;
        }

        public boolean isAccountLock() {
            return this == AccountLock;
        }

        public boolean isXXX() {
            return this == XXX;
        }

        public static EmployeeStatusType get(String statusType) {
            for (EmployeeStatusType value : values()) {
                if (value == EmployeeStatusType.valueOf(statusType)) {
                    return value;
                }
            }
            return YYY;
        }
    }

    @Override
    public String convertToDatabaseColumn(EmployeeStatusType attribute) {
        return attribute.toString();
    }

    @Override
    public EmployeeStatusType convertToEntityAttribute(String dbData) {
        return EmployeeStatusType.valueOf(dbData);
    }

}