package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.enums.RoleName;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.exception.TokenRefreshException;
import com.khai.quizguru.model.Image;
import com.khai.quizguru.model.user.RefreshToken;
import com.khai.quizguru.model.user.Role;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.repository.ImageRepository;
import com.khai.quizguru.repository.RefreshTokenRepository;
import com.khai.quizguru.repository.RoleRepository;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.service.RefreshTokenService;
import com.khai.quizguru.utils.TestData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class RefreshTokenServiceImplTest {

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;


    @BeforeEach
    public void setUp(){

        Role role = new Role();
        role.setName(RoleName.USER);
        roleRepository.save(role);

        Image image = new Image();
        imageRepository.save(image);

        User user = new User();
        user.setUsername(TestData.EXIST_USERNAME);
        user.setPassword(TestData.PASSWORD);
        user.setEmail(TestData.EXIST_EMAIL);
        user.setIsEnable(true);
        user.setRoles(List.of(role));
        user.setImage(image);
        userRepository.save(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(3600000));
        refreshToken.setUser(user);
        refreshToken.setToken(TestData.TOKEN);
        refreshTokenRepository.save(refreshToken);

    }


    @Test
    void createRefreshToken_UserNotFound_ThrowsResourceNotFoundException(){

        // Assert that
        assertThrows(ResourceNotFoundException.class, () ->
                refreshTokenService.createRefreshToken(TestData.NOT_FOUND_ID));
    }

    @Test
    void createRefreshToken_success(){

        // Given
        User user = userRepository.findByEmail(TestData.EXIST_EMAIL).get();

        // When
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        // Then
        assertNotNull(refreshToken);
    }

    @Test
    void findByToken_NotFound_ThrowsTokenRefreshException(){

        // Assert that
        assertThrows(TokenRefreshException.class, () ->
                refreshTokenService.findByToken(TestData.NOT_FOUND_TOKEN));
    }

    @Test
    void findByToken_Success(){

        // When
        RefreshToken token = refreshTokenService.findByToken(TestData.TOKEN);

        // Then
        assertEquals(token.getToken(), TestData.TOKEN);

    }

    @Test
    void deleteIfExpired_Success(){

        // Given
        RefreshToken token = refreshTokenService.findByToken(TestData.TOKEN);

        token.setExpiryDate(Instant.now().minusMillis(30000));

        // Assert that
        assertThrows(TokenRefreshException.class, () ->
                refreshTokenService.deleteIfExpired(token));

    }

}