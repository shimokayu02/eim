package sample.model.form.employee;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sample.model.constraints.Kana;
import sample.model.type.SexType;
import sample.model.type.employee.AuthorityTypeConverter.AuthorityType;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ChgEmployeeForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String employeeId;
    @Email
    @Size(max = 255)
    private String mail;
    @NotNull
    @Size(min = 1, max = 30)
    private String lastName;
    @NotNull
    @Size(min = 1, max = 30)
    private String firstName;
    @Kana
    @NotNull
    @Size(min = 1, max = 80)
    private String lastNameKana;
    @Kana
    @NotNull
    @Size(min = 1, max = 80)
    private String firstNameKana;
    private String birthday;
    private String birthplace;
    private SexType sex;
    private String hireDate;
    private String departmentCode;
    private String sectionCode;
    private String groupCode;
    private Integer positionValue;
    private AuthorityType authorityType;

}
