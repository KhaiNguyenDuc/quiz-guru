package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.Exception.ResourceNotFoundException;
import com.khai.quizguru.model.*;
import com.khai.quizguru.model.Record;
import com.khai.quizguru.model.User.User;
import com.khai.quizguru.payload.request.RecordItemRequest;
import com.khai.quizguru.payload.request.RecordRequest;
import com.khai.quizguru.payload.response.QuestionResponse;
import com.khai.quizguru.payload.response.RecordItemResponse;
import com.khai.quizguru.payload.response.RecordResponse;
import com.khai.quizguru.repository.*;
import com.khai.quizguru.service.RecordService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    @Override
    public List<RecordResponse> findAllRecordsByUserId(String userId) {

        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }
        List<Record> records = recordRepository.findAllByUser(user.get());
        return Arrays.asList(mapper.map(records, RecordResponse[].class));
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

        Record record = mapper.map(recordRequest, Record.class);
        record.setQuiz(quiz.get());
        record.setUser(user.get());
        List<String> choiceId = recordRequest.getRecordItems().stream()
                .map(RecordItemRequest::getSelectedChoiceId).toList();
        List<Choice> choices = choiceRepository.findAllById(choiceId);
        int score = 0;

        for (Choice item: choices) {
            if(item.getIsCorrect()){
                score = score + 1;
            }
        }
        List<RecordItem> recordItems = record.getRecordItems();
        recordItems.forEach(recordItem -> recordItem.setRecord(record));
        record.setScore(score);
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
        recordResponse.setGivenText(record.getQuiz().getGivenText());
        return recordResponse;
    }
}
