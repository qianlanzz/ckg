package com.qianlan.controller;

/**
 * @创建人 qianlan
 * @创建时间 2021/2/12
 * @描述 oss图片上传到阿里云
 */


import com.qianlan.util.OssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oss")
public class OssController {

    @Autowired
    OssUtil ossUtil; //注入OssUtil

    @PostMapping("/uploadfile")
    public Object fileUpload(@RequestParam("file") MultipartFile file)
    {
        try {
            String url = ossUtil.uploadFile(file); //调用OSS工具类
            Map<String, Object> returnbody = new HashMap<>();
            Map<String, Object> returnMap = new HashMap<>();
            returnMap.put("url", url);
            returnbody.put("data",returnMap);
            returnbody.put("code","200");
            returnbody.put("message","上传成功");
            return returnbody;
        }
        catch (Exception e) {
            Map<String, Object> returnbody = new HashMap<>();
            returnbody.put("data",null);
            returnbody.put("code","400");
            returnbody.put("message","上传失败");
            return returnbody;
        }
    }
}

