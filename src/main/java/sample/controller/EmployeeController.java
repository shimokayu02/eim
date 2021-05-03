package sample.controller;

import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sample.model.entity.DepartmentMaster;
import sample.model.entity.Employee;
import sample.model.form.employee.ChgEmployeeForm;
import sample.model.form.employee.ChgStatusTypeForm;
import sample.model.form.employee.RegEmployeeForm;
import sample.model.type.PositionLevelType;
import sample.model.type.employee.AuthorityTypeConverter.AuthorityType;
import sample.model.type.employee.StatusTypeConverter.EmployeeStatusType;
import sample.usecase.DepartmentMasterService;
import sample.usecase.EmployeeService;
import sample.util.DateUtils;

/**
 * Employeeドメインに関わる Controller。
 */
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    private final DepartmentMasterService departmentMasterService;
    private final EmployeeService employeeService;

    /** コンストラクタインジェクション */
    public EmployeeController(
            DepartmentMasterService departmentMasterService,
            EmployeeService employeeService) {
        this.departmentMasterService = departmentMasterService;
        this.employeeService = employeeService;
    }

    /** 従業員情報を変更します。 */
    @PostMapping("/update")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<String> update(ChgEmployeeForm form) {
        Employee entity = employeeService.readByEmployeeId(form.getEmployeeId());
        employeeService.update(entity, form);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /** 従業員を取得します。 */
    @GetMapping(value = "")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
    public ResponseEntity<Object> getEmployee(@RequestParam("employee_id") String employeeId) {
        Employee entity = employeeService.readByEmployeeId(employeeId);
        // Company権限の場合、全件取得。
        if (Arrays.asList(entity.getAuthorityType().authorities()).contains("COMPANY")) {
            return new ResponseEntity<>(employeeService.readAll()
                    .stream()
                    .map(x -> EmployeeUI.valueOf(x, departmentMasterService))
                    .collect(Collectors.toList()), HttpStatus.OK);
        }
        // Admin権限は自身とその部下のみ取得。
        return new ResponseEntity<>(employeeService.readByEmployeeIdOrMainOrganizationAndPositionLessThan(entity)
                .stream()
                .map(x -> EmployeeUI.valueOf(x, departmentMasterService))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    /** 従業員の登録を行います。 */
    @PostMapping("/save")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<String> save(RegEmployeeForm form) {
        employeeService.create(form);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    /** 従業員状態を有効化/無効化します。 */
    @PostMapping("/activate")
    @PreAuthorize("hasRole('COMPANY')")
    public ResponseEntity<String> activate(ChgStatusTypeForm form) {
        Employee entity = employeeService.readByEmployeeId(form.getEmployeeId());
        employeeService.activate(entity, form);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class EmployeeUI implements Serializable {
        private static final long serialVersionUID = 1L;
        private String employeeId;
        private String mail;
        private String lastName;
        private String firstName;
        private String lastNameKana;
        private String firstNameKana;
        private String listBirthday; // list
        private String crudBirthday; // crud
        private String birthplace;
        private String listSex; // list
        private String crudSex; // crud
        private String listHireDate; // list
        private String crudHireDate; // crud
        private String departmentName; // list
        private String departmentCode; // crud
        private String sectionName; // list
        private String sectionCode; // crud
        private String groupName; // list
        private String groupCode; // crud
        private String position; // list
        private Integer positionValue; // crud
        private AuthorityType authorityType;
        private EmployeeStatusType statusType;

        public static EmployeeUI valueOf(Employee entity, DepartmentMasterService departmentMasterService) {

            DepartmentMaster departmentMaster = departmentMasterService.readByMainOrganization(entity);

            EmployeeUI dto = new EmployeeUI();
            dto.setEmployeeId(entity.getEmployeeId());
            dto.setMail(entity.getMail());
            dto.setLastName(entity.getLastName());
            dto.setFirstName(entity.getFirstName());
            dto.setLastNameKana(entity.getLastNameKana());
            dto.setFirstNameKana(entity.getFirstNameKana());
            dto.setListBirthday(DateUtils.processed(entity.getBirthday()));
            dto.setCrudBirthday(DateUtils.revertYmd(entity.getBirthday()));
            dto.setBirthplace(entity.getBirthplace());
            dto.setListSex(entity.getSex() != null ? entity.getSex().getViewName() : "");
            dto.setCrudSex(entity.getSex() != null ? entity.getSex().toString() : "");
            dto.setListHireDate(DateUtils.processed(entity.getHireDate()));
            dto.setCrudHireDate(DateUtils.revertYmd(entity.getHireDate()));
            dto.setDepartmentName(departmentMaster.getDepartmentName());
            dto.setDepartmentCode(entity.getDepartmentCode());
            dto.setSectionName(departmentMaster.getSectionName());
            dto.setSectionCode(entity.getSectionCode());
            dto.setGroupName(departmentMaster.getGroupName());
            dto.setGroupCode(entity.getGroupCode());
            dto.setPosition(PositionLevelType.positionMap().get(entity.getPosition()));
            dto.setPositionValue(entity.getPosition());
            dto.setAuthorityType(entity.getAuthorityType());
            dto.setStatusType(entity.getStatusType());
            return dto;
        }
    }

}
