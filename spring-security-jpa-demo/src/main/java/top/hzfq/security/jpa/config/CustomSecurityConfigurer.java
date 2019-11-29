package top.hzfq.security.jpa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.hzfq.security.jpa.service.UserService;

/**
 * 自定义认证处理类，重写认证相关的方法完成认证处理
 *
 * @author hzfq
 * @email huzhifengqing@qq.com
 * @date 2019/11/29
 */
@Configuration
public class CustomSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder; //加密接口
    @Autowired
    private AuthenticationProvider authenticationProvider; //用户认证接口
    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler; //认证成功处理类，用户认证成功后处理不同用户跳转到不同的页面

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Spring Security5.x版本需要过滤静态资源
        http.authorizeRequests()
                //允许所有人访问登录页、静态资源
                .antMatchers("/login", "/css/**", "/js/**", "/img/*", "/vendors/**").permitAll()
                //角色为USER的用户可以访问"/"和"/home"
                .antMatchers("/", "/home").hasRole("USER")
                //角色为ADMIN和DBA的用户可以访问"/admin"及其子页面
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "DBA")
                //其他请求都需要权限认证
                .anyRequest().authenticated().and()
                //指定表单登录界面
                .formLogin().loginPage("/login").successHandler(authenticationSuccessHandler)
                //登录用的用户名和密码参数
                .usernameParameter("loginName").passwordParameter("password").and()
                //所有人都可以访问注销请求
                .logout().permitAll().and()
                //异常处理和访问拒绝页面
                .exceptionHandling().accessDeniedPage("/accessDenied");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //指定自定义的认证方式
        auth.authenticationProvider(authenticationProvider);
    }

    /**
     * 自定义用户认证处理
     *
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //不隐藏"用户未找到"异常
        provider.setHideUserNotFoundExceptions(false);
        //自定义的用户服务类，需要实现UserDetailsService接口
        provider.setUserDetailsService(userService);
        //设置密码加密方式
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * 密码加密方法，此处使用SSpring Security提供的BCryptPasswordEncoder加密类，可指定其他加密类，也可以自定义加密类
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
