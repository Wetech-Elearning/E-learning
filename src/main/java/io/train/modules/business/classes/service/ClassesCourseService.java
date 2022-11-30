package io.train.modules.business.classes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.classes.entity.ClassesCourseEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 11:24:08
 */
public interface ClassesCourseService extends IService<ClassesCourseEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<ClassesCourseEntity> listAll(Map<String, Object> params);
}

