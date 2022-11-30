package io.train.modules.business.translate.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.train.modules.business.translate.entity.TranslateEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-08-30 23:55:05
 */
@Mapper
public interface TranslateDao extends BaseMapper<TranslateEntity> {
	
}
