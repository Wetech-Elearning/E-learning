package io.train.modules.oss.controller;

import com.google.gson.Gson;

import io.train.common.exception.RRException;
import io.train.common.io.PropertiesUtils;
import io.train.common.utils.ConfigConstant;
import io.train.common.utils.Constant;
import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.common.validator.ValidatorUtils;
import io.train.common.validator.group.AliyunGroup;
import io.train.common.validator.group.QcloudGroup;
import io.train.common.validator.group.QiniuGroup;
import io.train.modules.oss.cloud.CloudStorageConfig;
import io.train.modules.oss.cloud.OSSFactory;
import io.train.modules.oss.entity.SysOssEntity;
import io.train.modules.oss.service.AwsService;
import io.train.modules.oss.service.SysOssService;
import io.train.modules.sys.service.SysConfigService;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件上传
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("sys/oss")
public class SysOssController {
	@Autowired
	private SysOssService sysOssService;
    @Autowired
    private SysConfigService sysConfigService;
    @Autowired
	private AwsService awsService;

    private final static String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;
	
	/**
	 * 列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:oss:all")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = sysOssService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 根据objId和creater查询上传文件列表
	 */
	@GetMapping("/getSysOssList")
	public R getSysOssList(@RequestParam Map<String, Object> params){
		String objId = String.valueOf(params.get("courseReportId"));
		String creater = String.valueOf(params.get("studentId"));
		List<SysOssEntity> sysOssEntityList = sysOssService.getSysOssList(objId,creater);
		return R.ok().put("data", sysOssEntityList);
	}
	
	@RequestMapping("/images")
	public void getUploadImage(@RequestParam(value="path") String imagePath,HttpServletResponse response,HttpServletRequest request){
		String path = PropertiesUtils.getConfig("file.upload.path");
		File file = new File(path+imagePath);
		String[] allowedFileSuffix = StringUtils.trimToEmpty(PropertiesUtils.getConfig("file.upload.video.suffix"))
				.split(",");
		String fileSuffix = StringUtils.trimToEmpty(FilenameUtils.getExtension(file.getName()))
				.toLowerCase();
		if (ArrayUtils.contains(allowedFileSuffix, fileSuffix)) {
			try {
				play(request,response,path+imagePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			downLoadImage(response,file);
		}
		
	}
	
	public void downLoadImage(HttpServletResponse response,File file){
		try {
			FileInputStream hFile=new FileInputStream(file);
            int i=hFile.available();
            byte data[]=new byte[i];
            hFile.read(data);
            hFile.close();
            response.setContentType("image/*");
            OutputStream toClient=response.getOutputStream();
            toClient.write(data);
            toClient.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void play(HttpServletRequest request,HttpServletResponse response,String filepath) throws IOException{
		response.reset();
		String rangeString = request.getHeader("Range");
		OutputStream outputStream = response.getOutputStream();
		File file = new File(filepath);
		if(file.exists()){
			RandomAccessFile targetFile = new RandomAccessFile(file, "r");
			long fileLength = targetFile.length();
			if(targetFile != null){
				long range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
				 response.setHeader("Content-Type", "video/mp4");
                 //设置此次相应返回的数据长度
                 response.setHeader("Content-Length", String.valueOf(fileLength - range));
                 //设置此次相应返回的数据范围
                 response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
                 //返回码需要为206，而不是200
                 response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                 //设定文件读取开始位置（以字节为单位）
                 targetFile.seek(range);
			} else {
				response.setHeader("Content-Disposition", "attachment; filename="+file.getName());
	            //设置文件长度
	            response.setHeader("Content-Length", String.valueOf(file.getName()));
	            //解决编码问题
	            response.setHeader("Content-Type","application/octet-stream");
			}
			byte[] cache = new byte[1024 * 300];
            int flag;
            while ((flag = targetFile.read(cache))!=-1){
                outputStream.write(cache, 0, flag);
            }
		} else {
			String message = "file is not exists";
            response.setHeader("Content-Type","application/json");
            outputStream.write(message.getBytes(StandardCharsets.UTF_8));
		}
		outputStream.flush();
        outputStream.close();
	}

    /**
     * 云存储配置信息
     */
    @GetMapping("/config")
    @RequiresPermissions("sys:oss:all")
    public R config(){
        CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);

        return R.ok().put("config", config);
    }


	/**
	 * 保存云存储配置信息
	 */
	@PostMapping("/saveConfig")
	@RequiresPermissions("sys:oss:all")
	public R saveConfig(@RequestBody CloudStorageConfig config){
		//校验类型
		ValidatorUtils.validateEntity(config);

		if(config.getType() == Constant.CloudService.QINIU.getValue()){
			//校验七牛数据
			ValidatorUtils.validateEntity(config, QiniuGroup.class);
		}else if(config.getType() == Constant.CloudService.ALIYUN.getValue()){
			//校验阿里云数据
			ValidatorUtils.validateEntity(config, AliyunGroup.class);
		}else if(config.getType() == Constant.CloudService.QCLOUD.getValue()){
			//校验腾讯云数据
			ValidatorUtils.validateEntity(config, QcloudGroup.class);
		}

        sysConfigService.updateValueByKey(KEY, new Gson().toJson(config));

		return R.ok();
	}
	

	/**
	 * 上传文件
	 */
	@PostMapping("/upload")
	public R upload(@RequestParam("file") MultipartFile file) throws Exception {
		String storageType = PropertiesUtils.getConfig("file.upload.storage");
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		SysOssEntity ossEntity = new SysOssEntity();
		if(StringUtils.equals(storageType, "local")){
			ossEntity = sysOssService.saveFile(file,"file");
		} else {
			String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);
			//保存文件信息
			ossEntity.setUrl(url);
			ossEntity.setName(file.getOriginalFilename());
			ossEntity.setCreateDate(new Date());
			sysOssService.save(ossEntity);
		}
		//上传文件

		return R.ok().put("data", ossEntity);
	}
	
	/**
	 * 删除
	 */
	@PostMapping("/delete")
	public R delete(@RequestParam Long id){
		Long[] idss = {id};
		sysOssService.removeByIds(Arrays.asList(idss));
		return R.ok();
	}
	
	/**
	 * 视频上传
	 * @param file
	 * @return
	 * @throws IOException
	 */
	@PostMapping("/upload/video")
	public R videoUplad(@RequestParam("file") MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}
		// 首先校验文件格式是否满足要求
		String[] allowedFileSuffix = StringUtils.trimToEmpty(PropertiesUtils.getConfig("file.upload.video.suffix"))
				.split(",");
		String fileSuffix = StringUtils.trimToEmpty(FilenameUtils.getExtension(file.getOriginalFilename()))
				.toLowerCase();
		if (!ArrayUtils.contains(allowedFileSuffix, fileSuffix)) {
			return R.error("视频格式不满足要求规范，请重新检查后再试");
		}
		SysOssEntity sysOss = sysOssService.saveFile(file,"video");
		if (sysOss == null) {
			return R.error("文件上传失败，请重新检查后再试");
		}
		return R.ok().put("data", sysOss);
	}

	@GetMapping("/getFileByObjType")
	public R getFileByObjType(HttpServletRequest request) {
		String objType = request.getParameter("objType");
		List<SysOssEntity> sysOssEntityList = sysOssService.getByObjType(objType);
		if(sysOssEntityList.size() > 0){
			for (SysOssEntity sysOssEntity:sysOssEntityList) {
				sysOssEntity.setUrl(awsService.downFile(sysOssEntity.getOssFileName()));
			}
		}
		return R.ok().put("data", sysOssEntityList);
	}

}
