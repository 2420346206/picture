package com.yxk.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PictureService {

    String Upload(MultipartFile file);

}
