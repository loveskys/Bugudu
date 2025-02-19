package com.lyh.controller;

import com.lyh.entity.ComResponse;
import com.lyh.service.MinioService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author lyh
 * @since 2024-07-20
 */
@Slf4j
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Resource
    private MinioService minioService;

    //上传单文件
    @RequestMapping(value = "/upload")
    public ComResponse<?> uploadFile(@RequestBody byte[] file, @RequestParam String path) {
        if (file.length > 0) {
            String newPath = minioService.putObject(file, path);
            if (null != newPath) {
                return new ComResponse<>().success(newPath);
            }
        }
        return new ComResponse<>().fail("上传出现错误");
    }

    //上传多文件
    @RequestMapping(value = "/uploadList")
    public ComResponse<?> uploadFileList(@RequestBody List<byte[]> files, @RequestParam List<String> paths) {
        if (0 < files.size()) {
            List<String> newPaths = minioService.putObjects(files, paths);
            if (null != newPaths && 0 < newPaths.size()) {
                return new ComResponse<>().success(newPaths);
            }
        }
        return new ComResponse<>().fail("上传出现错误");
    }

    //下载单文件
    @RequestMapping(value = "/down")
    public ComResponse<?> downFile(String fileName) {
        byte[] file = minioService.getObject(fileName);
        if (file != null) {
            return new ComResponse<>().success(file);
        }
        return new ComResponse<>().fail("获取文件失败");
    }

    //删除文件
    @RequestMapping(value = "/delete")
    public ComResponse<?> deleteFile(String path) {
        if (minioService.deleteFile(path)) {
            return new ComResponse<>().success();
        }
        return new ComResponse<>().fail();
    }
}