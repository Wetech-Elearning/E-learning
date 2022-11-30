package io.train.modules.business.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.exam.entity.ExamInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:52:42
 */
public interface ExamInfoService extends IService<ExamInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    
    List<ExamInfoEntity> queryAll(Map<String, Object> params);
}

