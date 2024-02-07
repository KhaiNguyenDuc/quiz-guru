package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.ResourceExistException;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.model.Image;
import com.khai.quizguru.model.Library;
import com.khai.quizguru.model.user.PasswordResetToken;
import com.khai.quizguru.model.user.VerificationToken;
import com.khai.quizguru.payload.request.EmailRequest;
import com.khai.quizguru.payload.request.PasswordResetRequest;
import com.khai.quizguru.payload.request.ProfileRequest;
import com.khai.quizguru.payload.request.RegisterRequest;
import com.khai.quizguru.model.user.Role;
import com.khai.quizguru.enums.RoleName;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.response.RegisterResponse;
import com.khai.quizguru.payload.response.UserResponse;
import com.khai.quizguru.repository.*;
import com.khai.quizguru.service.EmailService;
import com.khai.quizguru.service.UserService;
import com.khai.quizguru.utils.Constant;
import com.khai.quizguru.utils.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper mapper;
    private final LibraryRepository libraryRepository;
    private final PasswordEncoder encoder;
    private final ImageRepository imageRepository;
    private final VerificationTokenRepository verifyTokenRepository;
    private final EmailService emailService;
    private final PasswordTokenRepository passwordTokenRepository;

    @Value("${app.verificationTokenDurationMs}")
    private Long verificationTokenDurationMs;
    @Override
    public RegisterResponse createUser(RegisterRequest registerRequest) {
        if(registerRequest.getPassword().length() < 7){
            throw new InvalidRequestException(Constant.INVALID_PASSWORD_MSG);
        }
        String username = registerRequest.getUsername();
        String email = registerRequest.getEmail();
        if(userRepository.existsByUsername(username) || userRepository.existsByEmail(email)) {
            throw new ResourceExistException(Constant.RESOURCE_EXIST_MSG);
        }
        Optional<Role> roleOtp = roleRepository.findByName(RoleName.USER);
        if(roleOtp.isEmpty()){
            throw new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }

        Role role = roleOtp.get();
        Library library = new Library();
        Library librarySaved = libraryRepository.save(library);
        User user = mapper.map(registerRequest, User.class);
        user.setRoles(List.of(role));
        user.setIsEnable(false);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setLibrary(librarySaved);
        User userSaved = userRepository.save(user);



        createVerificationTokenForUser(user);

        return mapper.map(userSaved, RegisterResponse.class);
    }



    @Override
    public UserResponse getUserById(String id) {
        Optional<User> userOtp = userRepository.findById(id);
        if(userOtp.isEmpty()){
            throw  new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }
        return mapper.map(userOtp.get(), UserResponse.class);
    }

    @Override
    public UserResponse updateById(ProfileRequest profileRequest, String id) {
        Optional<User> userOtp = userRepository.findById(id);
        if(userOtp.isEmpty()){
            throw  new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
        }
        User user = userOtp.get();
        if(Objects.nonNull(profileRequest.getUsername())){
            Optional<User> checkUser = userRepository.findByUsername(profileRequest.getUsername());

            if(!checkUser.get().getId().equals(id)){
                throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
            }
            user.setUsername(profileRequest.getUsername());
        }
        try{
            if(Objects.nonNull(profileRequest.getFile())){

                if(Objects.nonNull(user.getImage())){
                    Image image = user.getImage();
                    image.setTitle(user.getId() + ".png");
                    FileUploadUtils.saveUserImage(profileRequest.getFile(), id);

                    Image imageSaved = imageRepository.save(image);
                    imageSaved.setPath(Constant.IMAGE_HOST+ "/" + imageSaved.getId());
                    user.setImage(imageSaved);
                }else{
                    Image image = new Image();
                    image.setTitle(user.getId() + ".png");
                    FileUploadUtils.saveUserImage(profileRequest.getFile(), id);

                    Image imageSaved = imageRepository.save(image);
                    imageSaved.setPath(Constant.IMAGE_HOST+ "/" + imageSaved.getId());
                    user.setImage(imageSaved);
                }

            }
            userRepository.save(user);
        }catch (Exception e){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }


        UserResponse userResponse =  mapper.map(user, UserResponse.class);
        userResponse.setImagePath(user.getImage().getPath());
        return userResponse;
    }

    @Override
    public VerificationToken createVerificationTokenForUser(User user) {
        final String token = RandomStringUtils.randomNumeric(4);
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationToken.setExpiryDate(Instant.now().plusMillis(verificationTokenDurationMs));
        log.info("In createVerificationTokenForUser");
        return verifyTokenRepository.save(verificationToken);

    }



    @Override
    public Boolean resendVerifyToken(String username) {
      try{
          if(Objects.isNull(username) || username.isEmpty()){
              throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
          }
          Optional<User> userOtp;
          if(username.contains("@")){
              userOtp = userRepository.findByEmail(username);
          }else{
              userOtp = userRepository.findByUsername(username);
          }

          if(userOtp.isEmpty()){
              throw  new ResourceNotFoundException(Constant.RESOURCE_NOT_FOUND_MSG);
          }
          User user = userOtp.get();
          Optional<VerificationToken> token = verifyTokenRepository.findByUser(user);
          if(token.isEmpty()){
              throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
          }

          VerificationToken verificationToken = token.get();
          verificationToken.setToken(RandomStringUtils.randomNumeric(4));
          verificationToken.setExpiryDate(Instant.now().plusMillis(verificationTokenDurationMs));
          verifyTokenRepository.save(verificationToken);

          EmailRequest emailRequest = new EmailRequest();
          emailRequest.setTo(user.getEmail());
          emailRequest.setSubject(Constant.VERIFY_SUBJECT);
          emailRequest.setUserId(user.getId());
          emailRequest.setBody(verificationToken.getToken());

          emailService.sendVerificationEmail(emailRequest);
          return true;
      }catch (Exception e){
          return false;
      }

    }

    @Override
    public String sendResetPassword(PasswordResetRequest request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if(userOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        User user = userOpt.get();
        PasswordResetToken token = new PasswordResetToken();
        Optional<PasswordResetToken> tokenOpt = passwordTokenRepository.findByUser(user);
        if(tokenOpt.isPresent()) {
            token = tokenOpt.get();
        }
        token.setUser(user);
        token.setToken(RandomStringUtils.randomNumeric(4));
        token.setExpiryDate(Instant.now().plusMillis(verificationTokenDurationMs));


        passwordTokenRepository.save(token);
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(user.getEmail());
        emailRequest.setSubject(Constant.PASSWORD_RESET_SUBJECT);
        emailRequest.setUserId(user.getId());
        emailRequest.setBody(token.getToken());
        emailService.sendResetPasswordEmail(emailRequest);
        return user.getId();
    }

    @Override
    public void resetPassword(PasswordResetRequest request) {
        if(request.getPassword().length() < 7){
            throw new InvalidRequestException(Constant.INVALID_PASSWORD_MSG);
        }
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if(userOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        User user = userOpt.get();

        Optional<PasswordResetToken> tokenOpt = passwordTokenRepository.findByUser(user);
        if(tokenOpt.isEmpty()) {
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }

        PasswordResetToken token = tokenOpt.get();

        if(request.getToken().equals(token.getToken()) && token.getExpiryDate().compareTo(Instant.now()) > 0){
            user.setPassword(encoder.encode(request.getPassword()));
        }else{
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        userRepository.save(user);
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(user.getEmail());
        emailRequest.setSubject(Constant.PASSWORD_RESET_SUCCESS_SUBJECT);
        emailRequest.setUserId(user.getId());
        emailRequest.setBody(Constant.PASSWORD_RESET_SUCCESS_SUBJECT);
        emailService.sendResetPasswordSuccessEmail(emailRequest);

    }
}
