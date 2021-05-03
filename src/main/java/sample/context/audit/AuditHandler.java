package sample.context.audit;

import static sample.context.audit.AuditEvent.*;

import java.util.Optional;

import org.apache.commons.lang3.mutable.MutableInt;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sample.model.entity.Employee;
import sample.model.type.employee.StatusTypeConverter.EmployeeStatusType;
import sample.repository.EmployeeRepository;

@Component
@Transactional
public class AuditHandler {
    private final EmployeeRepository employeeRepository;
    private final AuditEventRepository auditEventRepository;

    /** コンストラクタインジェクション */
    public AuditHandler(
            AuditEventRepository auditEventRepository,
            EmployeeRepository employeeRepository) {
        this.auditEventRepository = auditEventRepository;
        this.employeeRepository = employeeRepository;
    }

    @EventListener
    public void handleLoginSuccessEvent(AuthenticationSuccessEvent event) {

        AuditEvent entity = register(event, null);
        auditEventRepository.save(entity);

        String mail = event.getAuthentication().getName();
        Optional<Employee> employeeOpt = employeeRepository.findByMail(mail);
        employeeOpt.ifPresent(employee -> {
            MutableInt mi = new MutableInt();
            if (employee.getStatusType().isXXX()) {
                employee.setStatusType(EmployeeStatusType.Active);
                mi.increment();
            }
            if (employee.getLoginFailureCount() != 0) {
                employee.setLoginFailureCount(0); // 初期化
                mi.increment();
            }
            if (mi.getValue() != 0) {
                employeeRepository.save(employee);
            }
        });
    }

    @EventListener
    public void handleLoginFailureEvent(AbstractAuthenticationFailureEvent event) {

        AuditEvent entity = register(event, event.getException().getMessage());
        auditEventRepository.save(entity);

        String mail = event.getAuthentication().getName();
        Optional<Employee> employeeOpt = employeeRepository.findByMail(mail);
        employeeOpt.ifPresent(employee -> {
            employee.setLoginFailureCount(new MutableInt(employee.getLoginFailureCount()).incrementAndGet());
            if (employee.getLoginFailureCount() == 6 && !employee.getStatusType().isInactive()) {
                employee.setStatusType(EmployeeStatusType.AccountLock);
            }
            employeeRepository.save(employee);
        });
    }

}
