package sample.usecase;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import sample.DuplicateException;
import sample.model.dto.EmployeeDTO;
import sample.model.entity.DepartmentMaster;
import sample.model.entity.Employee;
import sample.model.form.employee.ChgEmployeeForm;
import sample.model.form.employee.ChgStatusTypeForm;
import sample.model.form.employee.RegEmployeeForm;
import sample.model.type.employee.StatusTypeConverter.EmployeeStatusType;
import sample.repository.DepartmentMasterRepository;
import sample.repository.EmployeeRepository;

/**
 * Employeeドメインに関わる Service。
 */
@Service
public class EmployeeService {
    private final DepartmentMasterRepository departmentMasterRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder encoder;

    /** コンストラクタインジェクション */
    public EmployeeService(
            DepartmentMasterRepository departmentMasterRepository,
            EmployeeRepository employeeRepository) {
        this.departmentMasterRepository = departmentMasterRepository;
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<Employee> readAll() {
        return employeeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Employee readByEmployeeId(String employeeId) {
        return employeeRepository.findByEmployeeId(employeeId).orElseThrow(() -> new EntityNotFoundException());
    }

    @Transactional(readOnly = true)
    public List<Employee> readByEmployeeIdOrMainOrganizationAndPositionLessThan(Employee entity) {
        return employeeRepository.findByEmployeeIdOrMainOrganizationAndPositionLessThan(entity.getEmployeeId(),
                entity.getDepartmentCode(), entity.getSectionCode(), entity.getGroupCode(), entity.getPosition());
    }

    @Transactional
    public void create(RegEmployeeForm form) {
        Employee entity = new Employee();
        String employeeId = getEmployeeId(employeeRepository);
        Optional<DepartmentMaster> departmentMasterOpt = departmentMasterRepository
                .findByDepartmentCodeAndDepartmentAuthority(form.getDepartmentCode(), "Company");
        boolean isCompany = departmentMasterOpt.isPresent();
        EmployeeDTO dto = EmployeeDTO.valueOf(form, employeeId, encoder, isCompany);
        entity.setEmployeeId(dto.getEmployeeId());
        entity.setMail(chkMail(entity, dto.getMail()));
        entity.setPassword(dto.getPassword());
        entity.setLoginFailureCount(0);
        entity.setLastName(dto.getLastName());
        entity.setFirstName(dto.getFirstName());
        entity.setLastNameKana(dto.getLastNameKana());
        entity.setFirstNameKana(dto.getFirstNameKana());
        entity.setBirthday(dto.getBirthday());
        entity.setBirthplace(dto.getBirthplace());
        entity.setSex(dto.getSex());
        entity.setHireDate(dto.getHireDate());
        entity.setMainOrganization(dto.getMainOrganization());
        entity.setPosition(dto.getPosition());
        entity.setAuthorityType(dto.getAuthorityType());
        entity.setStatusType(EmployeeStatusType.XXX);
        employeeRepository.save(entity);
    }

    @Transactional
    public void update(Employee entity, ChgEmployeeForm form) {
        Optional<DepartmentMaster> departmentMasterOpt = departmentMasterRepository
                .findByDepartmentCodeAndDepartmentAuthority(form.getDepartmentCode(), "Company");
        boolean isCompany = departmentMasterOpt.isPresent();
        EmployeeDTO dto = EmployeeDTO.valueOf(form, encoder, isCompany);
        entity.setMail(chkMail(entity, dto.getMail()));
        entity.setPassword(Objects.nonNull(entity.getPassword()) ? entity.getPassword() : dto.getPassword());
        entity.setLastName(dto.getLastName());
        entity.setFirstName(dto.getFirstName());
        entity.setLastNameKana(dto.getLastNameKana());
        entity.setFirstNameKana(dto.getFirstNameKana());
        entity.setBirthday(dto.getBirthday());
        entity.setBirthplace(dto.getBirthplace());
        entity.setSex(dto.getSex());
        entity.setHireDate(dto.getHireDate());
        entity.setMainOrganization(dto.getMainOrganization());
        entity.setPosition(dto.getPosition());
        entity.setAuthorityType(dto.getAuthorityType());
        employeeRepository.save(entity);
    }

    @Transactional
    public void activate(Employee entity, ChgStatusTypeForm form) {
        EmployeeDTO dto = EmployeeDTO.valueOf(form);
        entity.setStatusType(dto.getStatusType());
        if (dto.getStatusType().isActive() && entity.getLoginFailureCount() != 0) {
            entity.setLoginFailureCount(0);
        }
        employeeRepository.save(entity);
    }

    /**
     * 一意な従業員IDを生成します。
     *
     * @param employeeRepository
     * @return
     */
    private static String getEmployeeId(EmployeeRepository employeeRepository) {
        String employeeId;
        do {
            employeeId = RandomStringUtils.randomAlphanumeric(6);
        } while (employeeRepository.findByEmployeeId(employeeId).isPresent());
        return employeeId;
    }

    /**
     *
     * @param entity (必須)
     * @param mail
     * @return
     */
    private String chkMail(Employee entity, String mail) {

        Assert.notNull(entity.getEmployeeId(), "Hey developer! The entity's employeeId must not be null");

        if (StringUtils.isEmpty(mail)) {
            return null;
        }

        Optional<Employee> entityOpt = employeeRepository.findByEmployeeIdNotAndMail(entity.getEmployeeId(), mail);
        if (entityOpt.isPresent()) {
            String message = String.format("%s は既に登録されているメールアドレスです。", mail);
            throw new DuplicateException("mail", message);
        }
        return mail;
    }

}
