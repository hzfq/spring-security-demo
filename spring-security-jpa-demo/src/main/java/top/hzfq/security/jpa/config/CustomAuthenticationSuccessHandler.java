package top.hzfq.security.jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 自定义认证成功处理类，用户认证成功后处理不同用户跳转到不同的页面
 *
 * @author hzfq
 * @email huzhifengqing@qq.com
 * @date 2019/11/29
 */
@Configuration
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    //负责所有的重定向事务
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        //重定向请求到相应的URL
        redirectStrategy.sendRedirect(request, response, determineTargetUrl(authentication));
    }

    /**
     * 根据当前登录用户的角色返回适当的URL
     *
     * @param authentication
     * @return
     */
    private String determineTargetUrl(Authentication authentication) {
        //获取当前登录用户的角色权限集合
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            roles.add(authority.getAuthority());
        }
        //判断角色并跳转到相应界面
        if (isAdmin(roles)) {
            return "/admin";
        } else if (isUser(roles)) {
            return "/home";
        } else {
            return "/accessDenied";
        }
    }

    private boolean isUser(List<String> roles) {
        return roles.contains("ROLE_USER") ? true : false;
    }


    private boolean isAdmin(List<String> roles) {
        return roles.contains("ROLE_ADMIN") ? true : false;
    }

    public RedirectStrategy getRedirectStrategy() {
        return redirectStrategy;
    }

    public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
        this.redirectStrategy = redirectStrategy;
    }
}
