package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.model.Word;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.request.WordRequest;
import com.khai.quizguru.payload.response.WordResponse;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.repository.WordRepository;
import com.khai.quizguru.utils.TestData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class WordServiceImplTest {

    @Mock
    private ModelMapper mapper;

    @Mock
    private WordRepository wordRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WordServiceImpl wordService;


    @Test
    void findDefinition_UserNotFound_ThrowsResourceNotFoundException() {

        // Given
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());

        // Then
        assertThrows(ResourceNotFoundException.class, () ->
                wordService.findDefinition(TestData.WORDSET_ID, TestData.LIST_WORDS, TestData.USER_ID));
    }

    @Test
    void findDefinition_WordNotFound_ReturnEmptyArray() {

        // Given
        User user = new User();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(wordRepository.findByNameAndUser(anyString(), anyString(), any(User.class))).thenReturn(Optional.empty());

        // When
        List<WordResponse> result = wordService.findDefinition(TestData.WORDSET_ID, TestData.LIST_WORDS, TestData.USER_ID);

        // Then
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void findDefinition_WithoutExistDefinition_Success() {

        // Given
        String definition = "Definition";
        User user = new User();
        Word word = new Word();
        word.setDefinition(definition);

        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(wordRepository.findByNameAndUser(anyString(), anyString(), any(User.class))).thenReturn(Optional.of(word));

        when(mapper.map(any(Word.class), eq(WordResponse.class)))
                .thenAnswer(invocation -> {
                    Word inputWord = invocation.getArgument(0);
                    WordResponse wordResponse = new WordResponse();
                    wordResponse.setDefinition(inputWord.getDefinition());
                    return wordResponse;
                });

        // When
        List<WordResponse> result = wordService.findDefinition(TestData.WORDSET_ID, TestData.LIST_WORDS, TestData.USER_ID);

        // Then
        assertNotNull(result);
        assertEquals(result.get(0).getDefinition(), definition);
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
    void updateWordDefinition_WordNotFound_ThrowsInvalidRequestException() {

        // When
        User user = new User();
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(wordRepository.findByIdAndUser(anyString(), any(User.class))).thenReturn(Optional.empty());

        // Then
        assertThrows(InvalidRequestException.class, () ->
                wordService.udpateWordDefinition(TestData.WORD_ID, new WordRequest(), TestData.USER_ID));
    }

//    @Test
//    void updateWordDefinition_Success() {
//
//        // Given
//        String content = "content";
//        User user = new User();
//        Word word = new Word();
//        word.setContent(content);
//        WordRequest wordRequest = new WordRequest();
//        wordRequest.setContent(content);
//
//        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
//        when(wordRepository.findByIdAndUser(anyString(), any(User.class))).thenReturn(Optional.of(word));
//        when(mapper.map(any(Word.class), eq(WordResponse.class)))
//                .thenAnswer(invocation -> {
//                    Word inputWord = invocation.getArgument(0);
//                    WordResponse wordResponse = new WordResponse();
//                    wordResponse.setContent(inputWord.getContent());
//                    return new WordResponse();
//                });
//
//        // When
//        WordResponse wordResponse =
//                wordService.udpateWordDefinition(TestData.WORD_ID, wordRequest, TestData.USER_ID);
//
//        // Then
//        assertEquals(wordResponse.getContent(), content);
//    }

}
