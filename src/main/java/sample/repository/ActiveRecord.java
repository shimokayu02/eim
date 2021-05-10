package sample.repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import sample.context.security.OpUserDetails;
import sample.util.KanaToLatin;

public interface ActiveRecord extends ActiveMetaRecord {

    void setOperator(String operator);

    void setLastModifiedDate(Date lastModifiedDate);

    default String getOperator() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            return "system";
        }
        String operator = null;
        if (authentication.getPrincipal() instanceof OpUserDetails) {
            OpUserDetails userDetails = OpUserDetails.class.cast(authentication.getPrincipal());
            String latinFullName = getLatinFullName(userDetails);
            operator = latinFullName.substring(0, 1).toUpperCase() + latinFullName.replaceFirst("^.", "") + "｜"
                    + Stream.of(userDetails.getEmployee().getDepartmentCode(),
                            userDetails.getEmployee().getSectionCode(), userDetails.getEmployee().getGroupCode())
                            .filter(Objects::nonNull)
                            .collect(Collectors.joining())
                    + "［" + userDetails.getEmployee().getAuthorityType() + "］";
        }
        return operator; // Hermes, taro｜04［Admin］
    }

    private String getLatinFullName(OpUserDetails userDetails) {
        return KanaToLatin.convert(userDetails.getEmployee().getLastNameKana()) + ", "
                + KanaToLatin.convert(userDetails.getEmployee().getFirstNameKana());
    }

    default Date getLastModifiedDate() {
        return new Timestamp(System.currentTimeMillis());
    }

}
