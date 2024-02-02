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


}
