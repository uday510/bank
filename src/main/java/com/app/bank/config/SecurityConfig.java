package com.app.bank.config;

import com.app.bank.exception.CustomAccessDeniedHandler;
import com.app.bank.exception.CustomBasicAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
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
                .sessionManagement(
                        smc -> smc.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId)
                        .invalidSessionUrl("/invalidSession")
                        .maximumSessions(4)
                        .maxSessionsPreventsLogin(true))                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
                .csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(
                        requests -> requests.requestMatchers("/", "api/account", "api/balance",
                "api/cards", "api/loans").authenticated()
                .requestMatchers("api/notices", "api/contact", "/error", "/api/users/register").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(httpBasicConfig -> httpBasicConfig.authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));
        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
        http.logout(logout -> logout.logoutSuccessUrl("/api/logout").invalidateHttpSession(true)
                .deleteCookies("JSESSIONID"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
