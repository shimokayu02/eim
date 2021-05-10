package sample.repository;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.EntityManager;

import sample.util.Validator;

/**
 * AbstractRepository。
 *
 * @param <T> entity
 */
public abstract class AbstractRepository<T extends ActiveMetaRecord> {
    protected final String beanNameEmf = "eim_entityManagerFactory";

    protected abstract EntityManager getEntityManager();

    public void save(T entity) {
        entity.setOperator(entity.getOperator());
        if (Objects.isNull(entity.getCreatedDate())) {
            entity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        }
        entity.setLastModifiedDate(entity.getLastModifiedDate());
        getEntityManager().persist(Validator.checkFields(entity));
    }

}
