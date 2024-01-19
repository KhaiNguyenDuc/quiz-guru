package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.enums.QuizType;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.model.*;
import com.khai.quizguru.model.Record;
import com.khai.quizguru.model.question.Question;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.request.RecordRequest;
import com.khai.quizguru.payload.response.JsonPageResponse;
import com.khai.quizguru.payload.response.QuizResponse;
import com.khai.quizguru.payload.response.RecordResponse;
import com.khai.quizguru.repository.*;
import com.khai.quizguru.service.RecordService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final RecordItemRepository recordItemRepository;
    private final ChoiceRepository choiceRepository;
    private final ModelMapper mapper;
    private final QuestionRepository questionRepository;
    @Override
    public JsonPageResponse<RecordResponse> findAllRecordsByUserId(String userId, Pageable pageable) {

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }
        Page<Record> records = recordRepository.findAllByUser(user.get(), pageable);

        List<RecordResponse> recordResponses = Arrays.asList(mapper.map(records.getContent(), RecordResponse[].class));
        JsonPageResponse<RecordResponse> pageResponse = new JsonPageResponse<>();
        pageResponse.setData(recordResponses);
        pageResponse.setSize(pageable.getPageSize());
        pageResponse.setPage(pageable.getPageNumber());
        pageResponse.setTotalElements(records.getNumberOfElements());
        pageResponse.setTotalPages(records.getTotalPages());
        pageResponse.setLast(records.isLast());
        return pageResponse;

    }
    public Record mapRecordRequestToRecord(RecordRequest recordRequest, QuizType quizType) {
        Record record = new Record();
        AtomicInteger score = new AtomicInteger();
        List<RecordItem> recordItems = recordRequest.getRecordItems().stream().map(recordItemRequest -> {

            RecordItem recordItem = new RecordItem();
            Optional<Question> questionOpt = questionRepository.findById(recordItemRequest.getQuestionId());
            questionOpt.ifPresent(recordItem::setQuestion);
            List<Choice> choices = choiceRepository.findAllById(recordItemRequest.getSelectedChoiceIds());
            recordItem.setSelectedChoices(choices);

            if (quizType == QuizType.MULTIPLE_CHOICE_QUESTION && !choices.isEmpty()) {
                boolean allCorrect = choices.stream()
                        .allMatch(Choice::getIsCorrect);
                if (allCorrect) {
                    score.addAndGet(1); // Increment score only if all selected choices are correct
                }
            } else if (quizType == QuizType.SINGLE_CHOICE_QUESTION && !choices.isEmpty()) {
                for (Choice item : choices) {
                    if (item.getIsCorrect()) {
                        score.addAndGet(1); // Increment score for each correct choice
                        break; // Break the loop as only one choice can be correct in single-choice questions
                    }
                }
            }

            return recordItem;
        }).collect(Collectors.toList());
        record.setScore(score.get());
        record.setRecordItems(recordItems);
        record.setTimeLeft(recordRequest.getTimeLeft());
        return record;
    }

    @Override
    public RecordResponse createRecord(RecordRequest recordRequest, String userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }

        Optional<Quiz> quiz = quizRepository.findById(recordRequest.getQuizId());
        if(quiz.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }

        Record record = mapRecordRequestToRecord(recordRequest, quiz.get().getType());
        record.setQuiz(quiz.get());
        record.setUser(user.get());
        record.setDuration(quiz.get().getDuration());
        List<String> choiceIds = recordRequest.getRecordItems().stream()
                .flatMap(recordItemRequest -> recordItemRequest.getSelectedChoiceIds().stream())
                .toList();

        List<Choice> choices = new ArrayList<>();
        for(String choiceId : choiceIds){

            if(choiceId != null){
                Optional<Choice> choiceOpt = choiceRepository.findById(choiceId);
                choiceOpt.ifPresent(choices::add);
            }
        }

        List<RecordItem> recordItems = record.getRecordItems();
        recordItems.forEach(recordItem -> recordItem.setRecord(record));
        Record recordSaved = recordRepository.save(record);
        return mapper.map(recordSaved, RecordResponse.class);
    }

    @Override
    public RecordResponse findById(String recordId) {
        Optional<Record> recordOpt = recordRepository.findById(recordId);
        if(recordOpt.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }
        Record record = recordOpt.get();
        RecordResponse recordResponse = mapper.map(record, RecordResponse.class);
//        recordResponse.getRecordItems().forEach(recordItemResponse -> recordItemResponse.getQuestion().setType());
        recordResponse.setGivenText(record.getQuiz().getGivenText());
        return recordResponse;
    }
}
