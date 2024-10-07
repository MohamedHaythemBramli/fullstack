package com.linkedin.backend.service.impl;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;



@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {
    private final MinioClient minioClient;

    private String getLink(String bucket, String filename, Long expiry)
            throws InvalidKeyException, ErrorResponseException, InsufficientDataException, InternalException,
            InvalidResponseException, XmlParserException, ServerException, IllegalArgumentException, IOException,
            NoSuchAlgorithmException {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucket)
                        .object(filename)
                        .expiry(Math.toIntExact(expiry))
                        .build());
    }

    public String generateMinioURL(String bucketName, String objectName) {
        try {
            if (objectName == null) {
                return null;
            }
            return getLink(bucketName, objectName, 3600L);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public ObjectWriteResponse upload(String bucketName, String fileName, MultipartFile file)
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException,
            InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {

        log.info(String.format("Check is bucket %s exist", bucketName));
        boolean isBucketExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        log.info(String.format("Bucket %s exist: %s", bucketName, isBucketExist));
        if (!isBucketExist) {
            log.info("Creating bucket: " + bucketName);
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        // Upload file to MinIO server
        return minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(fileName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
    }

    // delete file from MinIO server
    public void delete(String bucketName, String objectName)
            throws ErrorResponseException, InsufficientDataException, InternalException, InvalidKeyException,
            InvalidResponseException, IOException, NoSuchAlgorithmException, ServerException, XmlParserException {
        RemoveObjectArgs target = RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build();
        minioClient.removeObject(target);
    }
}
