package io.train.modules.oss.service.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.config.AWSConfiguration;
import io.train.modules.oss.entity.SysOssEntity;
import io.train.modules.oss.service.AwsService;
import io.train.modules.oss.service.SysOssService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Date;

/**
 * @desc:
 * @author:liyajie
 * @createTime:2022/1/26 16:39
 * @version:1.0
 */
@Service("awsService")
@Slf4j
public class AwsServiceImpl implements AwsService {

    @Autowired
    private AWSConfiguration awsConfiguration;

    @Autowired
    SysOssService sysOssService;

    @Override
    public String simpleUpload(String filePath, String fileName) {
        String result = "";
        try{
            AWSCredentials awsCredentials = new BasicAWSCredentials(awsConfiguration.getAccessKey(), awsConfiguration.getSecretKey());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.AP_NORTHEAST_1)
                    .build();
            PutObjectRequest request = new PutObjectRequest(awsConfiguration.getBucketName(), fileName, new File(filePath));
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("plain/text");
            metadata.addUserMetadata("title", "someTitle");
            request.setMetadata(metadata);
            PutObjectResult putObjectResult = s3Client.putObject(request);
            if (null != putObjectResult) {
                GeneratePresignedUrlRequest httpRequest = new GeneratePresignedUrlRequest(awsConfiguration.getBucketName(), fileName);
                result =  s3Client.generatePresignedUrl(httpRequest).toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String multipartUpload(String filePath, String fileName) {
        String result = "";
        try {
            AWSCredentials awsCredentials = new BasicAWSCredentials(awsConfiguration.getAccessKey(), awsConfiguration.getSecretKey());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.AP_NORTHEAST_1)
                    .build();
            TransferManager tm = TransferManagerBuilder.standard()
                    .withS3Client(s3Client)
                    .build();
            Upload upload = tm.upload(awsConfiguration.getBucketName(), fileName, new File(filePath));
            log.info(fileName + ",分段上传开始");
            upload.waitForCompletion();
            log.info(fileName + ",分段上传完成");
            GeneratePresignedUrlRequest httpRequest = new GeneratePresignedUrlRequest(awsConfiguration.getBucketName(), fileName);
            result =  s3Client.generatePresignedUrl(httpRequest).toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String abortMultipartUpload(String filePath, String fileName) {
        int sevenDays = 1000 * 60 * 60 * 24 * 7;
        Date oneWeekAgo = new Date(System.currentTimeMillis() - sevenDays);
        try {
            AWSCredentials awsCredentials = new BasicAWSCredentials(awsConfiguration.getAccessKey(), awsConfiguration.getSecretKey());
            TransferManager tm = new TransferManager(new AWSStaticCredentialsProvider(awsCredentials));
            tm.abortMultipartUploads(awsConfiguration.getBucketName(), oneWeekAgo);
        }catch (AmazonClientException amazonClientException){
            amazonClientException.printStackTrace();
        }
        return null;
    }

    @Override
    public void abortMultipartUploadByUploadId(String fileName, String uploadId) {
        try {
            AWSCredentials awsCredentials = new BasicAWSCredentials(awsConfiguration.getAccessKey(), awsConfiguration.getSecretKey());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.AP_NORTHEAST_1)
                    .build();
            s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
                    awsConfiguration.getBucketName(), fileName, uploadId));
        }catch (AmazonClientException amazonClientException){
            amazonClientException.printStackTrace();
        }
    }

    @Override
    public String trackMPUProgressUpload(String filePath, String fileName, String newFileName, String objId) {
        String result = "";
        try {
            AWSCredentials awsCredentials = new BasicAWSCredentials(awsConfiguration.getAccessKey(), awsConfiguration.getSecretKey());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.AP_NORTHEAST_1)
                    .build();
            // 先删除云服务器上的文件
            SysOssEntity sysOssEntity = sysOssService.getByObjId(objId);
            if(null != sysOssEntity){
                s3Client.deleteObject(new DeleteObjectRequest(awsConfiguration.getBucketName(),sysOssEntity.getOssFileName()));
            }
            // 上传新的文件
            TransferManager tm = new TransferManager(new AWSStaticCredentialsProvider(awsCredentials));
            PutObjectRequest request = new PutObjectRequest(awsConfiguration.getBucketName(), newFileName, new File(filePath));
            request.setGeneralProgressListener(new ProgressListener() {
                @Override
                public void progressChanged(ProgressEvent progressEvent) {
                    log.info("Transferred bytes: " + progressEvent.getBytesTransferred());
                }
            });
            Upload upload = tm.upload(request);
            while (!upload.isDone()){
                Double percent = upload.getProgress().getPercentTransferred();
                UpdateWrapper<SysOssEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("obj_id", objId);
                updateWrapper.set("percent", percent > 99 ? 100 : new Double(percent).intValue());
                updateWrapper.set("name", fileName);
                updateWrapper.set("oss_file_name", newFileName);
                sysOssService.update(null, updateWrapper);
            }
            upload.waitForCompletion();
            GeneratePresignedUrlRequest httpRequest = new GeneratePresignedUrlRequest(awsConfiguration.getBucketName(), newFileName);
            result =  s3Client.generatePresignedUrl(httpRequest).toString();
            sysOssService.updateFileUrl(objId, result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String downFile(String fileName) {
        String url = "";
        try {
            AWSCredentials awsCredentials = new BasicAWSCredentials(awsConfiguration.getAccessKey(), awsConfiguration.getSecretKey());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.AP_NORTHEAST_1)
                    .build();
            if(StringUtils.isEmpty(fileName)){
                return "请传入文件名称";
            }
            GeneratePresignedUrlRequest httpRequest = new GeneratePresignedUrlRequest(awsConfiguration.getBucketName(), fileName);
            url =  s3Client.generatePresignedUrl(httpRequest).toString();
            log.info("文件下载地址：" + url);
        }catch (AmazonClientException amazonClientException){
            amazonClientException.printStackTrace();
        }
        return url;
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            AWSCredentials awsCredentials = new BasicAWSCredentials(awsConfiguration.getAccessKey(), awsConfiguration.getSecretKey());
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withRegion(Regions.AP_NORTHEAST_1)
                    .build();
            s3Client.deleteObject(awsConfiguration.getBucketName(), fileName);
            log.info("删除了文件：{}", fileName);
        }catch (AmazonClientException amazonClientException){
            amazonClientException.printStackTrace();
        }
    }
}
