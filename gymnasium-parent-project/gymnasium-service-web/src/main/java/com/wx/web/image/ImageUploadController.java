package com.wx.web.image;

import com.alibaba.fastjson.JSONObject;
import com.wx.config.minio.MinioUtils;
import com.wx.utils.ResultUtils;
import com.wx.utils.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload/")
@Slf4j
public class ImageUploadController {

    @Autowired
    MinioUtils minioUtils;

    @RequestMapping("/uploadImage")
    public ResultVo uploadImage(@RequestParam("file") MultipartFile file){
        log.info("触发上传图片controller");
        JSONObject images = null;
        try {
            images = minioUtils.uploadFile(file, "gym"); //这里记得填你自己的bucket容器
        } catch (Exception e) {
            log.error("上传图片失败", e);
            return ResultUtils.error("图片上传失败，请检查 MinIO 服务是否启动");
        }
        if (images == null || images.get("msg") == null) {
            return ResultUtils.error("图片上传失败，请检查 MinIO 服务是否启动");
        }
        return ResultUtils.success("图片上传成功", images);
    }
}
