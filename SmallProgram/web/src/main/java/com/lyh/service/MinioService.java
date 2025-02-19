package com.lyh.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface MinioService {

    String putObject(byte[] file, String filePath);
    List<String> putObjects(List<byte[]> files, List<String> filePaths);
    byte[] getObject(String fileName);
    boolean deleteFile(String fileName);


    String putObject(String kbs, MultipartFile multipartFile, String fileName);

    String putBase64Image(String kbs, String base64Image, String fileName);

//    List<String> putBase64ListImage(String kbs, List<String> base64Images, List<String> originalFileNames);
}
