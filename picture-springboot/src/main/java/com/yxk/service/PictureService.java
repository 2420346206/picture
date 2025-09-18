package com.yxk.service;

import com.yxk.dto.PictureCreateDto;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface PictureService {

    String upload(MultipartFile file);

    Boolean createPicture(PictureCreateDto pictureCreateDto, HttpServletRequest request);
}
