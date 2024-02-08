package com.khai.quizguru;

import com.khai.quizguru.enums.RoleName;
import com.khai.quizguru.model.Image;
import com.khai.quizguru.model.user.Role;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.repository.ImageRepository;
import com.khai.quizguru.repository.RoleRepository;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.utils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class QuestionGuruApplicationTests {


}
