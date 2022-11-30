package io.train.modules.business.translate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.translate.entity.TranslateEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-08-30 23:55:05
 */
public interface TranslateService extends IService<TranslateEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 删除
     * @author: liyajie
     * @date: 2021/8/30 0:14
     * @param uuids
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void removeByIds(List<String> uuids);

    /**
     * 根据所属课程id删除
     * @author: liyajie
     * @date: 2021/8/30 0:14
     * @param relatedCourseId
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void removeByRelatedCourseId(String relatedCourseId);

    /**
     * 根据所属课程id查询中日文翻译
     * @author: liyajie
     * @date: 2021/9/14 1:29
     * @param relatedCourseId
     * @return java.util.List<io.train.modules.business.common.translate.entity.TranslateEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<TranslateEntity> getTranslateEntityByRelatedCourseId(String relatedCourseId);
}

