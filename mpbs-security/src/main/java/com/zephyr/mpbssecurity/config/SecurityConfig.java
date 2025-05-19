package com.zephyr.mpbssecurity.config;

import com.zephyr.mpbssecurity.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/public/**").permitAll()   // 公开接口，无需token
                .antMatchers("/usr/**").permitAll()     // 登录注册接口无需token
                .antMatchers("/admin/**").hasRole("ULTIMATE") //hasAnyRole("ADMIN", "ULTIMATE") // 支持多个角色
                .anyRequest().authenticated();               // 其他接口都需要认证

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
