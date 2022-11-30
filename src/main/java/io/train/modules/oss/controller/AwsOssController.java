package io.train.modules.oss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.train.common.io.PropertiesUtils;
import io.train.common.utils.DateUtils;
import io.train.common.utils.R;
import io.train.config.AWSConfiguration;
import io.train.modules.oss.entity.SysOssEntity;
import io.train.modules.oss.service.AwsService;
import io.train.modules.oss.service.SysOssService;
import io.train.modules.oss.utils.VideoTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

/**
 * 亚马逊上传
 * @author:liyajie
 * @createTime:2022/1/28 0:59
 * @version:1.0
 */
@RestController
@Slf4j
@RequestMapping("sys/aws/oss")
public class AwsOssController {

    @Autowired
    AWSConfiguration awsConfiguration;

    @Autowired
    AwsService awsService;

    @Autowired
    SysOssService sysOssService;

    @Value("${data.file.path}")
    String filePath;

    @Value("${data.trans2mp4.path}")
    String trans2mp4Path;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        log.info("=========>aws文件上传");
        String uploadUrl = "";
        HttpSession httpSession = request.getSession();
        String uuid = request.getParameter("uuid");
        String objType = request.getParameter("objType");
        String creater = request.getParameter("creater");
        try {
            if (null != file) {
                String filename = file.getOriginalFilename();
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                String newFileName = uuid + "_" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN2) + "." + suffix;
                if (!"".equals(filename.trim())) {
                    File newFile = new File(filePath + File.separator + filename);
                    FileOutputStream os = new FileOutputStream(newFile);
                    os.write(file.getBytes());
                    os.close();
                    file.transferTo(newFile);
                    //保存附件表
                    String[] allowedFileSuffix = StringUtils.trimToEmpty(PropertiesUtils.getConfig("file.upload.video.suffix"))
                            .split(",");
                    String fileSuffix = StringUtils.trimToEmpty(FilenameUtils.getExtension(file.getOriginalFilename()))
                            .toLowerCase();
                    if (ArrayUtils.contains(allowedFileSuffix, fileSuffix)) {
                        File target = new File(trans2mp4Path + File.separator + newFile);
                        VideoTools.changeByteTypes2Mp4(newFile, target);
                        newFile = target;
                    }
                    QueryWrapper<SysOssEntity> queryWrapper = new QueryWrapper<SysOssEntity>();
                    queryWrapper.eq("obj_id",uuid);
                    List<SysOssEntity> sysOssEntityList = sysOssService.list(queryWrapper);
                    if(null != sysOssEntityList && sysOssEntityList.size() != 1){
                        SysOssEntity sysOssEntity = new SysOssEntity();
                        sysOssEntity.setName(filename);
                        sysOssEntity.setSuffix(suffix);
                        sysOssEntity.setObjId(Long.parseLong(uuid));
                        sysOssEntity.setObjType(objType);
                        sysOssEntity.setCreateDate(new Date());
                        sysOssEntity.setOssFileName(newFileName);
                        sysOssEntity.setCreater(creater);
                        sysOssService.save(sysOssEntity);
                    }
                    //上传到OSS
                    uploadUrl = awsService.trackMPUProgressUpload(newFile.getPath(), filename, newFileName, uuid);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return uploadUrl;
    }

    /**
     * 根据后缀获取hostFile
     * @param suffix
     * @return
     */
    private String getFileHostBySuffix(String suffix){
        if("mp4".equals(suffix)){
            return "vedio";
        }
        if("jpg".equals(suffix) || "jpeg".equals(suffix) || "png".equals(suffix)){
            return "image";
        }
        if("pptx".equals(suffix)){
            return "ppt";
        }
        if("xls".equals(suffix) || "xlsx".equals(suffix)){
            return "excel";
        }
        if("docx".equals(suffix)){
            return "word";
        }
        if("pdf".equals(suffix)){
            return "pdf";
        }
        return "other";
    }
}
