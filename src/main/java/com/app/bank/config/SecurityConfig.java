package com.app.bank.config;

import com.app.bank.filter.AuthorizationHeaderFilter;
import com.app.bank.filter.JWTTokenValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

@Configuration
@Profile("!prod")
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   SecurityProperties securityProperties,
                                                   JWTTokenValidationFilter jwtFilter,
                                                   AuthorizationHeaderFilter authHeaderFilter) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(securityProperties.getApis().toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(authHeaderFilter, JWTTokenValidationFilter.class)
                .build();
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
//    CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http, Environment environment) throws Exception {
//        http
//                .cors(
//                cors -> cors.configurationSource(new CorsConfigurationSource() {
//                    @Override
//                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
//                        CorsConfiguration configuration = new CorsConfiguration();
//                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
//                        configuration.setAllowedMethods(Collections.singletonList("*"));
//                        configuration.setAllowCredentials(true);
//                        configuration.setAllowedHeaders(Collections.singletonList("*"));
//                        configuration.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION));
//                        configuration.setMaxAge(3600L);
//                        return configuration;
//                    }
//                }))
//                .csrf(csrfConfig -> csrfConfig.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
//                        .ignoringRequestMatchers("api/contact", "api/notices", "api/notices/cache", "/api/users/login")
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
//                .addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
//                .addFilterAfter(new JWTTokenGenerationFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new RequestValidationFilter(), BasicAuthenticationFilter.class)
//                .addFilterAfter(new JWTTokenValidationFilter(), BasicAuthenticationFilter.class)
//                .addFilterAfter(new AuthoritiesLoggingAfterFilter(), JWTTokenValidationFilter.class)
//                .sessionManagement(
//                        smc ->
//                                smc.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId)
//                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                                .invalidSessionUrl("/invalidSession")
//                                .maximumSessions(4)
//                                .maxSessionsPreventsLogin(true))
//                .requiresChannel(rcc -> rcc.anyRequest().requiresInsecure())
//                .authorizeHttpRequests(
//                        requests -> requests
//                                .requestMatchers("/", "api/account/**", "api/myAccount/**").hasRole("USER")
//                                .requestMatchers( "api/balance", "api/myBalance").hasAnyRole("USER", "ADMIN")
//                                .requestMatchers("api/cards/**", "api/myCards/**").hasAuthority("ADMIN")
//                                .requestMatchers( "api/loans/**", "api/myLoans/**").hasAnyAuthority("VIEWLOANS", "VIEWACCOUNT")
//                                .requestMatchers( "api/users").authenticated()
//                                .requestMatchers("api/notices","api/notices/cache", "api/contact", "/error", "/api/users/register", "/api/users/login", "/favicon.ico").permitAll());
//        http.exceptionHandling(ehc -> ehc.accessDeniedHandler(new CustomAccessDeniedHandler()));
//
//        return http.build();
//    }