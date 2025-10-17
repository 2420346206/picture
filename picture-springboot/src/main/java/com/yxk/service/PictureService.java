package com.yxk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yxk.model.dto.PictureCreateDTO;
import com.yxk.model.dto.PictureQueryDTO;
import com.yxk.model.entity.Picture;
import com.yxk.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PictureService extends IService<Picture> {

    /**
     * 上传图片，返回 url
     */
    String upload(MultipartFile file);

    /**
     * 创建图片信息
     */
    Boolean createPicture(PictureCreateDTO pictureCreateDto, HttpServletRequest request);

    /**
     * 获取图片列表（分页）
     */
    Page<PictureVO> getPictureVOPage(PictureQueryDTO pictureQueryDTO, HttpServletRequest request);

    /**
     * 获取分类列表
     */
    List<String> categoryList();

    /**
     * 获取标签列表
     */
    List<String> tagList();

    /**
     * 根据 id 获取图片详细信息
     */
    PictureVO getPictureVOById(Long id);
}
