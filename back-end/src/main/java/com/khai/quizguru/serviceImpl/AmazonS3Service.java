package com.khai.quizguru.serviceImpl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.khai.quizguru.exception.InvalidRequestException;
import com.khai.quizguru.payload.request.FileResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class AmazonS3Service {
    private AmazonS3 s3client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;
    @Value("${amazonProperties.region}")
    private String region;
    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client =  AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart, String id) {
        return  new Date().getTime() + id + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
    }
    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }
    public FileResponse uploadFile(MultipartFile multipartFile, String id) {

        String fileUrl = "";
        String fileName = "";
        try {
            File file = convertMultiPartToFile(multipartFile);
            fileName = generateFileName(multipartFile, id);
            fileUrl = endpointUrl + "/" + fileName;
            uploadFileTos3bucket(fileName, file);
        } catch (Exception e) {
            throw new InvalidRequestException(e.getMessage());
        }
        FileResponse fileResponse = new FileResponse();
        fileResponse.setFileName(fileName);
        fileResponse.setUrl(fileUrl);
        return fileResponse;
    }

    public void deleteFile(String fileName) {
        s3client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }
}
