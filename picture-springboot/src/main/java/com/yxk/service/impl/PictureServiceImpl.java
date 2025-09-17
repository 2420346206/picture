package com.yxk.service.impl;

import com.aliyun.oss.OSS;
import com.yxk.config.OssClientConfig;
import com.yxk.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class PictureServiceImpl implements PictureService {

    @Autowired
    private OssClientConfig ossClientConfig;

    @Autowired
    private OSS ossClient;

    @Override
    public String Upload(MultipartFile file) {
        String fileName = "uploads/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        try {
            ossClient.putObject("your-bucket", fileName, file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "https://" + ossClientConfig.getBucket() + ".oss-cn-hangzhou.aliyuncs.com/" + fileName;
    }
}
