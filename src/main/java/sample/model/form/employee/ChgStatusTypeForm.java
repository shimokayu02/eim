package sample.model.form.employee;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ChgStatusTypeForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String employeeId;
    private String statusType;

}
