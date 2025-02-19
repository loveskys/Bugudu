package com.lyh.entity.activity.dto;

import com.lyh.entity.ComResponse;
import lombok.Data;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SwiperUploadDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private MultipartFile file;

    private String url;

    public ComResponse<?> validate() {
        if (!StringUtils.hasText(url)) {
            return new ComResponse<>().error("无链接");
        }
        return null;
    }
}
