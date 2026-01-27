package com.ecommerce.Ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
//        NOME DEL FILE CORRENTE
        String originalFilename = file.getOriginalFilename();
//        GENERIAMO UN IDENTIFICATIVO UNICO PER IL FILE(UUID)
        String randomId= UUID.randomUUID().toString();
//        CONCATENIAMO LO UUID UNIVOCO A PARTIRE DAL INDICE CHE CONTIENE IL . CIOè LA PARTE FORMATO(ESEMPIO 1234.JPG)
        String fileName = randomId.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));
        String filePath = path + File.separator + fileName;
//        CONTROLLIAMO CHE IL FILE ESISTA E CREIAMOLO
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
//        CARICHIAMO FILE SUL SERVER
        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
    }
}
