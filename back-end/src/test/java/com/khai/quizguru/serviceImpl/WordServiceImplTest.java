package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.request.WordRequest;
import com.khai.quizguru.payload.response.WordResponse;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.repository.WordRepository;
import com.khai.quizguru.utils.TestData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class WordServiceImplTest {


    @Mock
    private WordRepository wordRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WordServiceImpl wordService;

    @Test
    void findDefinition_UserNotFound_ThrowsResourceNotFoundException() {
        // When
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () ->
                wordService.findDefinition(TestData.WORDSET_ID, TestData.LIST_WORDS, TestData.USER_ID));
    }

    @Test
    void findDefinition_WordNotFound_ReturnEmptyArray() {
        // When
        User user = new User();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(wordRepository.findByNameAndUser(anyString(), anyString(), any(User.class))).thenReturn(Optional.empty());

        // Act
        List<WordResponse> result = wordService.findDefinition(TestData.WORDSET_ID, TestData.LIST_WORDS, TestData.USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void udpateWordDefinition_UserNotFound_ThrowsResourceNotFoundException() {
        // When
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () ->
                wordService.udpateWordDefinition(TestData.WORD_ID, new WordRequest(), TestData.USER_ID));
    }

    @Test
    void udpateWordDefinition_WordNotFound_ThrowsInvalidRequestException() {
        // When
        User user = new User();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(wordRepository.findByIdAndUser(anyString(), any(User.class))).thenReturn(Optional.empty());

        // Then
        assertThrows(InvalidRequestException.class, () ->
                wordService.udpateWordDefinition(TestData.WORD_ID, new WordRequest(), TestData.USER_ID));
    }

}
