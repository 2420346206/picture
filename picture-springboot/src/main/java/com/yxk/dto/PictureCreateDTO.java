package com.yxk.dto;

import lombok.Data;

import java.util.List;

@Data
public class PictureCreateDTO {

    /**
     * 图片 url
     */
    private String url;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签（JSON 数组）
     */
    private List<String> tags;

    /**
     * 空间 id
     */
    private Long spaceId;

}
