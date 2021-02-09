package uz.bdm.HrTesting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import uz.bdm.HrTesting.security.jwt.JWTConfigurer;
import uz.bdm.HrTesting.security.jwt.JWTTokenProvider;
import uz.bdm.HrTesting.security.jwt.JwtAuthenticationEntryPoint;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JWTTokenProvider JWTTokenProvider;

//    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfiguration(JWTTokenProvider JWTTokenProvider, SecurityProblemSupport problemSupport, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.JWTTokenProvider = JWTTokenProvider;
//        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/i18n/**"); // #3
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
//            .logout()
//            .logoutUrl("/logout")
//            .invalidateHttpSession(true)
//            .permitAll()
//            .and()
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(problemSupport)
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**","/question/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(securityConfigurerAdapter());
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(JWTTokenProvider);
    }

}
