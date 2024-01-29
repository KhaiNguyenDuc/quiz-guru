package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.service.WordService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WordServiceImpl implements WordService {

    @Value("${dictionary.api.url}")
    private String dictionaryUrl;

    private final RestTemplate restTemplate;
    @Override
    public List<Object> findDefinition(List<String> words) {

        List<String> lowercaseWords = words.stream()
                .map(String::toLowerCase)
                .map(String::trim)  // Remove leading and trailing spaces
                .toList();

        List<Object> objects = new ArrayList<>();
        try{

            for (String word : lowercaseWords) {
                String url = String.format(dictionaryUrl, word);
                log.info(url);
                try {
                    Object obj = restTemplate.getForEntity(url, Object.class);
                    objects.add(obj);
                } catch (HttpClientErrorException.NotFound e) {
                    // 404 Not Found response, ignore and continue to the next word
                    log.warn("Definition not found for word: {}", word);
                }
            }
        }catch (Exception e){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }

        return objects;
    }
}
