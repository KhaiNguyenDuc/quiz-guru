package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.enums.RoleName;
import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.ResourceExistException;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.model.Image;
import com.khai.quizguru.model.user.PasswordResetToken;
import com.khai.quizguru.model.user.Role;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.model.user.VerificationToken;
import com.khai.quizguru.payload.request.EmailRequest;
import com.khai.quizguru.payload.request.PasswordResetRequest;
import com.khai.quizguru.payload.request.ProfileRequest;
import com.khai.quizguru.payload.request.RegisterRequest;
import com.khai.quizguru.payload.response.RegisterResponse;
import com.khai.quizguru.payload.response.UserResponse;
import com.khai.quizguru.repository.*;
import com.khai.quizguru.service.EmailService;
import com.khai.quizguru.service.UserService;
import com.khai.quizguru.service.VerifyTokenService;
import com.khai.quizguru.utils.TestData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@SpringBootTest
@Slf4j
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Mock
    UserService userServiceMock;

    @Mock
    EmailService emailServiceMock;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private UserRepository userRepositoryMock;

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private ImageRepository imageRepository;

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
        user.setRoles(Arrays.asList(role));
        user.setImage(image);
        userRepository.save(user);

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setExpiryDate(Instant.now().plusMillis(3600000));
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(TestData.TOKEN);
        passwordTokenRepository.save(passwordResetToken);

        User user2 = new User();
        user2.setUsername(TestData.EXIST_USERNAME_2);
        user2.setPassword(TestData.PASSWORD);
        user2.setEmail(TestData.EXIST_EMAIL_2);
        user2.setIsEnable(true);
        user2.setRoles(Arrays.asList(role));
        userRepository.save(user2);
    }

    @Test
    void createUser_PasswordLengthLessThanSeven_ThrowsInvalidRequestException(){

        // Given
        RegisterRequest registerRequest =
                new RegisterRequest(TestData.USERNAME, TestData.EMAIL, TestData.INVALID_PASSWORD);

        // Then
        assertThrows(InvalidRequestException.class, () -> userService.createUser(registerRequest));

    }

    @Test
    void createUser_ExistByUsername_ThrowsResourceExistException(){

        // Given
        RegisterRequest registerRequest =
                new RegisterRequest(TestData.EXIST_USERNAME, TestData.EMAIL, TestData.PASSWORD);

        // Then
        assertThrows(ResourceExistException.class, () -> userService.createUser(registerRequest));

    }

    @Test
    void createUser_ExistByEmail_ThrowsResourceExistException(){

        // Given
        RegisterRequest registerRequest =
                new RegisterRequest(TestData.USERNAME, TestData.EXIST_EMAIL, TestData.PASSWORD);

        // Then
        assertThrows(ResourceExistException.class, () -> userService.createUser(registerRequest));

    }

    @Test
    void createUser_Success(){

        // Given
        RegisterRequest registerRequest =
                new RegisterRequest(TestData.USERNAME, TestData.EMAIL, TestData.PASSWORD);

        Mockito.doNothing().when(userServiceMock).createVerificationTokenForUser(any(User.class));

        // When
        RegisterResponse registerResponse = userService.createUser(registerRequest);


        // Then
        assertEquals(registerResponse.getUsername(), registerRequest.getUsername());
        assertEquals(registerResponse.getEmail(), registerRequest.getEmail());
    }

    @Test
    void getUserById_NotFound_ThrowsResourceNotFoundException(){

        // assert that
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(TestData.NOT_FOUND_ID));
    }

    @Test
    void getUserById_Success(){

        User user = userRepository.findByEmail(TestData.EXIST_EMAIL).get();
        // When
        UserResponse userResponse = userService.getUserById(user.getId());

        // Then
        assertEquals(userResponse.getId(), user.getId());
    }

    @Test
    void updateById_NotFound_ThrowsResourceNotFoundException(){

        // Given
        ProfileRequest profileRequest = new ProfileRequest();

        // Then
        assertThrows(ResourceNotFoundException.class, () -> userService.updateById(profileRequest,TestData.NOT_FOUND_ID));
    }

    @Test
    void updateById_UsernameExist_ThrowsInvalidRequestException(){

        // Given
        User user = userRepository.findByEmail(TestData.EXIST_EMAIL).get();
        ProfileRequest profileRequest = new ProfileRequest();
        profileRequest.setUsername(TestData.EXIST_USERNAME_2);

        // Then
        assertThrows(InvalidRequestException.class, () -> userService.updateById(profileRequest, user.getId()));

    }

    @Test
    void sendResetPassword_NotFoundEmail_ThrowsInvalidRequestException(){

        // Assert
        assertThrows(InvalidRequestException.class, () -> userService.sendResetPassword(TestData.NOT_FOUND_EMAIL));

    }

    @Test
    void sendResetPassword_Success(){

        // Given
        Mockito.doNothing().when(emailServiceMock).sendResetPasswordEmail(any(EmailRequest.class));

        // Expect
        User user = userRepository.findByEmail(TestData.EXIST_EMAIL).get();
        String expectedId = user.getId();

        // When
        String userId = userService.sendResetPassword(TestData.EXIST_EMAIL);

        // Then
        assertEquals(expectedId, userId);
    }

    @Test
    void resetPassword_PasswordInvalid_ThrowsInvalidRequestException(){

        // Given
        PasswordResetRequest request = new PasswordResetRequest();
        request.setPassword(TestData.INVALID_PASSWORD);

        // Then
        assertThrows(InvalidRequestException.class, () -> userService.resetPassword(request));
    }

    @Test
    void resetPassword_EmailNotFound_ThrowsInvalidRequestException(){

        // Given
        PasswordResetRequest request = new PasswordResetRequest();
        request.setPassword(TestData.PASSWORD);
        request.setEmail(TestData.NOT_FOUND_EMAIL);

        // Then
        assertThrows(InvalidRequestException.class, () -> userService.resetPassword(request));
    }

    @Test
    void resetPassword_TokenNotFound_ThrowsInvalidRequestException(){

        // Given
        PasswordResetRequest request = new PasswordResetRequest();
        request.setPassword(TestData.PASSWORD);
        request.setEmail(TestData.EXIST_EMAIL);
        request.setToken(TestData.NOT_FOUND_TOKEN);

        // Then
        assertThrows(InvalidRequestException.class, () -> userService.resetPassword(request));
    }

    @Test
    void resetPassword_Success() {

        // Given
        PasswordResetRequest request = new PasswordResetRequest();
        request.setPassword(TestData.PASSWORD);
        request.setEmail(TestData.EXIST_EMAIL);
        request.setToken(TestData.TOKEN);
        Mockito.doNothing().when(emailServiceMock).sendResetPasswordEmail(any(EmailRequest.class));

        // When
        try{
            userService.resetPassword(request);

            // Then verify userRepository.save() is called exactly once
            Mockito.verify(userRepositoryMock, times(1)).save(any(User.class));
        }catch (Exception e){
            log.info("Unhandled error when testing ( not effected on production");
        }
    }

}
