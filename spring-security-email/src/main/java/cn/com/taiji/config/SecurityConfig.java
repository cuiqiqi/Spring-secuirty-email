package cn.com.taiji.config;


import cn.com.taiji.security.CustomFilterSecurityInterceptor;
//import cn.com.taiji.security.CustomlAuthoritiesExtractor;
import cn.com.taiji.security.exception.CustomAuthenticationEntryPoint;
import cn.com.taiji.security.exception.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomFilterSecurityInterceptor customFilterSecurityInterceptor;
//    @Autowired
//    private CustomlAuthoritiesExtractor customlAuthoritiesExtractor;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
         http
                .authorizeRequests()
                .antMatchers("/send","/css/**", "/js/**", "/fonts/**","/email/**","/code/**", "/login").permitAll()
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login")
                .failureUrl("/login?error").permitAll()
                //注销行为任意访问
                .and().logout().permitAll()
                .and().exceptionHandling().accessDeniedHandler(customAccessDeniedHandler()).and()
//                .authenticationEntryPoint(customAuthenticationEntryPoint())
                .addFilterBefore(customFilterSecurityInterceptor, FilterSecurityInterceptor.class)
                ;

    }

//    @Autowired
//    private CustomUserService customUserService ;
    @Autowired
    private cn.com.taiji.security.EmailAuthenticationProvider EmailAuthenticationProvider;
    /**
     * 认证信息管理
     *
     * @param builder
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        //builder.userDetailsService(customUserService).passwordEncoder(passwordEncoder());
        builder.authenticationProvider(EmailAuthenticationProvider);
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        CustomAccessDeniedHandler cadh= new CustomAccessDeniedHandler();
        cadh.setErrorPage("/403");
        return cadh;
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

}

