package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.exception.ResourceExistException;
import com.khai.quizguru.exception.ResourceNotFoundException;
import com.khai.quizguru.model.Image;
import com.khai.quizguru.model.Library;
import com.khai.quizguru.payload.request.ProfileRequest;
import com.khai.quizguru.payload.request.RegisterRequest;
import com.khai.quizguru.model.user.Role;
import com.khai.quizguru.enums.RoleName;
import com.khai.quizguru.model.user.User;
import com.khai.quizguru.payload.response.RegisterResponse;
import com.khai.quizguru.payload.response.UserResponse;
import com.khai.quizguru.repository.ImageRepository;
import com.khai.quizguru.repository.LibraryRepository;
import com.khai.quizguru.repository.RoleRepository;
import com.khai.quizguru.repository.UserRepository;
import com.khai.quizguru.service.UserService;
import com.khai.quizguru.utils.Constant;
import com.khai.quizguru.utils.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Override
    public RegisterResponse createUser(RegisterRequest registerRequest) {

        String username = registerRequest.getUsername();
        if(userRepository.existsByUsername(username)) {
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
        user.setPassword(encoder.encode(user.getPassword()));
        user.setLibrary(librarySaved);
        User userSaved = userRepository.save(user);
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

}
