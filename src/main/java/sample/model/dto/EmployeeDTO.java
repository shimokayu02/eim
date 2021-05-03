package sample.model.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sample.model.Define.MainOrganization;
import sample.model.form.employee.ChgEmployeeForm;
import sample.model.form.employee.ChgStatusTypeForm;
import sample.model.form.employee.RegEmployeeForm;
import sample.model.type.PositionLevelType;
import sample.model.type.SexType;
import sample.model.type.employee.AuthorityTypeConverter.AuthorityType;
import sample.model.type.employee.StatusTypeConverter.EmployeeStatusType;
import sample.util.DateUtils;

@Getter
@Setter
@ToString(callSuper = false, exclude = "password")
@EqualsAndHashCode(callSuper = false)
public class EmployeeDTO extends DtoAbstract {

    private static final long serialVersionUID = 1L;

    private String employeeId;
    private String mail;
    private String password;
    private String lastName;
    private String firstName;
    private String lastNameKana;
    private String firstNameKana;
    private String birthday;
    private String birthplace;
    private SexType sex;
    private String hireDate;
    private Map<String, Object> mainOrganization;
    private int position;
    private AuthorityType authorityType;
    private EmployeeStatusType statusType;

    public static EmployeeDTO valueOf(RegEmployeeForm form, String employeeId, PasswordEncoder encoder, boolean isCompany) {
        EmployeeDTO dto = new EmployeeDTO();
        form = (RegEmployeeForm) requestForm(form);
        dto.setEmployeeId(employeeId);
        dto.setMail(form.getMail());
        dto.setPassword(getPassword(encoder, form.getMail(), form.getPositionValue(), isCompany));
        dto.setLastName(form.getLastName());
        dto.setFirstName(form.getFirstName());
        dto.setLastNameKana(form.getLastNameKana());
        dto.setFirstNameKana(form.getFirstNameKana());
        dto.setBirthday(DateUtils.convert(form.getBirthday()));
        dto.setBirthplace(form.getBirthplace());
        dto.setSex(form.getSex());
        dto.setHireDate(DateUtils.convert(form.getHireDate()));
        dto.setMainOrganization(getMainOrganizationMap(form.getDepartmentCode(), form.getSectionCode(), form.getGroupCode()));
        dto.setPosition(getPosition(form.getPositionValue()));
        dto.setAuthorityType(getAuthorityType(isCompany, form.getPositionValue()));
        return dto;
    }

    public static EmployeeDTO valueOf(ChgEmployeeForm form, PasswordEncoder encoder, boolean isCompany) {
        EmployeeDTO dto = new EmployeeDTO();
        form = (ChgEmployeeForm) requestForm(form);
        dto.setMail(form.getMail());
        dto.setPassword(getPassword(encoder, form.getMail(), form.getPositionValue(), isCompany));
        dto.setLastName(form.getLastName());
        dto.setFirstName(form.getFirstName());
        dto.setLastNameKana(form.getLastNameKana());
        dto.setFirstNameKana(form.getFirstNameKana());
        dto.setBirthday(DateUtils.convert(form.getBirthday()));
        dto.setBirthplace(form.getBirthplace());
        dto.setSex(form.getSex());
        dto.setHireDate(DateUtils.convert(form.getHireDate()));
        dto.setMainOrganization(getMainOrganizationMap(form.getDepartmentCode(), form.getSectionCode(), form.getGroupCode()));
        dto.setPosition(getPosition(form.getPositionValue()));
        dto.setAuthorityType(form.getAuthorityType());
        return dto;
    }

    public static EmployeeDTO valueOf(ChgStatusTypeForm form) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setStatusType(EmployeeStatusType.get(form.getStatusType()));
        return dto;
    }

    /**
     * 仮パスワードを自動生成します。
     *
     * @param encoder
     * @param mail
     * @param positionValue
     * @param isCompany
     * @return
     */
    private static String getPassword(PasswordEncoder encoder, String mail, Integer positionValue, boolean isCompany) {

        if (Objects.nonNull(mail) && (isAdmin(positionValue) || isCompany)) {
            return encoder.encode(RandomStringUtils.randomAlphanumeric(8));
        }
        return null;
    }

    /**
     *
     * @param departmentCode
     * @param sectionCode
     * @param groupCode
     * @return
     */
    private static Map<String, Object> getMainOrganizationMap(String departmentCode, String sectionCode, String groupCode) {
        Map<String, Object> mainOrganizationMap = new HashMap<>();
        mainOrganizationMap.put(MainOrganization.KEY_DEPARTMENT_CODE, departmentCode);
        mainOrganizationMap.put(MainOrganization.KEY_SECTION_CODE, sectionCode);
        mainOrganizationMap.put(MainOrganization.KEY_GROUP_CODE, groupCode);
        return mainOrganizationMap;
    }

    /**
     *
     * @param positionValue
     * @return
     */
    private static int getPosition(Integer positionValue) {
        Optional<Integer> positionValueOpt = Optional.ofNullable(positionValue);
        return positionValueOpt.map(x -> PositionLevelType.get(x)).orElse(PositionLevelType.OTHER.getValue());
    }

    /**
     * <p>
     * 役職 (position) および所属部署 (main_organaization) をもとに権限 (authority_type) を暫定します。</p>
     *
     * <dl>
     * <dt>Admin権限</dt>
     * <dd>役職が主任以上</dd>
     * <dt>Company権限<dt>
     * <dd>所属部署が総務部 or 人事教育部 or システム営業部</dd>
     * <dt>Employee権限</dt>
     * <dd>その他</dd>
     * </dl>
     *
     * @param isCompany
     * @param positionValue
     * @return
     */
    private static AuthorityType getAuthorityType(boolean isCompany, Integer positionValue) {

        if (isCompany) {
            return AuthorityType.Company; // 早期 return
        }
        return isAdmin(positionValue) ? AuthorityType.Admin : AuthorityType.Employee;
    }

    private static boolean isAdmin(Integer positionValue) {
        return positionValue != null && positionValue >= PositionLevelType.GROUP_MANAGER.getValue();
    }

}
