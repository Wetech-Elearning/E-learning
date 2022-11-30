package io.train.modules.oss.service;

/**
 * 亚马逊文件上传下载相关接口
 * @author:liyajie
 * @createTime:2022/1/26 16:37
 * @version:1.0
 */
public interface AwsService {

    /**
     * 简单上传
     * @author: liyajie
     * @date: 2022/1/26 16:39
     * @param
     * @return java.lang.String
     * @exception:
     * @update:
     * @updatePerson:
     **/
    String simpleUpload(String filePath, String fileName);

    /**
     * 分段上传
     * @author: liyajie
     * @date: 2022/1/26 16:39
     * @param
     * @return java.lang.String
     * @exception:
     * @update:
     * @updatePerson:
     **/
    String multipartUpload(String filePath, String fileName);

    /**
     * 中止分段上传
     * @author: liyajie
     * @date: 2022/1/26 16:39
     * @param
     * @return java.lang.String
     * @exception:
     * @update:
     * @updatePerson:
     **/
    String abortMultipartUpload(String filePath, String fileName);

    /**
     * 中止指定的分段上传
     * @author: liyajie
     * @date: 2022/1/26 17:46
     * @param fileName
     * @param uploadId
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void abortMultipartUploadByUploadId(String fileName, String uploadId);

    /**
     * 跟踪分段上传
     * @author: liyajie
     * @date: 2022/1/26 16:39
     * @param
     * @return java.lang.String
     * @exception:
     * @update:
     * @updatePerson:
     **/
    String trackMPUProgressUpload(String filePath, String fileName, String newFileName, String objId);

    /**
     * 下载
     * @author: liyajie
     * @date: 2022/1/26 16:39
     * @param
     * @return java.lang.String
     * @exception:
     * @update:
     * @updatePerson:
     **/
    String downFile(String fileName);

    /**
     * 删除单个文件
     * @author: liyajie
     * @date: 2022/3/14 11:49
     * @param fileName
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void deleteFile(String fileName);
}
