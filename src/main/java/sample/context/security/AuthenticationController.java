package sample.context.security;

import java.io.Serializable;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sample.model.entity.Employee;
import sample.model.type.employee.AuthorityTypeConverter.AuthorityType;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    @GetMapping("/loginStatus")
    public boolean loginStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal() instanceof OpUserDetails;
    }

    @GetMapping("")
    public OperatorInfoUI getOperatorInfo(
            @AuthenticationPrincipal OpUserDetails userDetails) {
        return userDetails != null ? OperatorInfoUI.valueOf(userDetails.getEmployee()) : null;
    }

    @Getter
    @Setter
    @ToString
    @EqualsAndHashCode
    public static class OperatorInfoUI implements Serializable {

        private static final long serialVersionUID = 1L;

        private String employeeId;
        private String lastName;
        private String firstName;
        private AuthorityType authorityType;

        public static OperatorInfoUI valueOf(Employee employee) {
            OperatorInfoUI dto = new OperatorInfoUI();
            dto.setEmployeeId(employee.getEmployeeId());
            dto.setLastName(employee.getLastName());
            dto.setFirstName(employee.getFirstName());
            dto.setAuthorityType(employee.getAuthorityType());
            return dto;
        }
    }

}
