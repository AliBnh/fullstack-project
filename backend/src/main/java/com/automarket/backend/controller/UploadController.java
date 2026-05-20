package com.automarket.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    @Value("${upload.dir:./uploads}")
    private String uploadDir;

    @PostMapping
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) return ResponseEntity.badRequest().body("No file");

        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);

        String ext = "";
        String orig = file.getOriginalFilename();
        if (orig != null && orig.contains(".")) ext = orig.substring(orig.lastIndexOf("."));

        String filename = UUID.randomUUID() + ext;
        Files.copy(file.getInputStream(), dir.resolve(filename));

        String url = "/api/uploads/" + filename;
        return ResponseEntity.ok(Map.of("url", url));
    }

    @GetMapping("/{filename}")
    public ResponseEntity<byte[]> serve(@PathVariable String filename) throws IOException {
        Path file = Paths.get(uploadDir).resolve(filename);
        if (!Files.exists(file)) return ResponseEntity.notFound().build();

        byte[] bytes = Files.readAllBytes(file);
        String contentType = Files.probeContentType(file);
        if (contentType == null) contentType = "image/jpeg";

        return ResponseEntity.ok().header("Content-Type", contentType).body(bytes);
    }
}
