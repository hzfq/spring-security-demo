package top.hzfq.security.jpa.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author hzfq
 * @email huzhifengqing@qq.com
 * @date 2019/11/28
 */
@Data
@Entity
@Table(name = "fk_role")
public class FKRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false, unique = true)
    private String authority; //用户权限
}
