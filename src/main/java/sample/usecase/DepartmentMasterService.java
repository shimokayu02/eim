package sample.usecase;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sample.model.entity.DepartmentMaster;
import sample.model.entity.Employee;
import sample.repository.DepartmentMasterRepository;

/**
 * DepartmentMasterドメインに関わる Service。
 */
@Service
public class DepartmentMasterService {
    private final DepartmentMasterRepository departmentMasterRepository;

    /** コンストラクタインジェクション */
    public DepartmentMasterService(
            DepartmentMasterRepository departmentMasterRepository) {
        this.departmentMasterRepository = departmentMasterRepository;
    }

    @Transactional(readOnly = true)
    public List<DepartmentMaster> readAll() {
        return departmentMasterRepository.findAll();
    }

    /**
     * 従業員の所属部署を取得します。
     *
     * @param employee
     * @return
     */
    @Transactional(readOnly = true)
    public DepartmentMaster readByMainOrganization(Employee employee) {
        return departmentMasterRepository.findByMainOrganization(
                employee.getDepartmentCode(),
                employee.getSectionCode(),
                employee.getGroupCode()).orElseThrow(() -> new EntityNotFoundException());
    }

}
