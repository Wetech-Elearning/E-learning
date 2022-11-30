package io.train.modules.business.question.dao;

import io.train.modules.business.question.entity.MultiQuestionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 选择题题库表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-28 00:05:00
 */
@Mapper
public interface MultiQuestionDao extends BaseMapper<MultiQuestionEntity> {
	
}
