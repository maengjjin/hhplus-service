package kr.hhplus.be.server.domain;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import kr.hhplus.be.server.Exception.UserException.UserNotFoundException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private long userId = 1L;

    User user = null;

    @BeforeEach
    void beforeEach(){
        user = User.of(userId, 30000L);
    }

    @Test
    void 유저_조회_성공() {
        // given 유효한 userId에 대한 사용자 정보가 존재함
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when userId를 통해 사용자 정보 조회
        User userInfo = userService.getUserInfo(userId);

        // then 반환된 사용자 정보가 기대한 값과 일치하는지 확인
        Assertions.assertThat(userInfo).isEqualTo(user);
    }

    @Test
    @DisplayName("없는 userId로 조회를 했을 때 실패")
    void 없는_아이디로_조회_했을_때_실패() {

        // when & then 조회 시 예외가 발생해야 함
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserInfo(2L);
        });

    }


}
