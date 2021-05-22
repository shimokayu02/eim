package sample.autoconfiguration.dialect;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JPA (Hibernate) を使用した PostgreSQL の JSONB 型マッピング。
 *
 * <p>
 * Dialect の詳細については次のブログを参考にしてください。
 * <ul>
 * <li>「<a href="https://www.vojtechruzicka.com/postgresqls-jsonb-type-mapping-using-hibernate/">PostgreSQL’s JSONB type mapping using Hibernate</a>」　Vojtech Ruzicka
 * </ul>
 */
public class CApplyJsonbType extends PostgreSQL95Dialect implements UserType {

    public CApplyJsonbType() {
        this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
    }

    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LogManager.getLogger(CApplyJsonbType.class);

    /** {@inheritDoc} */
    @Override
    public int[] sqlTypes() {
        return new int[] { Types.JAVA_OBJECT };
    }

    /** {@inheritDoc} */
    @Override
    public Class<?> returnedClass() {
        return Map.class;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode(Object x) throws HibernateException {
        int hash = 0;
        hash += (x != null ? x.hashCode() : 0);
        return hash;
    }

    /** {@inheritDoc} */
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws HibernateException, SQLException {
        PGobject pGobject = (PGobject) rs.getObject(names[0]);
        if (pGobject != null && Objects.nonNull(pGobject.getValue())) {
            try {
                return objectMapper.readValue(pGobject.getValue(), returnedClass());
            } catch (JsonProcessingException jpe) {
                logger.error(jpe);
            }
        }
        return Collections.emptyMap();
    }

    /** {@inheritDoc} */
    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        if (Objects.nonNull(value)) {
            try {
                st.setObject(index, objectMapper.writeValueAsString(value).replaceAll("\"null\"", "null"), Types.OTHER);
            } catch (JsonProcessingException jpe) {
                logger.error(jpe);
            }
        } else {
            st.setNull(index, Types.OTHER);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isMutable() {
        // TODO 自動生成されたメソッド・スタブ
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}
