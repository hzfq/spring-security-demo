package top.hzfq.security.jpa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录控制器
 *
 * @author hzfq
 * @email huzhifengqing@qq.com
 * @date 2019/11/29
 */
@Controller
@RequestMapping("/")
public class LoginController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    /**
     * 主页
     *
     * @param model
     * @return
     */
    @GetMapping("home")
    public String home(Model model) {
        logger.info("home page");
        model.addAttribute("user", getUsername());
        model.addAttribute("role", getAuthority());
        return "home";
    }

    /**
     * 管理员主页
     *
     * @param model
     * @return
     */
    @GetMapping("admin")
    public String admin(Model model) {
        logger.info("admin page");
        model.addAttribute("user", getUsername());
        model.addAttribute("role", getAuthority());
        return "admin";
    }

    /**
     * DBA主页
     *
     * @param model
     * @return
     */
    @GetMapping("dba")
    public String dba(Model model) {
        logger.info("dba page");
        model.addAttribute("user", getUsername());
        model.addAttribute("role", getAuthority());
        return "dba";
    }

    /**
     * 访问拒绝
     *
     * @param model
     * @return
     */
    @GetMapping("accessDenied")
    public String accessDenied(Model model) {
        logger.info("accessDenied page");
        model.addAttribute("user", getUsername());
        model.addAttribute("role", getAuthority());
        return "accessDenied";
    }

    /**
     * 注销
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //用户认证信息不为空时注销
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout";
    }

    /**
     * 获取当前用户的用户名
     *
     * @return
     */
    private String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * 获取当前用户的权限
     *
     * @return
     */
    private String getAuthority() {
        //用户认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            roles.add(authority.getAuthority());
        }
        return roles.toString();
    }
}
