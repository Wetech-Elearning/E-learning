package io.train.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.oss.entity.SysOssEntity;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);

	SysOssEntity saveFile(MultipartFile file,String type);

	/**
	 * 更新上传文件的url
	 * @author: liyajie
	 * @date: 2022/1/29 22:14
	 * @param objId
	 * @return void
	 * @exception:
	 * @update:
	 * @updatePerson:
	 **/
	void updateFileUrl(String objId, String url);

	/**
	 * 根据objID查询上传文件信息
	 * @author: liyajie
	 * @date: 2022/3/16 16:03
	 * @param objId
	 * @return io.train.modules.oss.entity.SysOssEntity
	 * @exception:
	 * @update:
	 * @updatePerson:
	 **/
	SysOssEntity getByObjId(String objId);

	/**
	 * 根据objType查询上传文件信息
	 * @author: liyajie
	 * @date: 2022/3/16 16:03
	 * @param objType
	 * @return io.train.modules.oss.entity.SysOssEntity
	 * @exception:
	 * @update:
	 * @updatePerson:
	 **/
	List<SysOssEntity> getByObjType(String objType);
	
	/**
	 * 根据objId和creater查询附件
	 * @author: liyajie 
	 * @date: 2022/8/18 15:20
	 * @param objId
	 * @param creater
	 * @return java.util.List<io.train.modules.oss.entity.SysOssEntity>
	 * @exception:
	 * @update:
	 * @updatePerson:
	 **/
	List<SysOssEntity> getSysOssList(String objId, String creater);
}
