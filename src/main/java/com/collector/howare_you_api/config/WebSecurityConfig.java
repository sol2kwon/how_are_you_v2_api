package com.collector.howare_you_api.config;

import com.collector.howare_you_api.auth.handle.OAuth2SuccessHandler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {

    private final DefaultOAuth2UserService userService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(CsrfConfigurer::disable) //CSRF 보호 비활성화
                .httpBasic(HttpBasicConfigurer::disable) //HTTP Basic 인증 비활성화
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않는 JWT 방식
                //경로별  인증 및 권한
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/api/v1/auth/**", "/oauth2/**").permitAll()
                        .requestMatchers("/api/v1/user/**").hasRole("USER")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint.baseUri("/api/v1/auth/oauth2"))
                        .redirectionEndpoint(endpoint -> endpoint.baseUri("/oauth2/callback/*"))
                        .userInfoEndpoint(endpoint ->endpoint.userService(userService)) // OAuth2 로그인 시 사용자 정보를 가져오는 엔드포인트
                        .successHandler(oAuth2SuccessHandler)
                );
                //.exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(new FailedAuthenticationEntryPoint()))
                //.addFilterBefore(jwtAu)
        return http.build();

    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
////        corsConfiguration.addAllowedOrigin(origin:"*");
//
//        return new InMemoryUserDetailsManager(user);
//    }


}
