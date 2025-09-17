package com.yxk.controller;

import com.yxk.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @PostMapping("/upload")
    public String upload(@RequestPart("file")MultipartFile file) {
        return pictureService.Upload(file);
    }
}
