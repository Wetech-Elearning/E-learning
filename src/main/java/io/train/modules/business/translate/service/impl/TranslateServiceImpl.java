package io.train.modules.business.translate.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.business.translate.dao.TranslateDao;
import io.train.modules.business.translate.entity.TranslateEntity;
import io.train.modules.business.translate.service.TranslateService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("translateService")
public class TranslateServiceImpl extends ServiceImpl<TranslateDao, TranslateEntity> implements TranslateService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<TranslateEntity> queryWrapper = new QueryWrapper<TranslateEntity>();
        queryWrapper.eq("delete_flag",0);
        if(null != params.get("creater") && !"".equals(params.get("creater").toString())){
            queryWrapper.eq("creater",params.get("creater"));
        }
        if(null != params.get("relatedCourseId") && !"".equals(params.get("relatedCourseId").toString())){
            queryWrapper.eq("related_course_id",params.get("relatedCourseId"));
        }
        IPage<TranslateEntity> page = this.page(
                new Query<TranslateEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<TranslateEntity> updateWrapper = new UpdateWrapper<TranslateEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public void removeByRelatedCourseId(String relatedCourseId) {
        UpdateWrapper<TranslateEntity> updateWrapper = new UpdateWrapper<TranslateEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("related_course_id",relatedCourseId);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public List<TranslateEntity> getTranslateEntityByRelatedCourseId(String relatedCourseId) {
        QueryWrapper<TranslateEntity> queryWrapper = new QueryWrapper<TranslateEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("related_course_id",relatedCourseId);
        return this.baseMapper.selectList(queryWrapper);
    }
}