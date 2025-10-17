package com.yxk.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yxk.common.BaseResponse;
import com.yxk.model.dto.PictureQueryDTO;
import com.yxk.utils.ResultUtils;
import com.yxk.model.dto.PictureCreateDTO;
import com.yxk.service.PictureService;
import com.yxk.model.vo.PictureVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public BaseResponse<Boolean> createPicture(@RequestBody PictureCreateDTO pictureCreateDto, HttpServletRequest request) {
        Boolean result = pictureService.createPicture(pictureCreateDto, request);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取图片列表
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<PictureVO>> PictureList(@RequestBody PictureQueryDTO pictureQueryDTO, HttpServletRequest request) {
        // 查询数据库
        Page<PictureVO> picturePage = pictureService.getPictureVOPage(pictureQueryDTO, request);
        // 获取封装类
        return ResultUtils.success(picturePage);
    }

    /**
     * 获取分类列表
     */
    @GetMapping("/list/category")
    public BaseResponse<List<String>> categoryList() {
        List<String> categoryList = pictureService.categoryList();
        return ResultUtils.success(categoryList);
    }

    /**
     * 获取标签列表
     */
    @GetMapping("/list/tag")
    public BaseResponse<List<String>> tagList() {
        List<String> tagList = pictureService.tagList();
        return ResultUtils.success(tagList);
    }

    /**
     * 获取标签列表
     */
    @GetMapping("/get")
    public BaseResponse<PictureVO> getPictureById(@RequestParam Long id) {
        PictureVO pictureVO = pictureService.getPictureVOById(id);
        return ResultUtils.success(pictureVO);
    }
}
