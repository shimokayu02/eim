package sample.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import sample.model.entity.Employee;
import sample.model.type.PositionLevelType;

/**
 * Employeeドメインに関わる Repository。
 */
@Repository
public class EmployeeRepository extends AbstractRepository<Employee> {

    @PersistenceContext(unitName = beanNameEmf)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Employee> findAll() {
        return em.createNamedQuery("Employee.findAll", Employee.class)
                .getResultList();
    }

    public Optional<Employee> findByEmployeeId(String employeeId) {
        return em.createNamedQuery("Employee.findByEmployeeId", Employee.class)
                .setParameter("employeeId", employeeId)
                .getResultStream()
                .findAny();
    }

    public Optional<Employee> findByMail(String mail) {
        return em.createNamedQuery("Employee.findByMail", Employee.class)
                .setParameter("mail", mail)
                .getResultStream()
                .findAny();
    }

    public Optional<Employee> findByEmployeeIdNotAndMail(String employeeId, String mail) {
        return em.createNamedQuery("Employee.findByEmployeeIdNotAndMail", Employee.class)
                .setParameter("employeeId", employeeId)
                .setParameter("mail", mail)
                .getResultStream()
                .findAny();
    }

    @SuppressWarnings("unchecked")
    public List<Employee> findByEmployeeIdOrMainOrganizationAndPositionLessThan(String employeeId, String departmentCode, String sectionCode, String groupCode, Integer position) {

        if (StringUtils.isEmpty(departmentCode) && StringUtils.isEmpty(sectionCode) && StringUtils.isEmpty(groupCode)) {
            return new ArrayList<>();
        }

        StringBuilder sb = new StringBuilder();
        // see https://stackoverflow.com/questions/42901240/jpa-and-json-operator-native-query
        sb.append("SELECT e.* FROM employee e WHERE e.employee_id = ?1 or e.main_organization ->> 'department_code' = ?2 and e.position < ?3");
        if (position == PositionLevelType.DEPARTMENT_MANAGER.getValue()
                || StringUtils.isEmpty(sectionCode) && StringUtils.isEmpty(groupCode)) {
            return em.createNativeQuery(sb.toString(), Employee.class)
                    .setParameter(1, employeeId)
                    .setParameter(2, departmentCode)
                    .setParameter(3, position)
                    .getResultList();
        }
        sb.append(" and e.main_organization ->> 'section_code' = ?4");
        if (position == PositionLevelType.SECTION_MANAGER.getValue() || StringUtils.isEmpty(groupCode)) {
            return em.createNativeQuery(sb.toString(), Employee.class)
                    .setParameter(1, employeeId)
                    .setParameter(2, departmentCode)
                    .setParameter(3, position)
                    .setParameter(4, sectionCode)
                    .getResultList();
        }
        sb.append(" and e.main_organization ->> 'group_code' = ?5");
        return em.createNativeQuery(sb.toString(), Employee.class)
                .setParameter(1, employeeId)
                .setParameter(2, departmentCode)
                .setParameter(3, position)
                .setParameter(4, sectionCode)
                .setParameter(5, groupCode)
                .getResultList();
    }

}
