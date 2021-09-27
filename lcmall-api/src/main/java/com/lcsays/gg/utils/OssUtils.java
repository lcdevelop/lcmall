package com.lcsays.gg.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: lichuang
 * @Date: 21-9-1 14:58
 */

public class OssUtils {

    public static String uploadFile(MultipartFile file,
                                    String keypoint,
                                    String accessKeyId,
                                    String accessKeySecret,
                                    String bucketName,
                                    String fileHost) {
        if (!file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            assert originalFilename != null;
            String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = new Date().getTime() + "_" + UUID.randomUUID().toString().substring(0, 8) + extName;
            OSS ossClient = new OSSClientBuilder().build(keypoint, accessKeyId, accessKeySecret);
            try {
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileHost + "/" + fileName, file.getInputStream());
                PutObjectResult putObjectResult = ossClient.putObject(putObjectRequest);
                ossClient.shutdown();
                return "https://" + bucketName + "." + keypoint + "/" + fileHost + "/" + fileName;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }
}
