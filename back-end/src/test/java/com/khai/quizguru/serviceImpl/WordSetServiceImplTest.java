package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.exception.UnauthorizedException;
import com.khai.quizguru.model.Library;
import com.khai.quizguru.model.Quiz;
import com.khai.quizguru.model.WordSet;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.request.WordRequest;
import com.khai.quizguru.payload.request.WordSetRequest;
import com.khai.quizguru.payload.response.WordSetResponse;
import com.khai.quizguru.repository.*;
import com.khai.quizguru.utils.TestData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import java.util.Arrays;
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
    private WordRepository wordRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private WordSetRepository wordSetRepository;

    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private UserRepository userRepository;

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
    void createWordSet_LibraryNotFound_ThrowsInvalidRequestException() {

        // Given
        String userId = TestData.USER_ID;
        WordSetRequest wordSetRequest = new WordSetRequest();
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(libraryRepository.findByUser(user)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(InvalidRequestException.class, () -> wordSetService.createWordSet(wordSetRequest, userId));
    }

    @Test
    void createWordSet_NullListWords_ThrowsInvalidRequestException() {

        // Given
        String userId = TestData.USER_ID;
        WordSetRequest wordSetRequest = new WordSetRequest();
        User user = new User();
        Library library = new Library();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(libraryRepository.findByUser(user)).thenReturn(Optional.of(library));

        // When // Then
        assertThrows(InvalidRequestException.class, () ->  wordSetService.createWordSet(wordSetRequest, userId));
    }

    @Test
    void createWordSet_WithoutQuizId_Success() {

        // Given
        String userId = TestData.USER_ID;
        String wordSetId = TestData.WORDSET_ID;
        User user = new User();
        Library library = new Library();

        WordSetRequest wordSetRequest = new WordSetRequest();
        wordSetRequest.setWords(Arrays.asList(new WordRequest(), new WordRequest()));
        WordSet wordSet = new WordSet();
        wordSet.setId(wordSetId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(libraryRepository.findByUser(user)).thenReturn(Optional.of(library));
        when(wordSetRepository.save(any(WordSet.class))).thenReturn(wordSet);
        when(wordRepository.saveAll(any())).thenReturn(null);

        // When
        String result = wordSetService.createWordSet(wordSetRequest, userId);

        // Then
        assertEquals(result, wordSet.getId());
    }

    @Test
    void createWordSet_WithQuizId_Success() {

        // Given
        String userId = TestData.USER_ID;
        String wordSetId = TestData.WORDSET_ID;
        User user = new User();
        Library library = new Library();

        WordSetRequest wordSetRequest = new WordSetRequest();
        wordSetRequest.setWords(Arrays.asList(new WordRequest(), new WordRequest()));
        wordSetRequest.setQuizId(TestData.QUIZ_ID);
        WordSet wordSet = new WordSet();
        wordSet.setId(wordSetId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(libraryRepository.findByUser(user)).thenReturn(Optional.of(library));
        when(quizRepository.findById(TestData.QUIZ_ID)).thenReturn(Optional.of(new Quiz()));
        when(wordSetRepository.save(any(WordSet.class))).thenReturn(wordSet);
        when(wordRepository.saveAll(any())).thenReturn(null);

        // When
        String result = wordSetService.createWordSet(wordSetRequest, userId);

        // Then
        assertEquals(result, wordSet.getId());
        verify(quizRepository, times(1)).findById(TestData.QUIZ_ID);
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
    void updateWordSet_Success() {

        // Given
        String userId = TestData.USER_ID;
        String wordSetId = TestData.WORDSET_ID;
        WordSetRequest wordSetRequest = new WordSetRequest();
        wordSetRequest.setName("Updated Word Set Name");

        WordSet wordSet = new WordSet();
        wordSet.setId(wordSetId);
        wordSet.setName("Old Word Set Name");

        when(wordSetRepository.findById(wordSetId)).thenReturn(Optional.of(wordSet));
        when(wordSetRepository.save(any(WordSet.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.map(any(WordSet.class), eq(WordSetResponse.class)))
                .thenAnswer(invocation -> {
                    WordSet inputWord = invocation.getArgument(0);
                    WordSetResponse wordSetResponse = new WordSetResponse();
                    wordSetResponse.setId(TestData.WORDSET_ID);
                    wordSetResponse.setName(inputWord.getName());
                    return wordSetResponse;
                });

        // When
        WordSetResponse updatedWordSetResponse = wordSetService.updateWordSet(wordSetRequest, wordSetId, userId);

        // Then
        assertNotNull(updatedWordSetResponse);
        assertEquals(wordSetRequest.getName(), updatedWordSetResponse.getName());
        assertEquals(wordSetId, updatedWordSetResponse.getId());
        verify(wordSetRepository).findById(wordSetId);
        verify(wordSetRepository).save(any(WordSet.class));
    }

    @Test
    void deleteById_WordSetNotFound_ThrowsInvalidRequestException() {
        // Given
        String wordSetId = TestData.WORDSET_ID;
        String userId = TestData.USER_ID;
        when(wordSetRepository.findById(wordSetId)).thenReturn(Optional.empty());

        // When // Then
        assertThrows(InvalidRequestException.class, () -> wordSetService.deleteById(wordSetId, userId));
        verify(wordSetRepository, never()).save(any());
    }

    @Test
    void deleteById_UnauthorizedUser_ThrowsUnauthorizedException() {

        // Given
        String wordSetId = TestData.WORDSET_ID;
        String userId = TestData.USER_ID;

        Library library = new Library();
        User user = new User();
        user.setId("differentUserId");
        library.setUser(user);

        WordSet wordSet = new WordSet();
        wordSet.setId(wordSetId);
        wordSet.setLibrary(library);

        when(wordSetRepository.findById(wordSetId)).thenReturn(Optional.of(wordSet));

        // When // Then
        assertThrows(UnauthorizedException.class, () -> wordSetService.deleteById(wordSetId, userId));
        verify(wordSetRepository, never()).save(any());
    }

    @Test
    void deleteById_Success() {

        // Given
        String wordSetId = TestData.WORDSET_ID;
        String userId = TestData.USER_ID;

        Library library = new Library();
        User user = new User();
        user.setId(userId);
        library.setUser(user);


        WordSet wordSet = new WordSet();
        wordSet.setId(wordSetId);
        wordSet.setLibrary(library);

        when(wordSetRepository.findById(wordSetId)).thenReturn(Optional.of(wordSet));
        when(wordSetRepository.save(any(WordSet.class))).thenReturn(wordSet);

        // When
        wordSetService.deleteById(wordSetId, userId);

        // Then
        assertTrue(wordSet.getIsDeleted());
        verify(wordSetRepository, times(1)).save(wordSet);
    }


}
