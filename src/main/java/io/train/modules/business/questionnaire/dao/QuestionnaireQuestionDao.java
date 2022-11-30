package io.train.modules.business.questionnaire.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.train.modules.business.questionnaire.entity.QuestionnaireQuestionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-10-24 04:53:55
 */
@Mapper
public interface QuestionnaireQuestionDao extends BaseMapper<QuestionnaireQuestionEntity> {
	
}
