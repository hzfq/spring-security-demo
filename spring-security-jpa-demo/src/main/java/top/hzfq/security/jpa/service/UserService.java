package top.hzfq.security.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import top.hzfq.security.jpa.domain.FKRole;
import top.hzfq.security.jpa.domain.FKUser;
import top.hzfq.security.jpa.repo.UserRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户服务类需要实现{@link UserDetailsService}，因为在Spring Security中配置的相关参数是需要{@link UserDetailsService}类型的数据
 *
 * @author hzfq
 * @email huzhifengqing@qq.com
 * @date 2019/11/29
 */
@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    /**
     * 查询用户，{@link UserDetails}是Spring Security的核心接口，其中定义了获取用户名、密码、权限等与认证相关信息的方法
     *
     * @param username 登录用户名，唯一
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //查询用户
        FKUser fkUser = userRepo.findByLoginName(username);
        if (fkUser == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        //保存用户权限，GrantedAuthority对象代表当前用户的权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<FKRole> roles = fkUser.getRoles();
        for (FKRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }
        //返回的User类是Spring Security的内部实现，用于保存用户名、密码、权限等与认证相关信息
        return new User(fkUser.getUsername(), fkUser.getPassword(), authorities);
    }
}
