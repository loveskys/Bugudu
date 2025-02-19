package com.lyh.service.impl;

import com.lyh.service.MinioService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MinioServiceImpl implements MinioService {

    @Resource
    private MinioClient minioClient;
    @Value(value = "${minio.bucket}")
    private String bucket;
    @Value(value = "${minio.host}")
    private String host;



    /**
     * 上传单文件
     * @param file
     * @param filePath
     */
    public String putObject(byte[] file, String filePath) {
        try (InputStream inputStream = new ByteArrayInputStream(file)) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(filePath)
                            .stream(inputStream, file.length, -1).build());
            System.out.println("minIO上传完成，44444444444444444");
            return host + bucket +"/"+ filePath;
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }



    /**
     * 上传多个文件
     */
    public List<String> putObjects(List<byte[]> files, List<String> filePaths) {
        List<String> newPaths = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            try (InputStream inputStream = new ByteArrayInputStream(files.get(i))) {
                minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(filePaths.get(i))
                        .stream(inputStream, files.get(i).length, -1).build());
                newPaths.add(host + bucket +"/"+ filePaths.get(i));
            } catch (Exception e) {
                log.error(e.getMessage(),e);
                return null;
            }
        }
        return newPaths;
    }

    /**
     * 根据文件名下载文件
     */
    public byte[] getObject(String fileName) {
        try (InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(fileName).build());
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024*4];
            int n = 0;
            while (-1 != (n = stream.read(buffer))) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        } catch (Exception e) {
            log.error("文件读取异常", e);
        }
        return null;
    }


    /**
     * 根据文件名删除文件
     */
    public boolean deleteFile(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(fileName).build());
            return true;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return false;
    }


    /**
     * 批量上传Base64编码的图片
     * @param base64Images Base64编码的图片字符串列表
     * @param originalFileNames 文件短名列表
     * @return 上传后的文件URL列表
     */
    public List<String> putBase64ListImage(String kbs, List<String> base64Images, List<String> originalFileNames) {
        List<String> uploadedUrls = new ArrayList<>();
        for (int i = 0; i < base64Images.size(); i++) {
            String base64Image = base64Images.get(i);
            String originalFileName = originalFileNames.get(i);
            String uploadedUrl = putBase64Image(kbs, base64Image, originalFileName);
            if (uploadedUrl != null) {
                uploadedUrls.add(uploadedUrl);
            }
        }
        return uploadedUrls;
    }


    /**
     * 上传MultipartFile单文件
     */
    public String putObject(String kbs, MultipartFile multipartFile, String originalFileName) {
        String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fileName = kbs + formattedDate + "/" + originalFileName;
        try (InputStream inputStream = new ByteArrayInputStream(multipartFile.getBytes())) {
            minioClient.putObject(PutObjectArgs.builder().bucket(bucket)
                    .object(fileName)
                    .stream(inputStream, multipartFile.getSize(), -1)
                    .contentType(multipartFile.getContentType())
                    .build());
            return host + bucket + "/" + fileName;
        } catch (Exception e) {
            System.out.println("错误：" + e.getMessage());
            log.error(e.getMessage(), e);
        }
        return null;
    }


    /**
     * 上传单张Base64编码的图片
     * @param base64Image Base64编码的图片字符串
     * @param originalFileName 文件短名
     * @return 上传后的文件URL
     */
    public String putBase64Image(String kbs, String base64Image, String originalFileName) {
        try {
            if (base64Image.startsWith("http")) {
                return base64Image;
            }
            String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String fileName = kbs + formattedDate + "/" + originalFileName;
            byte[] imageBytes = decodeBase64(base64Image);
            try (ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)) {
                minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(fileName).stream(bais, imageBytes.length, -1).build());
                return host + bucket +"/"+ fileName;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } catch (IOException e) {
            System.out.println("错误："+e.getMessage());
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private byte[] decodeBase64(String base64Image) throws IOException {
        String[] parts = base64Image.split(",");
        String base64Data = parts[1];
        return java.util.Base64.getDecoder().decode(base64Data);
    }


}