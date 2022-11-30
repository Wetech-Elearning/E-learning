package io.train.modules.business.collection.service.impl;

import io.train.modules.business.collection.dao.CollectionInfoDao;
import io.train.modules.business.collection.entity.CollectionInfoEntity;
import io.train.modules.business.collection.service.CollectionInfoService;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;


@Service("collectionInfoService")
public class CollectionInfoServiceImpl extends ServiceImpl<CollectionInfoDao, CollectionInfoEntity> implements CollectionInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CollectionInfoEntity> page = this.page(
                new Query<CollectionInfoEntity>().getPage(params),
                new QueryWrapper<CollectionInfoEntity>().orderByDesc("create_date")
        );

        return new PageUtils(page);
    }

    @Override
    public CollectionInfoEntity getByUserIdAndCourseId(String userId, String courseId) {
        QueryWrapper queryWrapper = new QueryWrapper<CollectionInfoEntity>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("course_id",courseId);
        return this.baseMapper.selectOne(queryWrapper);
    }
}