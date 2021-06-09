package com.hou.mail.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;
@Service
public class HFileStorageService implements StorageService {
    private final Path rootLocation;

    @Autowired
    public HFileStorageService(){
        rootLocation = Paths.get(System.getProperty("user.dir") );
        System.out.println(rootLocation);
    }
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void store(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if(file.isEmpty()){
                System.out.println("no such file");
                NullPointerException nu = new NullPointerException();
                nu.printStackTrace();
                throw nu;
            }else if (fileName.contains("..")){
                IllegalStateException il =  new IllegalStateException();
                il.printStackTrace();
                throw il;
            }else {
                Files.copy(file.getInputStream(), this.rootLocation.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(rootLocation::relativize);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Resource loadResource(String fileName) {
        try {
            Path flie = load(fileName);
            Resource resource = new UrlResource(flie.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }else {
                FileNotFoundException fileNotFoundException = new FileNotFoundException();
                fileNotFoundException.printStackTrace();
                throw fileNotFoundException;
            }
        } catch (MalformedURLException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
