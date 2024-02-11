package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.enums.QuestionType;
import com.khai.quizguru.enums.QuizType;
import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.model.*;
import com.khai.quizguru.model.Record;
import com.khai.quizguru.model.Question;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.request.RecordRequest;
import com.khai.quizguru.payload.response.JsonPageResponse;
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


/**
 * Implementation of the RecordService interface.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final ChoiceRepository choiceRepository;
    private final ModelMapper mapper;
    private final QuestionRepository questionRepository;

    /**
     * Retrieves a page of records associated with a user.
     *
     * @param userId   The ID of the user whose records are to be retrieved.
     * @param pageable Pagination information.
     * @return A JsonPageResponse containing a page of RecordResponse objects.
     * @throws ResourceNotFoundException If the user with the specified ID is not found.
     */
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

    /**
     * Maps a RecordRequest to a Record object.
     *
     * @param recordRequest The RecordRequest containing information about the record.
     * @param quizType      The type of the quiz associated with the record.
     * @return A Record object mapped from the RecordRequest.
     */
    public Record mapRecordRequestToRecord(RecordRequest recordRequest, QuizType quizType) {
        Record record = new Record();
        AtomicInteger score = new AtomicInteger();
        List<RecordItem> recordItems = recordRequest.getRecordItems().stream().map(recordItemRequest -> {

            RecordItem recordItem = new RecordItem();
            Optional<Question> questionOpt = questionRepository.findById(recordItemRequest.getQuestionId());
            if(questionOpt.isEmpty()){
                throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
            }
            Question question = questionOpt.get();
            recordItem.setQuestion(question);
            List<Choice> choices = choiceRepository.findAllById(recordItemRequest.getSelectedChoiceIds());
            recordItem.setSelectedChoices(choices);

            if (question.getType() == QuestionType.MULTIPLE_CHOICE && !choices.isEmpty()) {
                boolean allCorrect = choices.stream()
                        .allMatch(Choice::getIsCorrect);
                if (allCorrect) {
                    score.addAndGet(1); // Increment score only if all selected choices are correct
                }
            } else if (question.getType() == QuestionType.SINGLE_CHOICE && !choices.isEmpty()) {
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

    /**
     * Creates a new record based on the provided RecordRequest and user details.
     *
     * @param recordRequest The RecordRequest containing information about the record.
     * @param userId        The ID of the user associated with the record.
     * @return A RecordResponse object representing the newly created record.
     * @throws ResourceNotFoundException If the user or quiz with the specified ID is not found.
     */
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

    /**
     * Retrieves a record by its ID and maps it to a RecordResponse object.
     *
     * @param recordId The ID of the record to retrieve.
     * @return A RecordResponse object representing the retrieved record.
     * @throws ResourceNotFoundException If the record with the specified ID is not found.
     */
    @Override
    public RecordResponse findById(String recordId) {
        Optional<Record> recordOpt = recordRepository.findById(recordId);
        if(recordOpt.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }
        Record record = recordOpt.get();
        RecordResponse recordResponse = mapper.map(record, RecordResponse.class);
        recordResponse.setGivenText(record.getQuiz().getGivenText());
        return recordResponse;
    }
}
