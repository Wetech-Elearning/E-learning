package io.train.modules.business.collection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.collection.entity.CollectionInfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:12:46
 */
public interface CollectionInfoService extends IService<CollectionInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据课程id和用id查询课程是否被收藏
     * @author: liyajie
     * @date: 2021/10/18 7:42
     * @param userId
     * @param courseId
     * @return io.train.modules.business.common.collection.entity.CollectionInfoEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    CollectionInfoEntity getByUserIdAndCourseId(String userId,String courseId);
}

