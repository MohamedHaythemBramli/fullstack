package com.linkedin.backend.controller;



import com.linkedin.backend.service.impl.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api/minio")
@RequiredArgsConstructor
@Slf4j
public class MinioController {

    private final MinioService minioService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("bucketName") String bucketName,
            @RequestParam("fileName") String fileName,
            @RequestParam("file") MultipartFile file) {

        try {
            minioService.upload(bucketName, fileName, file);
            return ResponseEntity.status(HttpStatus.CREATED).body("File uploaded successfully.");
        } catch (Exception e) {
            log.error("Error uploading file to MinIO: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed.");
        }
    }

    @GetMapping("/download-url")
    public ResponseEntity<String> generateDownloadUrl(
            @RequestParam("bucketName") String bucketName,
            @RequestParam("fileName") String fileName) {

        String url = minioService.generateMinioURL(bucketName, fileName);
        if (url != null) {
            return ResponseEntity.ok(url);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to generate URL.");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(
            @RequestParam("bucketName") String bucketName,
            @RequestParam("fileName") String fileName) {

        try {
            minioService.delete(bucketName, fileName);
            return ResponseEntity.ok("File deleted successfully.");
        } catch (Exception e) {
            log.error("Error deleting file from MinIO: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File deletion failed.");
        }
    }
}

