package com.backend.service.serviceImpl;

import com.backend.exception.BadApiRequest;
import com.backend.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * Uploads a file
     *
     * @param file
     * @param path
     * @return The generated filename
     * @throws IOException If an I/O error occurs during the file upload process.
     * @throws BadApiRequest If the file extension is not allowed.
     */

    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {
        logger.info("uploadFile method is running started");
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString();
        logger.info("Generated random fileName:{}", fileName);
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = fileName + extension;

        String FullPathWithFileName = path + fileNameWithExtension;
        if (extension.equalsIgnoreCase(".JPG") || extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
            // file save

            File folder = new File(path);
            if (!folder.exists()) {
                //create folder
                folder.mkdirs();
                folder.deleteOnExit();
            }

            Files.copy(file.getInputStream(), Paths.get(FullPathWithFileName));
            return fileNameWithExtension;
        } else {
            throw new BadApiRequest("File with this " + extension + " not allowed");
        }

    }

    /**
     * Retrieves an InputStream for a resource
     *
     * @param path
     * @param name
     * @return inputStream
     * @throws FileNotFoundException
     */


    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path + name;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}
