package top.hzfq.security.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import top.hzfq.security.jpa.domain.FKUser;

/**
 * @author hzfq
 * @email huzhifengqing@qq.com
 * @date 2019/11/29
 */
public interface UserRepo extends JpaRepository<FKUser, Long> {

    FKUser findByLoginName(String loginName);
}
