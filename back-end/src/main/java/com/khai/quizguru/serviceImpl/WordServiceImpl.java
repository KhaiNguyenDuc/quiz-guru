package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.model.Word;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.request.WordRequest;
import com.khai.quizguru.payload.response.WordResponse;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.repository.WordRepository;
import com.khai.quizguru.service.WordService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Service implementation for managing words and their definitions.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WordServiceImpl implements WordService {

    @Value("${dictionary.api.url}")
    private String dictionaryUrl;

    private final RestTemplate restTemplate;
    private final WordRepository wordRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    /**
     * Finds the definitions of words in a word set.
     *
     * @param wordSetId The ID of the word set.
     * @param words     The list of words for which definitions are to be found.
     * @param userId    The ID of the user making the request.
     * @return A list of word responses containing the definitions.
     * @throws ResourceNotFoundException If the user is not found in the database.
     * @throws InvalidRequestException   If an invalid request is made.
     */
    @Override
    public List<WordResponse> findDefinition(String wordSetId ,List<String> words, String userId) {

        List<String> lowercaseWords = words.stream()
                .map(String::toLowerCase)
                .map(String::trim)  // Remove leading and trailing spaces
                .toList();
        Optional<User> userOtp = userRepository.findById(userId);
        if(userOtp.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }
        User user = userOtp.get();
        List<WordResponse> wordResponses = new ArrayList<>();
        try{

            for (String word : lowercaseWords) {
                Optional<Word> wordOpt = wordRepository.findByNameAndUser(word, wordSetId, user);
                if(wordOpt.isPresent()){
                    try{
                        Word w = wordOpt.get();
                        if(Objects.isNull(w.getDefinition())){
                            String url = String.format(dictionaryUrl, word);
                            log.info(url);
                            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
                            String responseBody = responseEntity.getBody();
                            if(Objects.nonNull(responseBody)){
                                w.setDefinition(responseBody);
                                wordRepository.save(w);
                            }

                        }
                        wordResponses.add(mapper.map(w, WordResponse.class));
                    } catch (HttpClientErrorException.NotFound e) {
                        // 404 Not Found response, ignore and continue to the next word
                        log.warn("Definition not found for word: {}", word);
                    }
                }
            }
        }catch (Exception e){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }

        return wordResponses;
    }

    /**
     * Updates the definition of a word.
     *
     * @param wordId      The ID of the word.
     * @param wordRequest The word request containing the updated content.
     * @param userId      The ID of the user making the request.
     * @return The updated word response.
     * @throws ResourceNotFoundException If the user is not found in the database.
     * @throws InvalidRequestException   If an invalid request is made.
     */
    @Override
    public WordResponse udpateWordDefinition(String wordId, WordRequest wordRequest, String userId) {
        Optional<User> userOtp = userRepository.findById(userId);
        if (userOtp.isEmpty()) {
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }

        User user = userOtp.get();

        Optional<Word> wordOpt = wordRepository.findByIdAndUser(wordId, user);
        if (wordOpt.isEmpty()) {
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        Word word = wordOpt.get();
        word.setContent(wordRequest.getContent());
        Word wordSaved = wordRepository.save(word);
        return mapper.map(wordSaved, WordResponse.class);
    }

}
