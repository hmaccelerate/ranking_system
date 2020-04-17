package com.info6205.ranking_system.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileService {

    @Value("${app.upload.dir}")
    public String uploadDir;

    public String uploadFile(MultipartFile file) {
        String filePath= "";
        try {
            Path copyLocation = Paths
                    .get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            filePath=copyLocation.toString();
//            return ;
        } catch (Exception e) {
            e.printStackTrace();
//            throw new FileStorageException("Could not store file " + file.getOriginalFilename()
//                    + ". Please try again!");
        }

        return filePath;
    }
}