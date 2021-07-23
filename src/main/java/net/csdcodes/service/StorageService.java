package net.csdcodes.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;
public interface StorageService {

    void init();

    void storeToDir(MultipartFile file,String dirType);

    void store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    Resource loadAsResource(String filename, Path dir);


    void deleteAll();
}
