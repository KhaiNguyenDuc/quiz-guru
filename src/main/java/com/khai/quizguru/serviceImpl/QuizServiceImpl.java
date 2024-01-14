package com.khai.quizguru.serviceImpl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.khai.quizguru.Exception.InternalErrorException;
import com.khai.quizguru.Exception.ResourceNotFoundException;
import com.khai.quizguru.dto.ChatRequest;
import com.khai.quizguru.dto.ChatResponse;
import com.khai.quizguru.dto.Message;
import com.khai.quizguru.dto.QuestionMixIn;
import com.khai.quizguru.model.Choice;
import com.khai.quizguru.model.Question;
import com.khai.quizguru.model.Quiz;
import com.khai.quizguru.model.User.User;
import com.khai.quizguru.payload.response.QuizResponse;
import com.khai.quizguru.repository.ChoiceRepository;
import com.khai.quizguru.repository.QuestionRepository;
import com.khai.quizguru.repository.QuizRepository;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.security.UserPrincipal;
import com.khai.quizguru.service.QuizService;
import com.khai.quizguru.utils.Constant;
import com.khai.quizguru.utils.Prompt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Override
    public QuizResponse findById(String id) {
        Optional<Quiz> quizOtp = quizRepository.findById(id);
        if(quizOtp.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }

        return mapper.map(quizOtp.get(), QuizResponse.class);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String generateQuiz(ChatRequest chat, String userId) {

        ChatResponse chatResponse = restTemplate.postForObject(apiURL, chat, ChatResponse.class);

        if(Objects.isNull(chatResponse)){
            throw new InternalErrorException(Constant.INTERNAL_ERROR_EXCEPTION_MSG);
        }
        log.info(chatResponse.toString());
        String stringResponse = chatResponse.getChoices().get(0).getMessage().getContent();


        try {
            // Parse the string to JSON
            JsonNode jsonNode = objMapper.readTree(stringResponse);

            // Extract the "questions" array from the JSON response
            JsonNode questionsNode = jsonNode.get("questions");

            List<Question> questions = new ArrayList<>();
            // Iterate through each question in the array
            for (JsonNode questionNode : questionsNode) {
                // Deserialize the Question, excluding the choices
                objMapper.addMixIn(Question.class, QuestionMixIn.class);
                Question question = objMapper.treeToValue(questionNode, Question.class);

                // Manually process the choices
                List<Choice> choices = new ArrayList<>();

                for (JsonNode choiceNode : questionNode.get("choices")) {
                    // Assuming Choice has a constructor that takes a string
                    Choice choice = new Choice();
                    choice.setName(choiceNode.asText());
                    choice.setQuestion(question);
                    choices.add(choice);
                }


                question.setChoices(choices);
                // Set the answer for each question
                question.setAnswer(questionNode.get("answer").asInt());

                questions.add(question);
            }

            // Create quiz

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
            quiz.setType(chat.getPromptRequest().getQuestionType());
            Quiz quizSaved = quizRepository.save(quiz);

            // Create questions
            questions.forEach(question -> question.setQuiz(quizSaved));
            List<Question> savedQuestions = questionRepository.saveAll(questions);

            // Create choice
            List<Choice> allChoices = new ArrayList<>();
            savedQuestions.forEach(savedQuestion -> allChoices.addAll(savedQuestion.getChoices()));
            choiceRepository.saveAll(allChoices);


            return quizSaved.getId();



        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalErrorException(Constant.INTERNAL_ERROR_EXCEPTION_MSG);
        }
    }

    @Override
    public List<QuizResponse> findAllByUserId(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }

        List<Quiz> quizzes = quizRepository.findAllByUser(userOpt.get());


        return Arrays.asList(mapper.map(quizzes, QuizResponse[].class));
    }


}
