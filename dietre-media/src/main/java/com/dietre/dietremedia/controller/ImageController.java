package com.dietre.dietremedia.controller;

import com.dietre.dietremedia.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class ImageController {
    private final S3Service s3Service;

    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestPart MultipartFile image) {
        String photoUrl = s3Service.uploadFile(image);
        return ResponseEntity.ok().body(photoUrl);
    }
    @PostMapping("/multiple")
    public ResponseEntity<?> uploadImages(@RequestPart List<MultipartFile> images) {
        List<String> photoUrl = s3Service.uploadFile(images);
        return ResponseEntity.ok().body(photoUrl);
    }
}