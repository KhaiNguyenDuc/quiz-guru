package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.enums.RoleName;
import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.TokenRefreshException;
import com.khai.quizguru.model.Image;
import com.khai.quizguru.model.user.Role;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.model.user.VerificationToken;
import com.khai.quizguru.repository.ImageRepository;
import com.khai.quizguru.repository.RoleRepository;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.repository.VerificationTokenRepository;
import com.khai.quizguru.utils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class VerifyTokenServiceImplTest {

    @Mock
    private VerificationTokenRepository verificationTokenRepositoryMock;

    @Mock
    private UserRepository userRepositoryMock;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    ImageRepository imageRepository;

    @InjectMocks
    private VerifyTokenServiceImpl verifyTokenService;


    @Test
    void findTokenByUser_UserNotFound_ThrowsInvalidRequestException() {
        
        // When
        when(userRepositoryMock.findById(TestData.NOT_FOUND_ID)).thenReturn(Optional.empty());

        // Then
        assertThrows(InvalidRequestException.class, () -> verifyTokenService.findTokenByUser(TestData.NOT_FOUND_ID));
    }

    @Test
    void findTokenByUser_TokenFound_Success() {

        // Given
        User user = new User();
        when(userRepositoryMock.findById(anyString())).thenReturn(Optional.of(user));

        VerificationToken verificationToken = new VerificationToken();
        when(verificationTokenRepositoryMock.findByUser(user)).thenReturn(Optional.of(verificationToken));

        // When
        VerificationToken result = verifyTokenService.findTokenByUser(TestData.USER_ID);

        // Then
        assertNotNull(result);
    }

    @Test
    void verifyUser_UserNotFound_ThrowsInvalidRequestException() {
        // When
        when(userRepositoryMock.findByEmail(TestData.NOT_FOUND_EMAIL)).thenReturn(Optional.empty());

        // Then
        assertThrows(InvalidRequestException.class, () -> verifyTokenService.verifyUser(TestData.TOKEN, TestData.NOT_FOUND_EMAIL));
    }

    @Test
    void verifyUser_TokenExpired_ThrowsTokenRefreshException() {

        // When
        User user = new User();
        when(userRepositoryMock.findByEmail(TestData.EXIST_EMAIL)).thenReturn(Optional.of(user));

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setExpiryDate(Instant.now().minusMillis(1)); // Token expired
        when(verificationTokenRepositoryMock.findByUser(user)).thenReturn(Optional.of(verificationToken));

        // Then
        assertThrows(TokenRefreshException.class, () -> verifyTokenService.verifyUser(TestData.TOKEN, TestData.EXIST_EMAIL));
    }

    @Test
    void verifyUser_TokenNotFound_ThrowsTokenRefreshException() {

        // When
        User user = new User();
        when(userRepositoryMock.findByEmail(TestData.EXIST_EMAIL)).thenReturn(Optional.of(user));

        when(verificationTokenRepositoryMock.findByUser(user)).thenReturn(Optional.empty());

        // Then
        assertThrows(TokenRefreshException.class, () -> verifyTokenService.verifyUser(TestData.TOKEN, TestData.EXIST_EMAIL));
    }



    @Test
    void verifyUser_InvalidToken_ReturnsFalse() {

        // When
        User user = new User();
        when(userRepositoryMock.findByEmail(TestData.EXIST_EMAIL)).thenReturn(Optional.of(user));

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setExpiryDate(Instant.now().plusMillis(3600000));
        verificationToken.setToken(TestData.NOT_FOUND_TOKEN);
        when(verificationTokenRepositoryMock.findByUser(user)).thenReturn(Optional.of(verificationToken));

        // Act
        boolean result = verifyTokenService.verifyUser(TestData.TOKEN, TestData.EXIST_EMAIL);

        // Assert
        assertFalse(result);
    }

    @Test
    void verifyUser_ByEmail_ValidToken_ReturnsTrue() {

        // Given
        User user = new User();
        when(userRepositoryMock.findByEmail(TestData.EXIST_EMAIL)).thenReturn(Optional.of(user));
        when(userRepositoryMock.findByUsername(anyString())).thenReturn(Optional.of(user));

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setExpiryDate(Instant.now().plusMillis(3600000));
        verificationToken.setToken(TestData.TOKEN);
        when(verificationTokenRepositoryMock.findByUser(user)).thenReturn(Optional.of(verificationToken));

        // When
        boolean result = verifyTokenService.verifyUser(TestData.TOKEN, TestData.EXIST_EMAIL);

        // Then
        assertTrue(result);
        verify(userRepositoryMock, times(1)).findByEmail(TestData.EXIST_EMAIL);
    }

    @Test
    void verifyUser_ByUsername_ValidToken_ReturnsTrue() {

        // Given
        User user = new User();
        when(userRepositoryMock.findByUsername(TestData.EXIST_USERNAME)).thenReturn(Optional.of(user));
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(Optional.of(user));

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setExpiryDate(Instant.now().plusMillis(3600000));
        verificationToken.setToken(TestData.TOKEN);
        when(verificationTokenRepositoryMock.findByUser(user)).thenReturn(Optional.of(verificationToken));

        // When
        boolean result = verifyTokenService.verifyUser(TestData.TOKEN, TestData.EXIST_USERNAME);

        // Then
        assertTrue(result);
        verify(userRepositoryMock, times(1)).findByUsername(TestData.EXIST_USERNAME);
    }
}
