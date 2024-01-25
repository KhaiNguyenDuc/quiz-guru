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
import com.khai.quizguru.model.question.Question;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.request.Prompt.VocabularyRequest;
import com.khai.quizguru.payload.request.Prompt.hasVocabulary;
import com.khai.quizguru.payload.response.JsonPageResponse;
import com.khai.quizguru.payload.response.QuizResponse;
import com.khai.quizguru.repository.*;
import com.khai.quizguru.service.QuizService;
import com.khai.quizguru.utils.Constant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizServiceImpl implements QuizService {



    @Value(("${openai.api.url}"))
    private String apiURL;


    private final QuizRepository quizRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final RestTemplate restTemplate;
    private final ModelMapper mapper;
    private final ObjectMapper objMapper;
    private final LibraryRepository libraryRepository;
    private final WordRepository wordRepository;
    private final WordSetRepository wordSetRepository;

    @Override
    public QuizResponse findById(String id) {
        Optional<Quiz> quizOtp = quizRepository.findById(id);
        if(quizOtp.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }
        QuizResponse quizResponse = mapper.map(quizOtp.get(), QuizResponse.class);
        quizResponse.setType(quizOtp.get().getType().getValue());
        return quizResponse;

    }

    @Override
    public String generateQuiz(ChatRequest chat, String userId) {

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
            Quiz quiz = new Quiz();
            quiz.setUser(userOtp.get());
            quiz.setGivenText(chat.getGivenText());
            quiz.setLevel(chat.getPromptRequest().getLevel());
            quiz.setLanguage(chat.getPromptRequest().getLanguage());
            quiz.setNumber(chat.getPromptRequest().getNumber());
            quiz.setType(chat.getPromptRequest().getQuizType());
            quiz.setDuration(chat.getPromptRequest().getDuration());
            Quiz quizSaved = quizRepository.save(quiz);

            // Parse the string to JSON
            JsonNode jsonNode = objMapper.readTree(stringResponse);

            // Extract the "questions" array from the JSON response
            JsonNode questionsNode = jsonNode.get("questions");


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
            return quizSaved.getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
    }

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
