package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.model.Quiz;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.request.RecordItemRequest;
import com.khai.quizguru.payload.request.RecordRequest;
import com.khai.quizguru.repository.*;
import com.khai.quizguru.utils.TestData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class RecordServiceImplTest {

    @InjectMocks
    private RecordServiceImpl recordService;

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Test
    void createRecord_UserNotFound_ThrowsResourceNotFoundException() {

        // Given
        String userId = TestData.USER_ID;
        RecordRequest recordRequest = new RecordRequest();
        recordRequest.setQuizId(TestData.QUIZ_ID);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> recordService.createRecord(recordRequest, userId));
    }

    @Test
    void createRecord_QuizNotFound_ThrowsResourceNotFoundException() {

        // Given
        String userId = TestData.USER_ID;
        RecordRequest recordRequest = new RecordRequest();
        recordRequest.setQuizId(TestData.QUIZ_ID);

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(quizRepository.findById(TestData.QUIZ_ID)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> recordService.createRecord(recordRequest, userId));
    }

    @Test
    void createRecord_QuestionNotFound_ThrowsInvalidRequestException() {
        // Given
        String userId = TestData.USER_ID;
        RecordRequest recordRequest = new RecordRequest();
        recordRequest.setQuizId(TestData.QUIZ_ID);
        recordRequest.setRecordItems(List.of(new RecordItemRequest()));

        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(quizRepository.findById(TestData.QUIZ_ID)).thenReturn(Optional.of(new Quiz()));
        when(questionRepository.findById(TestData.QUESTION_ID)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(InvalidRequestException.class, () -> recordService.createRecord(recordRequest, userId));
    }

    @Test
    void findAllRecordsByUserId_UserNotFound_ThrowsResourceNotFoundException() {
        // Given
        String userId = TestData.USER_ID;
        Pageable pageable = mock(Pageable.class);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> recordService.findAllRecordsByUserId(userId, pageable));
    }

    @Test
    void findById_RecordNotFound_ThrowsResourceNotFoundException() {
        // Given
        String recordId = TestData.RECORD_ID;
        when(recordRepository.findById(recordId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> recordService.findById(recordId));
    }
}
