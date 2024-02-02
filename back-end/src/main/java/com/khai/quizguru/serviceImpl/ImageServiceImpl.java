package com.khai.quizguru.serviceImpl;

import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.model.Image;
import com.khai.quizguru.repository.ImageRepository;
import com.khai.quizguru.service.ImageService;
import com.khai.quizguru.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Override
    public Resource findById(String imageId) {
        Optional<Image> imageOpt = imageRepository.findById(imageId);
        if(imageOpt.isEmpty()){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }
        Image image = imageOpt.get();

        try{
            Path imagePath = Paths.get(Constant.GET_USER_DIRECTORY  , image.getTitle());
            return new UrlResource(imagePath.toUri());
        }catch (Exception e){
            throw new InvalidRequestException(Constant.INVALID_REQUEST_MSG);
        }



    }
}
