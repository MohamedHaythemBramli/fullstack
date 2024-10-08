package com.linkedin.backend.controller;

import com.linkedin.backend.service.impl.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {



    private final MinioService minioService;

    private static final String BUCKET_NAME = "hotfolder";

    @PostMapping("/create")
    public String createFolders() {
        try {
            // Check and create the bucket if it doesn't exist
            minioService.createBucketIfNotExists(BUCKET_NAME);

            String[] folderNames = {"csv", "xml", "pdf"};
            for (String folder : folderNames) {
                minioService.createFolder(BUCKET_NAME, folder);
            }
            return "Folders created successfully!";
        } catch (Exception e) {
            return "Error creating folders: " + e.getMessage();
        }
    }
    @DeleteMapping("/delete/{folderName}")
    public String deleteFolder(@PathVariable String folderName) {
        try {
            minioService.deleteFolder(BUCKET_NAME, folderName);
            return "Folder deleted successfully!";
        } catch (Exception e) {
            return "Error deleting folder: " + e.getMessage();
        }
    }
}
