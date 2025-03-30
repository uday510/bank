package com.app.bank.config;

import com.app.bank.exception.CustomAccessDeniedHandler;
import com.app.bank.exception.CustomBasicAuthenticationEntryPoint;
import com.app.bank.filter.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Profile("!prod")
public class SecurityConfig {

    CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, Environment environment) throws Exception {
        http
                .cors(
                cors -> cors.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION));
                        configuration.setMaxAge(3600L);
                        return configuration;
                    }
                }))
                .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
                        .ignoringRequestMatchers("api/contact", "api/notices", "api/notices/cache", "/api/users/login")
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGenerationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new RequestValidationFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenValidationFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
                .sessionManagement(
                        smc ->
                                smc.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId)
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .invalidSessionUrl("/invalidSession")
                                .maximumSessions(4)
                                .maxSessionsPreventsLogin(true))
//                .securityContext(contextConfig -> contextConfig.requireExplicitSave(false))
                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
                .authorizeHttpRequests(
                        requests -> requests
//                                .requestMatchers("/", "api/account/**", "api/myAccount/**").hasAuthority("VIEWACCOUNT")
//                                .requestMatchers( "api/balance", "api/myBalance").hasAuthority("VIEWBALANCE")
//                                .requestMatchers("api/cards/**", "api/myCards/**").hasAuthority("VIEWCARDS")
//                                .requestMatchers( "api/loans/**", "api/myLoans/**").hasAnyAuthority("VIEWLOANS", "VIEWACCOUNT")
                                .requestMatchers("/", "api/account/**", "api/myAccount/**").hasRole("USER")
                                .requestMatchers( "api/balance", "api/myBalance").hasAnyRole("USER", "ADMIN")
                                .requestMatchers("api/cards/**", "api/myCards/**").hasAuthority("ADMIN")
                                .requestMatchers( "api/loans/**", "api/myLoans/**").hasAnyAuthority("VIEWLOANS", "VIEWACCOUNT")
                                .requestMatchers( "api/users").authenticated()
                                .requestMatchers("api/notices","api/notices/cache", "api/contact", "/error", "/api/users/register", "/api/users/login", "/favicon.ico").permitAll());
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

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {

        BankUserNamePasswordAuthenticationProvider authenticationProvider = new BankUserNamePasswordAuthenticationProvider(userDetailsService);

        ProviderManager providerManager = new ProviderManager(authenticationProvider);
        providerManager.setEraseCredentialsAfterAuthentication(false);
        return providerManager;
    }
}
