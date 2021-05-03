package sample.model.type.employee;

import javax.persistence.AttributeConverter;

import sample.model.type.employee.AuthorityTypeConverter.AuthorityType;

public class AuthorityTypeConverter implements AttributeConverter<AuthorityType, String> {

    /**
     * Enum of authority_type. (権限種別)
     */
    public static enum AuthorityType {
        /** 上長 */
        Admin("ADMIN", "EMPLOYEE"),
        /** 総務部・人事教育部・システム営業部 */
        Company("COMPANY", "EMPLOYEE"),
        /** 従業員 */
        Employee("EMPLOYEE");

        private final String[] authorities;

        private AuthorityType(String... authorities) {
            this.authorities = authorities;
        }

        public String[] authorities() {
            return this.authorities;
        }
    }

    @Override
    public String convertToDatabaseColumn(AuthorityType attribute) {
        return attribute.toString();
    }

    @Override
    public AuthorityType convertToEntityAttribute(String dbData) {
        return AuthorityType.valueOf(dbData);
    }

}