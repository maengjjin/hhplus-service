package kr.hhplus.be.server.domain.user;

import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface UserRepository {

    User findById(long id);
}
