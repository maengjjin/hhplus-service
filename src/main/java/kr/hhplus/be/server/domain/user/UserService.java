package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.Exception.UserException.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserInfo(long userId){
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
