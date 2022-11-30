package io.train.modules.business.questionpark.dao;

import io.train.modules.business.questionpark.entity.QuestionParkContent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:41:24
 */
@Mapper
public interface QuestionParkDao extends BaseMapper<QuestionParkContent> {
	
}
