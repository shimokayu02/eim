package sample.repository;

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
        entity.setLastModifiedDatetime(entity.getLastModifiedDatetime());
        getEntityManager().persist(Validator.checkFields(entity));
    }

}
