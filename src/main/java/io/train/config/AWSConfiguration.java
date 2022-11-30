package io.train.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/***
 * 亚马逊相关配置
 * @author: liyajie
 * @version: 1.0
 * @date: 2021/07/02 15:50
 */
@Configuration
@Component
@Data
public class AWSConfiguration {

    @Value("${aws.accessKey}")
    private String accessKey;

    @Value("${aws.secretKey}")
    private String secretKey;

    @Value("${aws.bucketName}")
    private String bucketName;

}
