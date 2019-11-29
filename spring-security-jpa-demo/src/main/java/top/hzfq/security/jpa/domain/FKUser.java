package top.hzfq.security.jpa.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 实际开发中用户表可继承{@link org.springframework.security.core.userdetails.UserDetails}接口，本例是在{@link
 * org.springframework.security.core.userdetails.UserDetailsService}接口中进行绑定
 *
 * @author hzfq
 * @email huzhifengqing@qq.com
 * @date 2019/11/29
 */
@Data
@Entity
@Table(name = "fk_user")
public class FKUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false, unique = true)
    private String loginName; //登录用户名，唯一
    @Column(length = 50, nullable = false, unique = true)
    private String username; //真实用户名或昵称
    @Column(length = 100, nullable = false)
    private String password;
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "fk_user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<FKRole> roles; //用户权限，多对多关系
}
