package io.train.modules.business.certificateinfo.dao;

import io.train.modules.business.certificateinfo.entity.CertificateInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:51:01
 */
@Mapper
public interface CertificateInfoDao extends BaseMapper<CertificateInfoEntity> {
	
	public List<CertificateInfoEntity> getCertificateInfoEntityByUser(@Param(value="userId") String userId);
	
}
