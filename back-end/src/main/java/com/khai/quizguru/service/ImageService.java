package com.khai.quizguru.service;

import org.springframework.core.io.Resource;

import java.io.File;

public interface ImageService {
    Resource findById(String imageId);
}
