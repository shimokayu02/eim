package sample.model.entity;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sample.autoconfiguration.dialect.CApplyJsonbType;
import sample.model.Define;
import sample.model.constraints.Kana;
import sample.model.type.SexType;
import sample.model.type.employee.AuthorityTypeConverter;
import sample.model.type.employee.AuthorityTypeConverter.AuthorityType;
import sample.model.type.employee.StatusTypeConverter;
import sample.model.type.employee.StatusTypeConverter.EmployeeStatusType;
import sample.repository.ActiveRecord;

/**
 * 従業員を表現します。
 */
@Entity
@Table(name = "employee")
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findByEmployeeId", query = "SELECT e FROM Employee e WHERE e.employeeId = :employeeId"),
    @NamedQuery(name = "Employee.findByMail", query = "SELECT e FROM Employee e WHERE e.mail = :mail"),
    @NamedQuery(name = "Employee.findByEmployeeIdNotAndMail", query = "SELECT e FROM Employee e WHERE e.employeeId != :employeeId AND e.mail = :mail")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "CApplyJsonbType", typeClass = CApplyJsonbType.class)
public class Employee implements ActiveRecord {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** 従業員ID */
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "employee_id")
    private String employeeId;
    /** メールアドレス */
    @Email
    @Size(max = 255)
    private String mail;
    /** パスワード (暗号化済) */
    @Size(max = 255)
    private String password;
    /** ログイン失敗回数 */
    @NotNull
    @Column(name = "login_failure_count")
    private int loginFailureCount;
    /** 姓 */
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "last_name")
    private String lastName;
    /** 名 */
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "first_name")
    private String firstName;
    /** 姓 (カナ) */
    @Kana
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "last_name_kana")
    private String lastNameKana;
    /** 名 (カナ) */
    @Kana
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "first_name_kana")
    private String firstNameKana;
    /** 生年月日 */
    @Size(max = 8)
    private String birthday;
    /** 出身地 */
    @Size(max = 30)
    private String birthplace;
    /** 性別 */
    @Enumerated(EnumType.ORDINAL)
    private SexType sex;
    /** 入社日 */
    @Size(max = 8)
    @Column(name = "hire_date")
    private String hireDate;
    /** 所属部署 */
    @Type(type = "CApplyJsonbType")
    @Column(name = "main_organization")
    private Map<String, Object> mainOrganization;
    /** 役職 */
    @NotNull
    private int position;
    /** 権限 */
    @Convert(converter = AuthorityTypeConverter.class)
    @NotNull
    @Column(name = "authority_type")
    private AuthorityType authorityType;
    /** 従業員状態 */
    @Convert(converter = StatusTypeConverter.class)
    @NotNull
    @Column(name = "status_type")
    private EmployeeStatusType statusType;
    /** オペレータ */
    @Getter(AccessLevel.NONE)
    @Size(max = 255)
    private String operator;
    /** 作成日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Date createdDate;
    /** 最終更新日時 */
    @Getter(AccessLevel.NONE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    public String getDepartmentCode() {
        return (String) getMainOrganization().get(Define.MainOrganization.KEY_DEPARTMENT_CODE);
    }

    public String getSectionCode() {
        return (String) getMainOrganization().get(Define.MainOrganization.KEY_SECTION_CODE);
    }

    public String getGroupCode() {
        return (String) getMainOrganization().get(Define.MainOrganization.KEY_GROUP_CODE);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.model.entity.Employee[ id=" + id + " ]";
    }

}
