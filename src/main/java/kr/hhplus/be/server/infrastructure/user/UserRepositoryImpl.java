package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.Exception.UserException.UserNotFoundException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findById(long userId) {
        return userJpaRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }




}
