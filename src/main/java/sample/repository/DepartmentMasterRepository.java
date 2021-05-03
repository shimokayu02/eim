package sample.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import sample.model.entity.DepartmentMaster;

/**
 * DepartmentMasterドメインに関わる Repository。
 */
@Repository
public class DepartmentMasterRepository extends AbstractRepository<DepartmentMaster> {

    @PersistenceContext(unitName = beanNameEmf)
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<DepartmentMaster> findAll() {
        return em.createNamedQuery("DepartmentMaster.findAll", DepartmentMaster.class)
                .getResultList();
    }

    public Optional<DepartmentMaster> findByDepartmentCodeAndDepartmentAuthority(String departmentCode, String departmentAuthority) {
        return em.createNamedQuery("DepartmentMaster.findByDepartmentCodeAndDepartmentAuthority", DepartmentMaster.class)
                .setParameter("departmentCode", departmentCode)
                .setParameter("departmentAuthority", departmentAuthority)
                .getResultStream()
                .findAny();
    }

    /**
     * 従業員の所属部署を取得します。
     *
     * @param departmentCode
     * @param sectionCode
     * @param groupCode
     * @return
     */
    public Optional<DepartmentMaster> findByMainOrganization(String departmentCode, String sectionCode, String groupCode) {

        if (StringUtils.isEmpty(departmentCode) && StringUtils.isEmpty(sectionCode) && StringUtils.isEmpty(groupCode)) {
            return Optional.of(new DepartmentMaster());
        }

        if (StringUtils.isEmpty(sectionCode) && StringUtils.isEmpty(groupCode)) {
            return em.createNamedQuery("DepartmentMaster.findByDepartmentCodeAndSectionCodeIsNullAndGroupCodeIsNull", DepartmentMaster.class)
                    .setParameter("departmentCode", departmentCode)
                    .getResultStream()
                    .findFirst();
        }
        if (StringUtils.isEmpty(groupCode)) {
            return em.createNamedQuery("DepartmentMaster.findByDepartmentCodeAndSectionCodeAndGroupCodeIsNull", DepartmentMaster.class)
                    .setParameter("departmentCode", departmentCode)
                    .setParameter("sectionCode", sectionCode)
                    .getResultStream()
                    .findFirst();
        }
        return em.createNamedQuery("DepartmentMaster.findByDepartmentCodeAndSectionCodeAndGroupCode", DepartmentMaster.class)
                .setParameter("departmentCode", departmentCode)
                .setParameter("sectionCode", sectionCode)
                .setParameter("groupCode", groupCode)
                .getResultStream()
                .findAny();
    }

}
