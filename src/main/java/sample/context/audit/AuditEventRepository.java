package sample.context.audit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import sample.util.Validator;

@Repository
public class AuditEventRepository {

    @PersistenceContext(unitName = "eim_entityManagerFactory")
    private EntityManager em;

    public void save(AuditEvent entity) {
        em.persist(Validator.checkFields(entity));
    }

}
