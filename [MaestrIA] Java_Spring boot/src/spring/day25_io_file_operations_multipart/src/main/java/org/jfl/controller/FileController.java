package org.jfl.controller;

import org.jfl.service.FileStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {
    private final FileStorageService fileService;

    public FileController(FileStorageService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public String upload(
            @RequestParam("file")
            MultipartFile file) {

        System.out.println("Name: " + file.getOriginalFilename());
        System.out.println("Size: " + file.getSize());
        System.out.println("Content type: " + file.getContentType());

        try {
            fileService.store(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "File received";
    }

    @GetMapping("/{name}")
    public ResponseEntity<Resource> download(@PathVariable String name)
    {
        try {
            Resource resource = fileService.download(name);
            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\""
                                    + name
                                    + "\""
                    )
                    .contentType(
                            MediaType.APPLICATION_OCTET_STREAM
                    )
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}