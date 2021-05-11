package uz.bdm.HrTesting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
import uz.bdm.HrTesting.constants.AuthoritiesConstants;
import uz.bdm.HrTesting.exception.CustomAccessDeniedHandler;
import uz.bdm.HrTesting.security.DomainUserDetailsService;
import uz.bdm.HrTesting.security.jwt.JWTConfigurer;
import uz.bdm.HrTesting.security.jwt.JWTTokenProvider;
import uz.bdm.HrTesting.security.jwt.JwtAuthenticationEntryPoint;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JWTTokenProvider JWTTokenProvider;
    private final DomainUserDetailsService domainUserDetailsService;
    //    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfiguration(JWTTokenProvider JWTTokenProvider, DomainUserDetailsService domainUserDetailsService, SecurityProblemSupport problemSupport, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.JWTTokenProvider = JWTTokenProvider;
        this.domainUserDetailsService = domainUserDetailsService;
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

    @Bean
    public CustomAccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
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
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**", "/question/**").permitAll()
                .antMatchers("/candidate/**","/report/**").hasAnyAuthority(AuthoritiesConstants.Moderator,AuthoritiesConstants.Recruiter, AuthoritiesConstants.ADMIN)
//                .antMatchers( "/recruiter/**", "/department/**","/role/**").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.Moderator) /// oldin faqat admin bor edi
//                .antMatchers( "/recruiter/**", "/department/list","/role/**").hasAnyAuthority(AuthoritiesConstants.ADMIN, AuthoritiesConstants.Moderator) /// oldin faqat admin bor edi
                .anyRequest().authenticated()
                .and()
                .apply(securityConfigurerAdapter());
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(JWTTokenProvider);
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(domainUserDetailsService).passwordEncoder(passwordEncoder());
//
//    }


}
