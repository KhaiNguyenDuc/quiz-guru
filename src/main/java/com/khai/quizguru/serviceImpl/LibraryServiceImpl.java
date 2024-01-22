package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.model.Library;
import com.khai.quizguru.model.Word;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.response.JsonPageResponse;
import com.khai.quizguru.payload.response.LibraryResponse;
import com.khai.quizguru.payload.response.WordResponse;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.repository.LibraryRepository;
import com.khai.quizguru.repository.WordRepository;
import com.khai.quizguru.service.LibraryService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final LibraryRepository libraryRepository;
    private final WordRepository wordRepository;

    @Override
    public JsonPageResponse<WordResponse> findLibraryByUserId(String userId, Pageable pageable) {

        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }

        Optional<Library> libraryOpt = libraryRepository.findByUser(userOpt.get());
        if(libraryOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        Library library = libraryOpt.get();
        Page<Word> words = wordRepository.findByLibrary(library, pageable);

        List<WordResponse> wordResponses = Arrays.asList(mapper.map(words.getContent(), WordResponse[].class));

        JsonPageResponse<WordResponse> pageResponse = new JsonPageResponse<>();
        pageResponse.setData(wordResponses);
        pageResponse.setSize(pageable.getPageSize());
        pageResponse.setPage(pageable.getPageNumber());
        pageResponse.setTotalElements(words.getNumberOfElements());
        pageResponse.setTotalPages(words.getTotalPages());
        pageResponse.setLast(words.isLast());

        return pageResponse;
    }
}
