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
                .antMatchers("/files/share/download/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/email/sendCode").permitAll()
                .antMatchers("/blog/getBlogList").permitAll()
                .antMatchers("/blog/detail/**").permitAll()
                .antMatchers("/blog/getCommentsByPost/**").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/usr/**").permitAll()
                .antMatchers("/admin/**").hasRole("ULTIMATE")
                .antMatchers("/blog/post").hasRole("ULTIMATE")
                .antMatchers("/blog/update/**").hasRole("ULTIMATE")
                .antMatchers("/blog/delete/**").hasRole("ULTIMATE")
                .antMatchers("/files/**").hasAnyRole("ULTIMATE", "SENIOR")
                .antMatchers("/logs/**").hasAnyRole("ULTIMATE", "SENIOR", "INTERMEDIATE")
                .anyRequest().authenticated();
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
