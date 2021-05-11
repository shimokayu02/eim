package sample.repository;

import java.util.Date;
import java.util.Objects;

import javax.persistence.EntityManager;

import sample.util.Validator;

/**
 * AbstractRepository。
 *
 * @param <T> entity
 */
public abstract class AbstractRepository<T extends ActiveRecord> {
    protected final String beanNameEmf = "eim_entityManagerFactory";

    protected abstract EntityManager getEntityManager();

    public void save(T entity) {
        entity.setOperator(entity.getOperator());
        if (Objects.isNull(entity.getCreatedDate())) {
            Date createdDate = entity.getLastModifiedDate();
            entity.setCreatedDate(createdDate);
        }
        entity.setLastModifiedDate(entity.getLastModifiedDate());
        getEntityManager().persist(Validator.checkFields(entity));
    }

}
