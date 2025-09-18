package com.yxk.controller;

import com.yxk.common.BaseResponse;
import com.yxk.common.ResultUtils;
import com.yxk.dto.PictureCreateDto;
import com.yxk.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @PostMapping("/upload")
    public BaseResponse<String> upload(@RequestPart("file") MultipartFile file) {
        String url = pictureService.upload(file);
        return ResultUtils.success(url);
    }

    @PostMapping("/create")
    public BaseResponse<Boolean> createPicture(@RequestBody PictureCreateDto pictureCreateDto, HttpServletRequest request) {
        Boolean result = pictureService.createPicture(pictureCreateDto, request);
        return ResultUtils.success(result);
    }
}
