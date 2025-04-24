package kr.hhplus.be.server.domain.user;

import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface UserRepository {

    Optional<User> findById(long id);

    User save(User build);
}
