package com.zephyr.mpbssecurity.config;

import com.zephyr.mpbssecurity.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors();
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .antMatchers("/static/**").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/ULTIMATE/**").hasRole("ULTIMATE")
                .antMatchers("/SENIOR/**").hasAnyRole("ULTIMATE", "SENIOR")
                .antMatchers("/INTERMEDIATE/**").hasAnyRole("ULTIMATE", "SENIOR", "INTERMEDIATE")
                .antMatchers("/JUNIOR/**").hasAnyRole("ULTIMATE", "SENIOR", "INTERMEDIATE", "JUNIOR")
                .antMatchers("/NORMAL/**").hasAnyRole("ULTIMATE", "SENIOR", "INTERMEDIATE", "JUNIOR", "NORMAL")

                .anyRequest().authenticated();
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
