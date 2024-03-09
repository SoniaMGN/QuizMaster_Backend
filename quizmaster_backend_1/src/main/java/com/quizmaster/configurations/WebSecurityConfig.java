package com.quizmaster.configurations;

import com.quizmaster.Auth.RegisteredUserDetailService;
import com.quizmaster.filters.JwtTokenFilter;
import com.quizmaster.filters.VerifiedFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    public static String secretKey="SoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSoniaSonia";


    @Autowired
    private JwtTokenFilter jwtTokenFilter;



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers( "/api/v1/user/login","/api/v1/user/register","/swagger-ui/**","/v3/api-docs/**","/api/v1/user/verifyUser").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //http.addFilterAfter(verifiedFilter,UsernamePasswordAuthenticationFilter.class);


    }

    //@Override


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder();
    }


    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Autowired
    private RegisteredUserDetailService registeredUserDetailService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.userDetailsService(registeredUserDetailService);
    }
}
