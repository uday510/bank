package com.app.bank.config;

import com.app.bank.exceptionhandling.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
                .csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(
                        requests -> requests.requestMatchers("/", "api/account", "api/myBalance",
                "api/myCards", "api/myLoans").authenticated()
                .requestMatchers("api/notices", "api/contact", "/error", "/api/users/register").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(httpBasicConfig -> httpBasicConfig.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        return http.build();
    }

/*    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }*/

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
