package io.train.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Data
public class OSSConfiguration {
    @Value("${aliyun.endpoint}")
    private String endpoint;

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.bucketName}")
    private String bucketName;

    @Value("${aliyun.video.filehost}")
    private String video;

    @Value("${aliyun.image.filehost}")
    private String image;

    @Value("${aliyun.ppt.filehost}")
    private String ppt;

    @Value("${aliyun.excel.filehost}")
    private String excel;

    @Value("${aliyun.word.filehost}")
    private String word;

    @Value("${aliyun.pdf.filehost}")
    private String pdf;

    @Value("${aliyun.other.filehost}")
    private String other;
}
