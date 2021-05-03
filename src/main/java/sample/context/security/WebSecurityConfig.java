package sample.context.security;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /** {@inheritDoc} */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().anonymous();
        // login
        http.formLogin()
                .usernameParameter("mail")
                .defaultSuccessUrl("http://localhost:3000")
                .permitAll();
        http.authorizeRequests()
                .anyRequest().authenticated();
    }

    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public class MethodSecurityConfig {

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(prefix = "extension.security.cors", name = "enabled", matchIfMissing = false)
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource ubccs = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfigure = new CorsConfiguration();
        corsConfigure.setAllowCredentials(true);
        corsConfigure.addAllowedOrigin("http://localhost:3000");
        corsConfigure.addAllowedMethod("*");
        corsConfigure.setMaxAge(3600L);
        ubccs.registerCorsConfiguration("/**", corsConfigure);
        return new CorsFilter(ubccs);
    }

}
