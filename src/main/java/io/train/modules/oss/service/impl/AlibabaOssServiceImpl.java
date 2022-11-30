package io.train.modules.oss.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.config.OSSConfiguration;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.oss.entity.SysOssEntity;
import io.train.modules.oss.service.AlibabaOssService;
import io.train.modules.oss.service.SysOssService;
import io.train.modules.oss.utils.PutObjectProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service("AlibabaOssService")
public class AlibabaOssServiceImpl implements AlibabaOssService {
    Logger log = LoggerFactory.getLogger(AlibabaOssServiceImpl.class);

    @Autowired
    public SysOssService sysOssService;

    @Autowired
    public CourseService courseService;

    @Autowired
    private OSSConfiguration ossConfiguration;
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 上传
     * @param file
     * @return
     */
    public  String upload(File file, String fileHost, HttpSession httpSession){
        log.info("=========>OSS文件上传开始："+file.getName());
        String endpoint= ossConfiguration.getEndpoint();
        String accessKeyId= ossConfiguration.getAccessKeyId();
        String accessKeySecret=ossConfiguration.getAccessKeySecret();
        String bucketName=ossConfiguration.getBucketName();
        System.out.println(endpoint+"endpoint");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = format.format(new Date());

        if(null == file){
            return null;
        }

        OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
        try {
            //容器不存在，就创建
            if(! ossClient.doesBucketExist(bucketName)){
                ossClient.createBucket(bucketName);
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
                ossClient.createBucket(createBucketRequest);
            }
            //创建文件路径
            String fileUrl = fileHost+"/"+(dateStr + "/" + UUID.randomUUID().toString().replace("-","")+"-"+file.getName());
            //上传文件
            PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file).
                    <PutObjectRequest>withProgressListener(new PutObjectProgressListener(httpSession,sysOssService)));
            //设置权限 这里是公开读
            ossClient.setBucketAcl(bucketName,CannedAccessControlList.PublicRead);
            if(null != result){
                log.info("==========>OSS文件上传成功,OSS地址："+fileUrl);
                Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 180);
                URL url = ossClient.generatePresignedUrl(bucketName, fileUrl, expiration);
                //上传成功之后，回填附件url
                UpdateWrapper<SysOssEntity> updateWrapper = new UpdateWrapper<SysOssEntity>();
                updateWrapper.eq("obj_id",httpSession.getAttribute("uuid")).set("url", url.toString());
                sysOssService.update(null,updateWrapper);
                //上传成功之后，回填课程fileUrl
                /*UpdateWrapper<CourseEntity> updateWrapper1 = new UpdateWrapper<CourseEntity>();
                updateWrapper1.eq("uuid",httpSession.getAttribute("uuid")).set("file_url", url.toString());
                courseService.update(null,updateWrapper1);*/
                return url.toString();
            }
        }catch (OSSException oe){
            log.error(oe.getMessage());
        }catch (ClientException ce){
            log.error(ce.getMessage());
        }finally {
            //关闭
            ossClient.shutdown();
        }
        return null;
    }


    /**
     * 删除
     * @param fileKey
     * @return
     */
    public  String delete(String fileKey,String fileHost){
        log.info("=========>OSS文件删除开始");
        String endpoint= ossConfiguration.getEndpoint();
        String accessKeyId= ossConfiguration.getAccessKeyId();
        String accessKeySecret=ossConfiguration.getAccessKeySecret();
        String bucketName=ossConfiguration.getBucketName();
        try {
            OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);

            if(!ossClient.doesBucketExist(bucketName)){
                log.info("==============>您的Bucket不存在");
                return "您的Bucket不存在";
            }else {
                log.info("==============>开始删除Object");
                ossClient.deleteObject(bucketName,fileKey);
                log.info("==============>Object删除成功："+fileKey);
                return "==============>Object删除成功："+fileKey;
            }
        }catch (Exception ex){
            log.info("删除Object失败",ex);
            return "删除Object失败";
        }
    }

    /**
     * 查询文件名列表
     * @param bucketName
     * @return
     */
    public List<String> getObjectList(String bucketName, String fileHost){
        List<String> listRe = new ArrayList<>();
        String endpoint= ossConfiguration.getEndpoint();
        String accessKeyId= ossConfiguration.getAccessKeyId();
        String accessKeySecret=ossConfiguration.getAccessKeySecret();
        try {
            log.info("===========>查询文件名列表");
            OSSClient ossClient = new OSSClient(endpoint,accessKeyId,accessKeySecret);
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);
            //列出11111目录下今天所有文件
            listObjectsRequest.setPrefix(fileHost + "/" + format.format(new Date())+"/");
            ObjectListing list = ossClient.listObjects(listObjectsRequest);
            for(OSSObjectSummary objectSummary : list.getObjectSummaries()){
                System.out.println(objectSummary.getKey());
                listRe.add(objectSummary.getKey());
            }
            return listRe;
        }catch (Exception ex){
            log.info("==========>查询列表失败",ex);
            return new ArrayList<>();
        }
    }
}
