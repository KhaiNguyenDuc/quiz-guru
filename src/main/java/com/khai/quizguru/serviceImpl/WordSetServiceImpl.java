package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.exception.UnauthorizedException;
import com.khai.quizguru.model.Library;
import com.khai.quizguru.model.Quiz;
import com.khai.quizguru.model.Word;
import com.khai.quizguru.model.WordSet;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.request.Prompt.VocabularyRequest;
import com.khai.quizguru.payload.request.Prompt.hasVocabulary;
import com.khai.quizguru.payload.request.WordRequest;
import com.khai.quizguru.payload.request.WordSetRequest;
import com.khai.quizguru.payload.response.JsonPageResponse;
import com.khai.quizguru.payload.response.RecordResponse;
import com.khai.quizguru.payload.response.WordResponse;
import com.khai.quizguru.payload.response.WordSetResponse;
import com.khai.quizguru.repository.*;
import com.khai.quizguru.service.WordSetService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordSetServiceImpl implements WordSetService {
    private final WordSetRepository wordSetRepository;
    private final LibraryRepository libraryRepository;
    private final UserRepository userRepository;
    private final WordRepository wordRepository;
    private final QuizRepository quizRepository;
    private final ModelMapper mapper;
    @Override
    public String createWordSet(WordSetRequest wordSetRequest, String userId) {

        Optional<User> userOtp = userRepository.findById(userId);
        if(userOtp.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }
        Optional<Library> libraryOpt = libraryRepository.findByUser(userOtp.get());
        if(libraryOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        Library library = libraryOpt.get();
        library.setUser(userOtp.get());

        WordSet wordSet = new WordSet();

        if(wordSetRequest.getQuizId() != null){
            Optional<Quiz> quizOpt = quizRepository.findById(wordSetRequest.getQuizId());

            if(quizOpt.isPresent()){
                Quiz quiz = quizOpt.get();
                wordSet.setQuiz(quiz);
            }
        }


        wordSet.setLibrary(library);

        wordSet.setName(wordSetRequest.getName());
        wordSet.setWordNumber(wordSetRequest.getWords().size());
        WordSet wordSetSaved = wordSetRepository.save(wordSet);
        List<Word> words = new ArrayList<>();
        for(WordRequest wordRequest : wordSetRequest.getWords()){
            Word word = new Word();
            word.setName(wordRequest.getName());
            word.setDefinition(wordRequest.getDefinition());
            word.setWordSet(wordSetSaved);
            words.add(word);
        }
        wordRepository.saveAll(words);

        return wordSetSaved.getId();
    }

    @Override
    public JsonPageResponse<WordSetResponse> findAllWordSetByUserId(String userId, Pageable pageable) {

        Optional<User> userOtp = userRepository.findById(userId);
        if(userOtp.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }
        Optional<Library> libraryOpt = libraryRepository.findByUser(userOtp.get());
        if(libraryOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        Library library = libraryOpt.get();



        Page<WordSet> wordSetPage = wordSetRepository.findByLibrary(library, pageable);

        for(WordSet wordSet: wordSetPage.getContent()){
            Integer reviewNumber = quizRepository.getTotalReviewTimeForWordSet(wordSet);
            wordSet.setReviewNumber(reviewNumber);
        }
        List<WordSetResponse> wordSetResponses = Arrays.asList(mapper.map(wordSetPage.getContent(), WordSetResponse[].class));
        JsonPageResponse<WordSetResponse> pageResponse = new JsonPageResponse<>();
        pageResponse.setData(wordSetResponses);
        pageResponse.setSize(pageable.getPageSize());
        pageResponse.setPage(pageable.getPageNumber());
        pageResponse.setTotalElements(wordSetPage.getNumberOfElements());
        pageResponse.setTotalPages(wordSetPage.getTotalPages());
        pageResponse.setLast(wordSetPage.isLast());
        return pageResponse;

    }

    @Override
    public JsonPageResponse<WordSetResponse> findWordsById(String wordSetId, String userId, Pageable pageable) {
        Optional<WordSet> wordSetOpt = wordSetRepository.findById(wordSetId);

        if(wordSetOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        WordSet wordSet = wordSetOpt.get();
        if(!wordSet.getLibrary().getUser().getId().equals(userId)){
            throw  new UnauthorizedException(Constant.UNAUTHORIZED_MSG);
        }
        WordSetResponse wordSetResponse = mapper.map(wordSet, WordSetResponse.class);
        Page<Word> words = wordRepository.findAllByWordSet(wordSet, pageable);
        List<WordResponse> wordResponses = Arrays.asList(mapper.map(words.getContent(), WordResponse[].class));

        wordSetResponse.setWordResponses(wordResponses);
        JsonPageResponse<WordSetResponse> pageResponse = new JsonPageResponse<>();
        pageResponse.setData(List.of(wordSetResponse));
        pageResponse.setSize(pageable.getPageSize());
        pageResponse.setPage(pageable.getPageNumber());
        pageResponse.setTotalElements(words.getNumberOfElements());
        pageResponse.setTotalPages(words.getTotalPages());
        pageResponse.setLast(words.isLast());
        return pageResponse;

    }


    @Override
    public void deleteById(String wordSetId, String userId) {
        Optional<WordSet> wordSetOpt = wordSetRepository.findById(wordSetId);

        if(wordSetOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        WordSet wordSet = wordSetOpt.get();
        if(!wordSet.getLibrary().getUser().getId().equals(userId)){
            throw new UnauthorizedException(Constant.UNAUTHORIZED_MSG);
        }

        wordSet.setIsDeleted(Boolean.TRUE);
        wordSetRepository.save(wordSet);

    }

    @Override
    public void bindQuiz(String wordSetId, String quizId, String userId) {
        Optional<WordSet> wordSetOpt = wordSetRepository.findById(wordSetId);

        if(wordSetOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        WordSet wordSet = wordSetOpt.get();

        Optional<Quiz> quizOpt = quizRepository.findById(quizId);

        if(quizOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }

        Quiz quiz = quizOpt.get();
        if(!quiz.getUser().getId().equals(userId)){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        wordSet.setQuiz(quiz);
        wordSetRepository.save(wordSet);
    }
}
