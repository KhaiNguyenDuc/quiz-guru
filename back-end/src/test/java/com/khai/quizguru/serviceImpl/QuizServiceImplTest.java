package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.exception.UnauthorizedException;
import com.khai.quizguru.model.Quiz;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.repository.QuizRepository;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.utils.TestData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class QuizServiceImplTest {

    @InjectMocks
    private QuizServiceImpl quizService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void deleteById_UserNotFound_ThrowsInvalidRequestException() {

        // Given
        String userId = TestData.USER_ID;
        String quizId = TestData.QUIZ_ID;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(InvalidRequestException.class, () -> quizService.deleteById(quizId, userId));
    }

    @Test
    void deleteById_QuizNotFound_ThrowsInvalidRequestException() {

        // Given
        String userId = TestData.USER_ID;
        String quizId = TestData.QUIZ_ID;
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(InvalidRequestException.class, () -> quizService.deleteById(quizId, userId));
    }

    @Test
    void deleteById_UnauthorizedUser_ThrowsUnauthorizedException() {

        // Given
        String userId = TestData.USER_ID;
        String quizId = TestData.QUIZ_ID;
        User user = new User();
        user.setId("user2");
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setUser(user);
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        // When / Then
        assertThrows(UnauthorizedException.class, () -> quizService.deleteById(quizId, userId));
    }

    @Test
    void deleteById_SuccessfulDeletion_NoExceptionThrown() {

        // Given
        String userId = TestData.USER_ID;
        String quizId = TestData.QUIZ_ID;
        User user = new User();
        user.setId(userId);
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setUser(user);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        // When / Then
        assertDoesNotThrow(() -> quizService.deleteById(quizId, userId));
    }

    @Test
    void findAllByUserId_UserNotFound_ThrowsResourceNotFoundException() {
        // Given
        String userId = TestData.USER_ID;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> quizService.findAllByUserId(userId, Pageable.unpaged()));
    }


}
