package com.khai.quizguru.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khai.quizguru.exception.InternalErrorException;
import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.dto.ChatRequest;
import com.khai.quizguru.dto.ChatResponse;
import com.khai.quizguru.dto.QuestionMixIn;
import com.khai.quizguru.exception.UnauthorizedException;
import com.khai.quizguru.model.*;
import com.khai.quizguru.model.Question;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.request.HasHtmlContent;
import com.khai.quizguru.payload.request.QuizGenerationResult;
import com.khai.quizguru.payload.request.WordRequest;
import com.khai.quizguru.payload.request.WordSetRequest;
import com.khai.quizguru.payload.request.text.BaseTextRequest;
import com.khai.quizguru.payload.request.vocabulary.GenerateVocabularyRequest;
import com.khai.quizguru.payload.request.vocabulary.VocabularyPromptRequest;
import com.khai.quizguru.payload.response.JsonPageResponse;
import com.khai.quizguru.payload.response.QuizResponse;
import com.khai.quizguru.payload.response.WordResponse;
import com.khai.quizguru.payload.response.WordSetResponse;
import com.khai.quizguru.repository.*;
import com.khai.quizguru.service.QuizService;
import com.khai.quizguru.service.WordSetService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Service implementation for managing quizzes.
 * This service handles operations related to quiz generation, retrieval, and deletion.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {

    @Value("${openai.api.url}")
    private String apiURL;


    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final WordSetRepository wordSetRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper mapper;
    private final ObjectMapper objMapper;
    private final WordSetService wordSetService;

    /**
     * Retrieves a quiz by its unique identifier and maps it to a QuizResponse object.
     *
     * @param id The unique identifier of the quiz to retrieve.
     * @return A QuizResponse object representing the retrieved quiz.
     * @throws ResourceNotFoundException If no quiz with the specified ID is found.
     */
    @Override
    public QuizResponse findById(String id) {
        Optional<Quiz> quizOtp = quizRepository.findById(id);
        if(quizOtp.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }
        Quiz quiz = quizOtp.get();
        QuizResponse quizResponse = mapper.map(quiz, QuizResponse.class);
        if(Objects.nonNull(quiz.getWordSet())){
            WordSetResponse wordSetResponse = mapper.map(quiz.getWordSet(), WordSetResponse.class);

            List<WordResponse> wordResponses = Arrays.asList(mapper.map(quiz.getWordSet().getWords(), WordResponse[].class));
            wordSetResponse.setWords(wordResponses);
            quizResponse.setWordSet(wordSetResponse);

        }
        quizResponse.setType(quiz.getType().getValue());
        return quizResponse;

    }

    /**
     * Generates a quiz based on the provided ChatRequest, saves the associated word set if applicable,
     * and returns the result.
     *
     * @param chat   The ChatRequest containing information for quiz generation.
     * @param userId The ID of the user generating the quiz.
     * @return A QuizGenerationResult object representing the generated quiz and associated data.
     * @throws InvalidRequestException   If the request is invalid or cannot be processed.
     * @throws ResourceNotFoundException If a required resource is not found.
     */
    @Override
    public QuizGenerationResult generateQuizAndSaveWordSet(ChatRequest chat, String userId){
        QuizGenerationResult result;
        try {
            result = this.generateQuiz(chat, userId);

            WordSetRequest wordSetRequest = new WordSetRequest();
            wordSetRequest.setQuizId(result.getQuizId());
            wordSetRequest.setName("");
            ChatResponse chatResponse = result.getChatResponse();
            String stringResponse = chatResponse.getChoices().get(0).getMessage().getContent();

            // Parse the string to JSON
            JsonNode jsonNode = objMapper.readTree(stringResponse);

            // Extract the "questions" array from the JSON response
            JsonNode wordsNode = jsonNode.get("words");
            List<WordRequest> wordRequests = new ArrayList<>();
            for(JsonNode node: wordsNode){

                WordRequest wordRequest = new WordRequest();
                wordRequest.setName(node.asText());
                wordRequests.add(wordRequest);
            }
            wordSetRequest.setWords(wordRequests);

            String wordSetId = "";
            if(chat.getPromptRequest() instanceof VocabularyPromptRequest vocabularyPromptRequest){
                wordSetRequest.setName(vocabularyPromptRequest.getWordSetName());

                // Mapping exist wordSet with quiz
                if(!Objects.equals(vocabularyPromptRequest.getWordSetId(), "")){
                    wordSetId = vocabularyPromptRequest.getWordSetId();
                    wordSetService.addWordToWordSet(wordSetId, wordSetRequest);
                    wordSetService.bindQuiz(wordSetId, wordSetRequest.getQuizId(), userId);
                    //ToDo: delete old quiz due to one to one relationship
                }else{
                    if(!vocabularyPromptRequest.getWordSetName().isEmpty()){
                        // Mapping exist wordSet with quiz
                        Optional<WordSet> wordSetOpt =
                                wordSetRepository.findByNameAndUser(vocabularyPromptRequest.getWordSetName(), userId);
                        if(wordSetOpt.isPresent()){
                            wordSetId = wordSetOpt.get().getId();
                            wordSetService.addWordToWordSet(wordSetId, wordSetRequest);
                            wordSetService.bindQuiz(wordSetId, wordSetRequest.getQuizId(), userId);
                        }else{
                            // Create new wordSet
                            wordSetId = wordSetService.createWordSet(wordSetRequest, userId);
                        }
                    }
                }
            }
            result.setWordSetId(wordSetId);

        }catch (Exception e){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        return result;
    }

    /**
     * Generates a quiz based on the provided ChatRequest and user ID.
     *
     * @param chat   The ChatRequest containing information for quiz generation.
     * @param userId The ID of the user generating the quiz.
     * @return A QuizGenerationResult object representing the generated quiz and associated data.
     * @throws InvalidRequestException   If the request is invalid or cannot be processed.
     * @throws ResourceNotFoundException If a required resource is not found.
     */
    @Override
    public QuizGenerationResult generateQuiz(ChatRequest chat, String userId) {

        ChatResponse chatResponse = restTemplate.postForObject(apiURL, chat, ChatResponse.class);


        log.info(chat.getPromptRequest().generatePrompt());

        if(Objects.isNull(chatResponse)){
            throw new InternalErrorException(Constant.INTERNAL_ERROR_EXCEPTION_MSG);
        }
        log.info(chatResponse.toString());
        String stringResponse = chatResponse.getChoices().get(0).getMessage().getContent();


        try {
            Optional<User> userOtp = userRepository.findById(userId);
            if(userOtp.isEmpty()){
                throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
            }
            Quiz quiz = getQuiz(chat, userOtp);
            Quiz quizSaved = quizRepository.save(quiz);

            // Parse the string to JSON
            JsonNode jsonNode = objMapper.readTree(stringResponse);

            // Extract the "questions" array from the JSON response
            JsonNode questionsNode = jsonNode.get("questions");
            if(Objects.isNull(questionsNode)){
                throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
            }

            // Iterate through each question in the array
            for (JsonNode questionNode : questionsNode) {
                // Deserialize the Question, excluding the choices
                objMapper.addMixIn(Question.class, QuestionMixIn.class);
                Question question = objMapper.treeToValue(questionNode, Question.class);
                question.setQuiz(quiz);
                questionRepository.save(question);
                List<Choice> choices = new ArrayList<>();
                for (JsonNode choiceNode : questionNode.get("choices")) {
                    // Assuming Choice has a constructor that takes a string
                    Choice choice = new Choice();
                    choice.setQuestion(question);
                    choice.setName(choiceNode.asText());
                    choices.add(choice);
                    choiceRepository.save(choice);

                }
                // Set the answer for each question
                for(int i = 0;i<questionNode.get("answers").size(); i++){
                    question.setAnswer(questionNode.get("answers").get(i).asInt(), choices);
                }
                questionRepository.save(question);
            }

            QuizGenerationResult quizGenerationResult = new QuizGenerationResult();
            quizGenerationResult.setQuizId(quizSaved.getId());
            quizGenerationResult.setChatResponse(chatResponse);
            return quizGenerationResult;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
    }

    /**
     * Constructs a Quiz object based on the provided ChatRequest and user details.
     *
     * @param chat     The ChatRequest containing information for quiz generation.
     * @param userOtp  An Optional containing the user details.
     * @return A Quiz object representing the generated quiz.
     */
    private Quiz getQuiz(ChatRequest chat, Optional<User> userOtp) {
        Quiz quiz = new Quiz();
        quiz.setUser(userOtp.get());
        if(chat.getPromptRequest() instanceof HasHtmlContent){
            quiz.setGivenText(((HasHtmlContent) chat.getPromptRequest()).getHtmlContent());
        }else if(chat.getPromptRequest() instanceof GenerateVocabularyRequest){
            quiz.setGivenText("");
        } else{
            quiz.setGivenText(chat.getGivenText());
        }

        quiz.setLevel(chat.getPromptRequest().getLevel());
        quiz.setLanguage(chat.getPromptRequest().getLanguage());
        quiz.setNumber(chat.getPromptRequest().getNumber());
        quiz.setType(chat.getPromptRequest().getQuizType());
        quiz.setDuration(chat.getPromptRequest().getDuration());
        return quiz;
    }

    /**
     * Retrieves a page of quizzes associated with a user and maps them to QuizResponse objects.
     *
     * @param userId   The ID of the user whose quizzes are to be retrieved.
     * @param pageable Pagination information.
     * @return A JsonPageResponse containing a page of QuizResponse objects.
     * @throws ResourceNotFoundException If the user with the specified ID is not found.
     */
    @Override
    public JsonPageResponse<QuizResponse> findAllByUserId(String userId, Pageable pageable) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }

        Page<Quiz> quizzes = quizRepository.findAllByUser(userOpt.get(), pageable);
        List<QuizResponse> quizResponses =  Arrays.asList(mapper.map(quizzes.getContent(), QuizResponse[].class));

        JsonPageResponse<QuizResponse> pageResponse = new JsonPageResponse<>();
        pageResponse.setData(quizResponses);
        pageResponse.setSize(pageable.getPageSize());
        pageResponse.setPage(pageable.getPageNumber());
        pageResponse.setTotalElements(quizzes.getNumberOfElements());
        pageResponse.setTotalPages(quizzes.getTotalPages());
        pageResponse.setLast(quizzes.isLast());
        return pageResponse;
    }

    /**
     * Deletes a quiz by its ID, provided the user is authorized to do so.
     *
     * @param quizId The ID of the quiz to delete.
     * @param userId The ID of the user performing the deletion.
     * @throws InvalidRequestException   If the request is invalid or cannot be processed.
     * @throws UnauthorizedException     If the user is not authorized to perform the deletion.
     * @throws ResourceNotFoundException If the quiz with the specified ID is not found.
     */
    @Override
    public void deleteById(String quizId, String userId) {

        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }


        Optional<Quiz> quizOpt = quizRepository.findById(quizId);
        if(quizOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        Quiz quiz = quizOpt.get();
        if(!userOpt.get().equals(quiz.getUser())){
            throw new UnauthorizedException(Constant.UNAUTHORIZED_MSG);
        }
        quiz.setIsDeleted(true);
        quizRepository.save(quiz);
    }


}
