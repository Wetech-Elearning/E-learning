package io.train.modules.business.questionpark.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.questionpark.entity.QuestionParkContent;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:41:24
 */
public interface QuestionParkService extends IService<QuestionParkContent> {

    PageUtils queryPage(Map<String, Object> params);
}

