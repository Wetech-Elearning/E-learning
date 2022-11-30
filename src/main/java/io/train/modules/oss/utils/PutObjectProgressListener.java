package io.train.modules.oss.utils;

import com.aliyun.oss.event.ProgressEvent;
import com.aliyun.oss.event.ProgressEventType;
import com.aliyun.oss.event.ProgressListener;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.modules.oss.entity.SysOssEntity;
import io.train.modules.oss.service.SysOssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

import javax.servlet.http.HttpSession;

/**
 * ossClient.putObject, ossClient.getObject, ossClient.uploadPart方法支持进度条功能.
 * ossClient.uploadFile和ossClient.downloadFile方法不支持进度条功能.
 * 简单上传进度条监听器
 * https://help.aliyun.com/document_detail/84796.html?spm=a2c4g.11186623.6.791.40554d83cDiWSN
 */
@Slf4j
@Service
public class PutObjectProgressListener implements ProgressListener {

    @Autowired
    public SysOssService sysOssService;

    private long bytesWritten = 0;
    private long totalBytes = -1;
    private HttpSession session;
    private boolean succeed = false;
    private int percent = 0;

    public PutObjectProgressListener(HttpSession httpSession,SysOssService sysOssService) {
        this.session = httpSession;
        this.sysOssService = sysOssService;
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_STARTED_EVENT:
                log.debug("Start to upload......");
                break;
            case REQUEST_CONTENT_LENGTH_EVENT:
                this.totalBytes = bytes;
                log.debug(this.totalBytes + " bytes in total will be uploaded to OSS");
                break;

            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                if (this.totalBytes != -1) {
                    int percent = (int)(this.bytesWritten * 100.0 / this.totalBytes);
                    log.debug(bytes + " bytes have been written at this time, upload progress: " + percent + "%(" + this.bytesWritten + "/" + this.totalBytes + ")");
                    //保存进度条到数据库
                    String uuid = String.valueOf(session.getAttribute("uuid"));
                    UpdateWrapper<SysOssEntity> updateWrapper = new UpdateWrapper<>();
                    updateWrapper.eq("obj_id",uuid).set("percent", percent);
                    sysOssService.update(null,updateWrapper);
                    try {
                    	if(percent ==100) {
                    		SysOssEntity obj = sysOssService.getById(uuid);
                    		if(null != obj){
                                File file = new File(obj.getName());
                                file.deleteOnExit();
                            }
                    	}
					} catch (Exception e) {
						e.printStackTrace();
					}
                } else {
                    log.debug(bytes + " bytes have been written at this time, upload ratio: unknown" + "(" + this.bytesWritten + "/...)");
                }
                break;
            case TRANSFER_COMPLETED_EVENT:
                this.succeed = true;
                log.debug("Succeed to upload, " + this.bytesWritten + " bytes have been transferred in total");
                break;
            case TRANSFER_FAILED_EVENT:
                log.debug("Failed to upload, " + this.bytesWritten + " bytes have been transferred");
                break;
            default:
                break;
        }
    }

    public boolean isSucceed(){
        return succeed;
    }
}
