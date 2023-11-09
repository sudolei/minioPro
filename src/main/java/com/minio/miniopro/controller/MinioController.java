package com.minio.miniopro.controller;

import com.minio.miniopro.util.AjaxResult;
import com.minio.miniopro.util.MinioClientUtils;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/minio")
public class MinioController {
    @Autowired
    private MinioClientUtils minioClientUtils;

    @GetMapping("/test")
    public AjaxResult testPutObject() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\MSI\\Desktop\\新建文本文档.txt");
        boolean bs = minioClientUtils.putObject("fsp-dev", "新建文本文档.txt", fileInputStream, "image/jpg");
        return AjaxResult.success("上传成功");
    }


    /**
     * 上传
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult upload(@RequestParam(name = "file", required = false) MultipartFile file, HttpServletRequest request) {
        String bucketName = "public";
        String originalFilename = file.getOriginalFilename();
        // 新的文件名 = 存储桶名称_时间戳.后缀名
        String fileName = bucketName + "_" + System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));
        try {
            minioClientUtils.putObject(bucketName, file, fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        } catch (InvalidBucketNameException e) {
            throw new RuntimeException(e);
        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success();
    }


    @GetMapping("/objUrl/{objName}")
    public AjaxResult getObjUrl(@PathVariable("objName") String objName) {
        String url = null;
        try {
            url = minioClientUtils.getObjectUrl("public", objName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        } catch (InvalidBucketNameException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        }

        return AjaxResult.success(url);
    }

    @PostMapping("/makeBucket")
    public AjaxResult makeBucket(@RequestParam String bucket) {
        boolean b = false;
        try {
            b = minioClientUtils.makeBucket(bucket);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return AjaxResult.success(b);
    }


    /**
     * 是否存在bucket
     *
     * @param bucket
     * @return
     */
    @GetMapping("/bucketExists")
    public AjaxResult bucketExists(String bucket) {
        boolean b = false;
        try {
            b = minioClientUtils.bucketExists(bucket);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(b);
    }

    @GetMapping("/list")
    public AjaxResult listBucket() {
        List<String> buckets = null;
        try {
            buckets = minioClientUtils.listBucketNames();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(buckets);
    }

    @DeleteMapping
    public AjaxResult deleteBucket(String bucketName) {
        boolean b = false;
        try {
            b = minioClientUtils.removeBucket(bucketName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(b);
    }

    @GetMapping("/listObjs")
    public AjaxResult listObjs(String bucketName) {
        List<String> strings = null;
        try {
            strings = minioClientUtils.listObjectNames(bucketName);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        } catch (InvalidBucketNameException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(strings);
    }


    @PostMapping("/download")
    public void download() {
        try {
            minioClientUtils.downloadObject("public", "public_1699509370641.jpg", "d://bbb.jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        } catch (InvalidBucketNameException e) {
            throw new RuntimeException(e);
        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/presignedObjectUrl")
    public AjaxResult getPresignedObjectUrl(String bucketName, String objName, int day) {
        int DEFAULT_EXPIRY_TIME = 7 * 24 * 3600;
        String aPublic = null;
        try {
            if (day >= 1 && day <= 7) {
                DEFAULT_EXPIRY_TIME = day * 24 * 3600;
            }
            aPublic = minioClientUtils.getPresignedObjectUrl(bucketName, objName, DEFAULT_EXPIRY_TIME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return AjaxResult.success(aPublic);
    }
}
