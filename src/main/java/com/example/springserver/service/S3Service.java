package com.example.springserver.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.springserver.global.apiPayload.format.ErrorCode;
import com.example.springserver.global.apiPayload.format.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${spring.cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;

    // Base64로 받은 데이터 디코딩해서 S3에 업로드 후 이미지url 반환
    public String uploadFileImage(MultipartFile imageFile) {
        try {
            String extension = ".jpg";
            String contentType = imageFile.getContentType();
            if (contentType == null || !isValidImageType(contentType)) {
                throw new GlobalException(ErrorCode.IMAGE_UPLOAD_FAILED);
            } else if (contentType.equals("image/jpeg")) {
                extension = ".jpg";
            } else if (contentType.equals("image/png")) {
                extension = ".png";
            } else if (contentType.equals("image/webp")) {
                extension = ".webp";
            }

            String fileName = "profile-images/" + UUID.randomUUID() + extension;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(imageFile.getSize());

            // S3에 업로드
            amazonS3.putObject(bucketName, fileName, imageFile.getInputStream(), metadata);

            // CloudFront URL 반환
            return cloudFrontDomain + "/" + fileName;
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }
    private boolean isValidImageType(String contentType) {
        return contentType.equals("image/jpeg") || contentType.equals("image/png") || contentType.equals("image/webp");
    }
}