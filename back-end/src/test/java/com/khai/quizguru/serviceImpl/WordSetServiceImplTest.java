package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.model.Quiz;
import com.khai.quizguru.model.Word;
import com.khai.quizguru.model.WordSet;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.request.WordRequest;
import com.khai.quizguru.payload.request.WordSetRequest;
import com.khai.quizguru.repository.*;
import com.khai.quizguru.utils.TestData;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@TestPropertySource(locations = "classpath:application-test.properties")
class WordSetServiceImplTest {


    @InjectMocks
    private WordSetServiceImpl wordSetService;

    @Mock
    private WordSetRepository wordSetRepository;

    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private WordRepository wordRepository;

    @Test
    void createWordSet_UserNotFound_ThrowsResourceNotFoundException() {
        // Given
        String userId = TestData.USER_ID;
        WordSetRequest wordSetRequest = new WordSetRequest();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> wordSetService.createWordSet(wordSetRequest, userId));
    }

    @Test
    void findAllWordSetByUserId_UserNotFound_ThrowsResourceNotFoundException() {
        // Given
        String userId = TestData.USER_ID;
        Pageable pageable = mock(Pageable.class);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> wordSetService.findAllWordSetByUserId(userId, pageable));
    }

    @Test
    void findAllWordSetByUserId_LibraryNotFound_ThrowsInvalidRequestException() {
        // Given
        String userId = TestData.USER_ID;
        Pageable pageable = mock(Pageable.class);
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(libraryRepository.findByUser(user)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(InvalidRequestException.class, () -> wordSetService.findAllWordSetByUserId(userId, pageable));
    }

    @Test
    void findWordsById_WordSetNotFound_ThrowsInvalidRequestException() {
        // Given
        String wordSetId = TestData.WORDSET_ID;
        String userId = TestData.USER_ID;
        Pageable pageable = mock(Pageable.class);
        when(wordSetRepository.findById(wordSetId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(InvalidRequestException.class, () -> wordSetService.findWordsById(wordSetId, userId, pageable));
    }

    @Test
    void bindQuiz_Success() {
        // Given
        String wordSetId = TestData.WORDSET_ID;
        String quizId = TestData.QUIZ_ID;
        String userId = TestData.USER_ID;

        WordSet wordSet = new WordSet();
        wordSet.setId(wordSetId);
        when(wordSetRepository.findById(wordSetId)).thenReturn(Optional.of(wordSet));

        Quiz quiz = new Quiz();
        quiz.setId(quizId);

        User user = new User();
        user.setId(userId);
        quiz.setUser(user);
        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        // When
        wordSetService.bindQuiz(wordSetId, quizId, userId);

        // Then
        assertEquals(quiz, wordSet.getQuiz());
        verify(wordSetRepository).save(wordSet);
    }

    @Test
    void bindQuiz_WordSetNotFound_ThrowsInvalidRequestException() {

        // Given
        String wordSetId = TestData.WORDSET_ID;
        String quizId = TestData.QUIZ_ID;
        String userId = TestData.USER_ID;
        when(wordSetRepository.findById(wordSetId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(InvalidRequestException.class, () -> wordSetService.bindQuiz(wordSetId, quizId, userId));
        verifyNoInteractions(quizRepository);
    }

    @Test
    void bindQuiz_QuizNotFound_ThrowsInvalidRequestException() {

        // Given
        String wordSetId = TestData.WORDSET_ID;
        String quizId = TestData.QUIZ_ID;
        String userId = TestData.USER_ID;
        WordSet wordSet = new WordSet();
        wordSet.setId(wordSetId);
        when(wordSetRepository.findById(wordSetId)).thenReturn(Optional.of(wordSet));
        when(quizRepository.findById(quizId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(InvalidRequestException.class, () -> wordSetService.bindQuiz(wordSetId, quizId, userId));
        assertNull(wordSet.getQuiz());
        verify(wordSetRepository).findById(wordSetId);
        verify(quizRepository).findById(quizId);
        verifyNoMoreInteractions(wordSetRepository);
    }

    @Test
    void updateWordSet_WordSetNotFound_ThrowsInvalidRequestException() {

        // Given
        String wordSetId = TestData.WORDSET_ID;
        String userId = TestData.USER_ID;
        WordSetRequest wordSetRequest = new WordSetRequest();
        when(wordSetRepository.findById(wordSetId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(InvalidRequestException.class, () -> wordSetService.updateWordSet(wordSetRequest, wordSetId, userId));
        verify(wordSetRepository).findById(wordSetId);
        verifyNoMoreInteractions(wordSetRepository);
    }


    @Test
    void addWordToWordSet_Success() {
        // Given
        String wordSetId = TestData.WORDSET_ID;
        
        
        WordSetRequest wordSetRequest = new WordSetRequest();
        wordSetRequest.setWords(List.of(
                new WordRequest(),
                new WordRequest()
        ));

        WordSet existingWordSet = new WordSet();
        existingWordSet.setId(wordSetId);
        when(wordSetRepository.findById(wordSetId)).thenReturn(Optional.of(existingWordSet));
        when(wordRepository.existsByNameAndWordSet(anyString(), any(WordSet.class))).thenReturn(false);

        // When
        wordSetService.addWordToWordSet(wordSetId, wordSetRequest);

        // Then
        verify(wordRepository, times(2)).save(any(Word.class));
        verify(wordSetRepository).save(existingWordSet);
    }

}
