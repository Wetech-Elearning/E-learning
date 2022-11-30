package io.train.modules.oss.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.train.common.io.PropertiesUtils;
import io.train.common.lang.DateUtils;
import io.train.common.utils.IdGen;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.common.utils.ValidateUtils;
import io.train.modules.oss.cloud.OSSFactory;
import io.train.modules.oss.dao.SysOssDao;
import io.train.modules.oss.entity.SysOssEntity;
import io.train.modules.oss.service.SysOssService;
import io.train.modules.oss.utils.VideoTools;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;


@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOssEntity> implements SysOssService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		IPage<SysOssEntity> page = this.page(
			new Query<SysOssEntity>().getPage(params)
		);

		return new PageUtils(page);
	}

	@Override
	public SysOssEntity saveFile(MultipartFile file,String type) {
		String storageType = PropertiesUtils.getConfig("file.upload.storage");
		SysOssEntity sysOss = null;
		if (StringUtils.equals(storageType, "local")) {
			String fileName = StringUtils.trimToEmpty(file.getOriginalFilename());
			String suffix = StringUtils.trimToEmpty(FilenameUtils.getExtension(fileName)).toLowerCase();
			// 默认文件保存本地
			String savePath = PropertiesUtils.getConfig("file.upload.prefix") + File.separator + DateUtils.getYear() + File.separator
					+ DateUtils.getDate("yyyyMM") + File.separator+ DateUtils.getDate("yyyyMMdd");
			String filePath = savePath + File.separator + IdGen.getFullUuid() + "." + suffix;
			try {
//				String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);
				FileUtils.forceMkdir(new File(PropertiesUtils.getConfig("file.upload.path") + savePath));
				File targetFile = new File(PropertiesUtils.getConfig("file.upload.path") + filePath);
				int fileSize = file.getBytes().length; // 文件大小byte
				file.transferTo(targetFile);
				// 上传成功后保存记录信息
				sysOss = new SysOssEntity();
				sysOss.setName(ValidateUtils.strFilter(fileName)); // 过滤文件名中的非法字符，避免攻击
				sysOss.setSuffix(suffix);
				sysOss.setTitle("");
				sysOss.setFileSize(fileSize); // 文件大小byte
//				sysOss.setUrl(url);
				sysOss.setUrl(filePath.replace("\\", "/"));
				String[] allowedFileSuffix = StringUtils.trimToEmpty(PropertiesUtils.getConfig("file.upload.video.suffix"))
						.split(",");
				if("video".equals(type)){
					if(ArrayUtils.contains(allowedFileSuffix, suffix)){
						String bytesFilePath = savePath + File.separator + IdGen.getFullUuid() + ".mp4";
						sysOss.setUrl(bytesFilePath.replace("\\", "/"));
						VideoTools.changeByteTypes2Mp4(PropertiesUtils.getConfig("file.upload.path") + filePath, PropertiesUtils.getConfig("file.upload.path") + bytesFilePath);
						VideoTools.getScreenshot(PropertiesUtils.getConfig("file.upload.path") + bytesFilePath);
					}
				}
				this.save(sysOss);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				sysOss = null;
			}
		}
		return sysOss;
	}

	@Override
	public void updateFileUrl(String objId, String url) {
		UpdateWrapper<SysOssEntity> updateWrapper = new UpdateWrapper();
		updateWrapper.eq("obj_id", objId);
		updateWrapper.set("url", url);
		updateWrapper.set("percent", 100);
		this.baseMapper.update(null, updateWrapper);
	}

	@Override
	public SysOssEntity getByObjId(String objId) {
		QueryWrapper<SysOssEntity> queryWrapper = new QueryWrapper<SysOssEntity>();
		queryWrapper.eq("obj_id",objId);
		return this.baseMapper.selectOne(queryWrapper);
	}

	@Override
	public List<SysOssEntity> getByObjType(String objType) {
		QueryWrapper<SysOssEntity> queryWrapper = new QueryWrapper<SysOssEntity>();
		queryWrapper.eq("obj_type",objType);
		return this.baseMapper.selectList(queryWrapper);
	}

	@Override
	public List<SysOssEntity> getSysOssList(String objId, String creater) {
		QueryWrapper<SysOssEntity> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("obj_id",objId);
		queryWrapper.eq("creater",creater);
		return this.baseMapper.selectList(queryWrapper);
	}
}
