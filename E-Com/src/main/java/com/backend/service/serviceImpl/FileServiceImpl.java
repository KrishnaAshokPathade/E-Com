package com.backend.service.serviceImpl;

import com.backend.exception.BadApiRequest;
import com.backend.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = fileName + extension;

        String FullPathWithFileName = path + fileNameWithExtension;
        if (extension.equalsIgnoreCase("JPG")||extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg")) {
// file save

            File folder = new File(path);
            if (!folder.exists()) {
                //create folder
                folder.mkdirs();
            }

            Files.copy(file.getInputStream(), Paths.get(FullPathWithFileName));
            return fileNameWithExtension;
        } else {
            throw new BadApiRequest("File with this " + extension + " not allowed");
        }

    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path + name;

        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
