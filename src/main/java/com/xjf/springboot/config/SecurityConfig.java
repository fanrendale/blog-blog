package com.xjf.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全配置
 *
 * @Author: xjf
 * @Date: 2019/2/17 20:37
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)      //启用方法安全设置
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String KEY = "xjf666.xyz";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();     //使用BCrypt加密
    }

    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);     //设置密码加密方式

        return authenticationProvider;
    }

    /**
     * 自定义配置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**","/js/**","/fonts/**","index").permitAll()   //都可以访问
                .antMatchers("/h2-console/**").permitAll()      //都可以访问
                //需要相应的角色才能访问
                .antMatchers("/admins/**").hasRole("ADMIN")
                .and()
                //基于表单登录验证
                .formLogin()
                //自定义登录界面
                .loginPage("/login").failureUrl("/login-error")
                //启用remember me
                .and().rememberMe().key(KEY)
                //处理异常，拒绝访问就重定向到403
                .and().exceptionHandling().accessDeniedPage("/403");

        //禁用H2控制台的CSRF防护
        http.csrf().ignoringAntMatchers("/h2-console/**");
        //允许来自同一来源的H2控制台的请求
        http.headers().frameOptions().sameOrigin();
    }

    /**
     * 认证信息管理
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }
}
