package org.jfl.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path uploadDirectory = Path.of("uploads");

    public String store(MultipartFile file) throws IOException {
        Files.createDirectories(uploadDirectory);

        String storedName = UUID.randomUUID() + ".bin";
        Path target = uploadDirectory.resolve(storedName).normalize();

        long maxSize = 5 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException(
                    "File is too large"
            );
        }

        try (InputStream input = file.getInputStream()) {
            Files.copy(input,target, StandardCopyOption.REPLACE_EXISTING);
        }

        return storedName;
    }


    public Resource download(String fileName) throws IOException {
        Path path = uploadDirectory.resolve(fileName).normalize();

        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new FileNotFoundException(fileName);
        }

        Resource resource = new FileSystemResource(path);
        return resource;
    }
}
