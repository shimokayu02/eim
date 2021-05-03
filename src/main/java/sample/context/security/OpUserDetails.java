package sample.context.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import sample.model.entity.Employee;

public class OpUserDetails implements UserDetails {

    private final Employee employee;
    private final Collection<GrantedAuthority> authorities;

    public OpUserDetails(Employee employee, Collection<GrantedAuthority> authorities) {
        this.employee = employee;
        this.authorities = authorities;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /** {@inheritDoc} */
    @Override
    public String getUsername() {
        return employee.getMail();
    }

    /** {@inheritDoc} */
    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isAccountNonLocked() {
        if (employee.getStatusType().isAccountLock()) {
            return false; // ユーザアカウントがロックされています
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEnabled() {
        if (employee.getStatusType().isInactive()) {
            return false; // 無効なユーザです
        }
        return true;
    }

    public Employee getEmployee() {
        return employee;
    }

}
