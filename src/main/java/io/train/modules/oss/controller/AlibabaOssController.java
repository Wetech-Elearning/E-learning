package io.train.modules.oss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.train.common.io.PropertiesUtils;
import io.train.common.utils.DateUtils;
import io.train.common.utils.R;
import io.train.config.OSSConfiguration;
import io.train.modules.oss.entity.SysOssEntity;
import io.train.modules.oss.service.AlibabaOssService;
import io.train.modules.oss.service.SysOssService;
import io.train.modules.oss.service.impl.AlibabaOssServiceImpl;
import io.train.modules.oss.utils.VideoTools;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("sys/alibaba/oss")
public class AlibabaOssController {
    Logger log = LoggerFactory.getLogger(AlibabaOssServiceImpl.class);

    @Autowired
    AlibabaOssService alibabaOssService;

    @Autowired
    OSSConfiguration ossConfiguration;

    @Autowired
    SysOssService sysOssService;

    @Value("${data.trans2mp4.path}")
    String trans2mp4Path;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public String upload(@RequestParam("file")MultipartFile file, HttpServletRequest request) {
        log.info("=========>文件上传");
        String uploadUrl = "";
        HttpSession httpSession = request.getSession();
        String uuid = request.getParameter("uuid");
        try {
            if (null != file) {

                String filename = file.getOriginalFilename();
                String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                String fileHost = this.getFileHostBySuffix(suffix);
                if (!"".equals(filename.trim())) {
                    File newFile = new File(filename);
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
                    if(sysOssEntityList.size() != 1){
                        SysOssEntity sysOssEntity = new SysOssEntity();
                        sysOssEntity.setName(filename);
                        sysOssEntity.setSuffix(suffix);
                        sysOssEntity.setObjId(Long.parseLong(uuid));
                        sysOssEntity.setCreateDate(new Date());
                        sysOssService.save(sysOssEntity);
                    }
                    //上传到OSS
                    httpSession.setAttribute("uuid", uuid);
                    uploadUrl = alibabaOssService.upload(newFile,fileHost,httpSession);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return uploadUrl;
    }

    /**
     * 查询上传文件列表
     * @return
     */
    @GetMapping("/getObjectList")
    @ResponseBody
    public Map<String,Object> getObjectList() {
        Map<String,Object > map = new HashMap<String,Object >();
        List<String> vedioList = alibabaOssService.getObjectList(ossConfiguration.getBucketName(),"vedio");
        map.put("vedio",vedioList);
        List<String> imageList = alibabaOssService.getObjectList(ossConfiguration.getBucketName(),"image");
        map.put("image",imageList);
        List<String> pptList = alibabaOssService.getObjectList(ossConfiguration.getBucketName(),"ppt");
        map.put("ppt",pptList);
        List<String> excelList = alibabaOssService.getObjectList(ossConfiguration.getBucketName(),"excel");
        map.put("excel",excelList);
        List<String> wordList = alibabaOssService.getObjectList(ossConfiguration.getBucketName(),"word");
        map.put("word",wordList);
        List<String> pdfList = alibabaOssService.getObjectList(ossConfiguration.getBucketName(),"pdf");
        map.put("pdf",pdfList);
        List<String> otherList = alibabaOssService.getObjectList(ossConfiguration.getBucketName(),"other");
        map.put("other",otherList);
        return map;
    }

    @GetMapping("/delete")
    @ResponseBody
    public String  deleteByFileKey(String fileKey) {
        String suffix = fileKey.substring(fileKey.lastIndexOf(".") + 1);
        String fileHost = this.getFileHostBySuffix(suffix);
        return alibabaOssService.delete(fileKey,fileHost);
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
