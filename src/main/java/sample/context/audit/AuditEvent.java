package sample.context.audit;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import lombok.Data;
import lombok.NoArgsConstructor;
import sample.context.audit.type.CategoryType;

/**
 * AuditEvent。
 *
 * <p>low: It is DDD demo implementation based on jkazama's <a href="https://github.com/jkazama/ddd-java">ddd-java</a>
 */
@Entity
@Table(name = "audit_event")
@Data
public class AuditEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** 時間 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "occurred_time")
    private Date occurredTime;
    /** カテゴリ */
    @Enumerated(EnumType.STRING)
    private CategoryType category;
    /** メッセージ */
    @Size(max = 1024)
    private String message;
    /** ユーザ名 */
    private String username;

    public static AuditEvent register(AbstractAuthenticationEvent event, String message) {
        return new RegAuditEventDTO().create(event, message);
    }

    @Data
    @NoArgsConstructor
    public static class RegAuditEventDTO implements Serializable {
        private static final long serialVersionUID = 1L;

        public AuditEvent create(AbstractAuthenticationEvent event, String message) {
            AuditEvent entity = new AuditEvent();
            entity.setOccurredTime(new Timestamp(event.getTimestamp()));
            entity.setCategory(getCategory(event));
            entity.setMessage(message);
            entity.setUsername(event.getAuthentication().getName());
            return entity;
        }

        private CategoryType getCategory(AbstractAuthenticationEvent event) {

            if (event instanceof AuthenticationSuccessEvent) {
                return CategoryType.LOGINED;
            } else if (event instanceof AbstractAuthenticationFailureEvent) {
                return CategoryType.LOGIN_ERROR;
            }
            return null;
        }
    }

}
