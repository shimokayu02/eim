package sample.context.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sample.model.entity.Employee;
import sample.repository.EmployeeRepository;

@Service
public class OpUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    /** コンストラクタインジェクション */
    public OpUserDetailsService(
            EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public OpUserDetails loadUserByUsername(String mail) {
        Employee employee = employeeRepository.findByMail(mail)
                .orElseThrow(() -> new UsernameNotFoundException("ログインに失敗しました。"));
        return new OpUserDetails(employee, getAuthorities(employee));
    }

    private Collection<GrantedAuthority> getAuthorities(Employee employee) {
        String[] authorities = Arrays.asList(employee.getAuthorityType().authorities())
                .stream()
                .map(x -> "ROLE_" + x).toArray(String[]::new);
        return AuthorityUtils.createAuthorityList(authorities);
    }

}
