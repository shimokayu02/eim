package sample.repository;

import java.io.Serializable;
import java.util.Date;

public interface ActiveMetaRecord extends Serializable {

    public abstract String getOperator();

    public abstract void setOperator(String operator);

    public abstract Date getCreatedDate();

    public abstract void setCreatedDate(Date createdDate);

    public abstract Date getLastModifiedDate();

    public abstract void setLastModifiedDate(Date lastModifiedDate);

}
