package com.cy.config;
import com.cy.service.impl.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 安全控制
 * Created by cy
 * 2017/12/6 15:51
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Bean
    UserDetailsService loginService() {
        return new LoginService();
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/druid/**");
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/list","/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/list").failureUrl("/?error")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and().portMapper().http(80).mapsTo(443)
                .and().csrf().disable();
        http.rememberMe().alwaysRemember(true);

//                //设置默认登录成功跳转页面
//                .defaultSuccessUrl("/index").failureUrl("/login?error").permitAll()
//                .and()
//                //开启cookie保存用户数据
//                .rememberMe()
//                //设置cookie有效期
//                .tokenValiditySeconds(60 * 60 * 24 * 7)
//                //设置cookie的私钥
//                .key("")
    }
}
