package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PointJpaRepository extends JpaRepository<User, Long> {


}
