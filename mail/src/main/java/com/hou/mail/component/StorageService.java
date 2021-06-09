package com.hou.mail.component;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    void init();
    void store(MultipartFile multipartFile);
    public Path load(String filename);
    Stream<Path> loadAll();
    Resource loadResource(String fileName);
    void deleteAll();
}
