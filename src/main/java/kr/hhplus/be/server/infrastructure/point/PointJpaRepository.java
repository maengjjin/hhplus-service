package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PointJpaRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("UPDATE User u SET u.point = :point WHERE u.userId = :userId")
    void updatePoint(@Param("userId") long userId, @Param("point") long point);

}
