package com.example.finalprojectbond.Service;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;
@Service
public class FirebaseStorageService {

    private final Storage storage = StorageOptions.getDefaultInstance().getService();

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());
        return blob.getMediaLink();
    }

    public void deleteFile(String fileUrl) {
        String bucketName = "tuwaiq-final-project.firebasestorage.app";
        String fileName = extractFileNameFromUrl(fileUrl);
        BlobId blobId = BlobId.of(bucketName, fileName);
        boolean deleted = storage.delete(blobId);
        if (!deleted) {
            throw new RuntimeException("Failed to delete file from Firebase Storage");
        }
    }

    private String extractFileNameFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new RuntimeException("File URL is null or empty.");
        }


        String cleanUrl = fileUrl.split("\\?")[0];


        if (!cleanUrl.contains("/")) {
            throw new RuntimeException("Invalid file URL format: " + fileUrl);
        }
        return cleanUrl.substring(cleanUrl.lastIndexOf("/") + 1);
    }


}
