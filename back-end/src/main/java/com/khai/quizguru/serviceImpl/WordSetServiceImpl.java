    package com.khai.quizguru.serviceImpl;

    import com.khai.quizguru.exception.InvalidRequestException;
    import com.khai.quizguru.exception.ResourceNotFoundException;
    import com.khai.quizguru.exception.UnauthorizedException;
    import com.khai.quizguru.model.Library;
    import com.khai.quizguru.model.Quiz;
    import com.khai.quizguru.model.Word;
    import com.khai.quizguru.model.WordSet;
    import com.khai.quizguru.model.user.User;
    import com.khai.quizguru.payload.request.WordRequest;
    import com.khai.quizguru.payload.request.WordSetRequest;
    import com.khai.quizguru.payload.response.JsonPageResponse;
    import com.khai.quizguru.payload.response.WordResponse;
    import com.khai.quizguru.payload.response.WordSetResponse;
    import com.khai.quizguru.repository.*;
    import com.khai.quizguru.service.WordSetService;
    import com.khai.quizguru.utils.Constant;
    import lombok.RequiredArgsConstructor;
    import org.modelmapper.ModelMapper;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;

    import java.util.*;

    /**
     * Service implementation for managing word sets.
     */
    @Service
    @RequiredArgsConstructor
    public class WordSetServiceImpl implements WordSetService {
        private final WordSetRepository wordSetRepository;
        private final LibraryRepository libraryRepository;
        private final UserRepository userRepository;
        private final WordRepository wordRepository;
        private final QuizRepository quizRepository;
        private final ModelMapper mapper;

        /**
         * Creates a new word set.
         *
         * @param wordSetRequest The word set request containing the details of the word set to be created.
         * @param userId         The ID of the user creating the word set.
         * @return The ID of the newly created word set.
         * @throws ResourceNotFoundException If the user or library is not found in the database.
         * @throws InvalidRequestException   If an invalid request is made.
         */
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
            if(Objects.isNull(wordSetRequest.getWords())){
                throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
            }
            Library library = libraryOpt.get();
            library.setUser(userOtp.get());

            WordSet wordSet = new WordSet();
            wordSet.setName(wordSetRequest.getName());
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
                if(Objects.nonNull(wordRequest.getName())){
                    word.setName(wordRequest.getName().toLowerCase().trim());
                }
                if(Objects.nonNull(wordRequest.getDefinition())){
                    word.setDefinition(wordRequest.getDefinition());
                }
                word.setWordSet(wordSetSaved);
                words.add(word);
            }
            wordRepository.saveAll(words);

            return wordSetSaved.getId();
        }

        /**
         * Finds all word sets belonging to a user.
         *
         * @param userId   The ID of the user.
         * @param pageable The pagination information.
         * @return A JSON page response containing a list of word set responses.
         * @throws ResourceNotFoundException If the user or library is not found in the database.
         * @throws InvalidRequestException   If an invalid request is made.
         */
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

        /**
         * Finds words in a word set by its ID.
         *
         * @param wordSetId The ID of the word set.
         * @param userId    The ID of the user.
         * @param pageable  The pagination information.
         * @return A JSON page response containing the word set and its words.
         * @throws InvalidRequestException If an invalid request is made.
         * @throws UnauthorizedException   If the user is not authorized to access the word set.
         */
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

            wordSetResponse.setWords(wordResponses);
            JsonPageResponse<WordSetResponse> pageResponse = new JsonPageResponse<>();
            pageResponse.setData(List.of(wordSetResponse));
            pageResponse.setSize(pageable.getPageSize());
            pageResponse.setPage(pageable.getPageNumber());
            pageResponse.setTotalElements(words.getNumberOfElements());
            pageResponse.setTotalPages(words.getTotalPages());
            pageResponse.setLast(words.isLast());
            return pageResponse;

        }

        /**
         * Deletes a word set by its ID.
         *
         * @param wordSetId The ID of the word set.
         * @param userId    The ID of the user.
         * @throws InvalidRequestException If an invalid request is made.
         * @throws UnauthorizedException   If the user is not authorized to delete the word set.
         */
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

        /**
         * Binds a quiz to a word set.
         *
         * @param wordSetId The ID of the word set.
         * @param quizId    The ID of the quiz.
         * @param userId    The ID of the user.
         * @throws InvalidRequestException If an invalid request is made.
         */
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

        /**
         * Adds words to a word set.
         *
         * @param wordSetId      The ID of the word set.
         * @param wordSetRequest The word set request containing the words to be added.
         * @throws InvalidRequestException If an invalid request is made.
         */
        @Override
        public void addWordToWordSet(String wordSetId, WordSetRequest wordSetRequest) {
            Optional<WordSet> wordSetOpt = wordSetRepository.findById(wordSetId);

            if(wordSetOpt.isEmpty()){
                throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
            }
            WordSet wordSet = wordSetOpt.get();

            List<Word> words = new ArrayList<>();
            for(WordRequest wordRequest : wordSetRequest.getWords()){
                if(!wordRepository.existsByNameAndWordSet(wordRequest.getName(), wordSet)){
                    Word word = new Word();
                    word.setName(wordRequest.getName());
                    word.setDefinition(wordRequest.getDefinition());
                    word.setWordSet(wordSet);
                    words.add(word);
                }
            }
            wordRepository.saveAll(words);

            wordSet.setWordNumber(wordSet.getWordNumber() + words.size());
            wordSetRepository.save(wordSet);
        }

        /**
         * Updates a word set.
         *
         * @param wordSetRequest The updated word set request.
         * @param wordSetId      The ID of the word set.
         * @param userId         The ID of the user.
         * @return The updated word set response.
         * @throws InvalidRequestException If an invalid request is made.
         */
        @Override
        public WordSetResponse updateWordSet(WordSetRequest wordSetRequest, String wordSetId, String userId) {
            Optional<WordSet> wordSetOpt = wordSetRepository.findById(wordSetId);

            if(wordSetOpt.isEmpty()){
                throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
            }
            WordSet wordSet = wordSetOpt.get();

            wordSet.setName(wordSetRequest.getName());
            WordSet wordSetSaved = wordSetRepository.save(wordSet);
            return mapper.map(wordSetSaved, WordSetResponse.class);
        }
    }
