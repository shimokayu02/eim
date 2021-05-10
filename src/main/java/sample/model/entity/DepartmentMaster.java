package sample.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sample.repository.ActiveRecord;

/**
 * 部署マスターを表現します。
 */
@Entity
@Table(name = "department_master")
@NamedQueries({
    @NamedQuery(name = "DepartmentMaster.findAll", query = "SELECT d FROM DepartmentMaster d"),
    @NamedQuery(name = "DepartmentMaster.findByDepartmentCodeAndDepartmentAuthority", query = "SELECT d FROM DepartmentMaster d WHERE d.departmentCode = :departmentCode and d.departmentAuthority = :departmentAuthority"),
    @NamedQuery(name = "DepartmentMaster.findByDepartmentCodeAndSectionCodeAndGroupCode", query = "SELECT d FROM DepartmentMaster d WHERE d.departmentCode = :departmentCode and d.sectionCode = :sectionCode and d.groupCode = :groupCode"),
    @NamedQuery(name = "DepartmentMaster.findByDepartmentCodeAndSectionCodeAndGroupCodeIsNull", query = "SELECT d FROM DepartmentMaster d WHERE d.departmentCode = :departmentCode and d.sectionCode = :sectionCode and d.groupCode IS NULL"),
    @NamedQuery(name = "DepartmentMaster.findByDepartmentCodeAndSectionCodeIsNullAndGroupCodeIsNull", query = "SELECT d FROM DepartmentMaster d WHERE d.departmentCode = :departmentCode and d.sectionCode IS NULL and d.groupCode IS NULL")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentMaster implements ActiveRecord {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 部署コード */
    @Size(max = 2)
    @Column(name = "department_code")
    private String departmentCode;
    /** 部署名 */
    @Size(max = 30)
    @Column(name = "department_name")
    private String departmentName;
    /** 部署権限 */
    @Size(max = 30)
    @Column(name = "department_authority")
    private String departmentAuthority;
    /** 課コード */
    @Size(max = 2)
    @Column(name = "section_code")
    private String sectionCode;
    /** 課名 */
    @Size(max = 30)
    @Column(name = "section_name")
    private String sectionName;
    /** 班コード */
    @Size(max = 2)
    @Column(name = "group_code")
    private String groupCode;
    /** 班名 */
    @Size(max = 30)
    @Column(name = "group_name")
    private String groupName;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DepartmentMaster)) {
            return false;
        }
        DepartmentMaster other = (DepartmentMaster) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sample.model.entity.DepartmentMaster[ id=" + id + " ]";
    }

}
