package io.train.modules.business.rate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.modules.business.rate.dao.CourseRateDao;
import io.train.modules.business.rate.entity.CourseRateEntity;
import io.train.modules.business.rate.service.CourseRateService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("courseRateService")
public class CourseRateServiceImpl extends ServiceImpl<CourseRateDao, CourseRateEntity> implements CourseRateService {
    @Override
    public List<CourseRateEntity> getRateByCourseId(Long courseId) {
        QueryWrapper<CourseRateEntity> queryWrapper = new QueryWrapper<CourseRateEntity>();
        queryWrapper.eq("course_id", courseId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void saveRate(CourseRateEntity courseRate) {
        QueryWrapper<CourseRateEntity> queryWrapper = new QueryWrapper<CourseRateEntity>();
        queryWrapper.eq("course_id", courseRate.getCourseId());
        queryWrapper.eq("creater", courseRate.getCreater());
        CourseRateEntity courseRateEntity = this.baseMapper.selectOne(queryWrapper);
        if(null != courseRateEntity){
            UpdateWrapper<CourseRateEntity> updateWrapper = new UpdateWrapper<CourseRateEntity>();
            updateWrapper.eq("course_id", courseRate.getCourseId());
            updateWrapper.eq("creater", courseRate.getCreater());
            updateWrapper.set("rate",courseRate.getRate());
            this.baseMapper.update(null,updateWrapper);
        }else{
            this.baseMapper.insert(courseRate);
        }
    }
}