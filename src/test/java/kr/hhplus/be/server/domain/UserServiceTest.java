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
        //arrange 유저 조회를 하기 위해 user를 준비한다

        Mockito.when(userRepository.findById(userId)).thenReturn(user);

        // act userId를 가지고 user의 정보를 조회 한다
        User userInfo = userService.getUserInfo(userId);

        // 테스트 결과
        Assertions.assertThat(user).isEqualTo(userInfo);

    }

    @Test
    @DisplayName("없는 userId로 조회를 했을 때 실패")
    void 유저_조회_실패(){

        //arrange 없는 userId를 조회 할 경우 null을 리턴하게 세팅
        Mockito.when(userRepository.findById(2L))
            .thenReturn(null);


        // 테스트 결과
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserInfo(2L);
        });

    }



}
