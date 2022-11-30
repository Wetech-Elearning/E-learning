package io.train.modules.business.relation.service.impl;

import io.train.modules.business.relation.dao.ClassCourseDao;
import io.train.modules.business.relation.entity.ClassCourseEntity;
import io.train.modules.business.relation.service.ClassCourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;


@Service("classCourseService")
public class ClassCourseServiceImpl extends ServiceImpl<ClassCourseDao, ClassCourseEntity> implements ClassCourseService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ClassCourseEntity> page = this.page(
                new Query<ClassCourseEntity>().getPage(params),
                new QueryWrapper<ClassCourseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ClassCourseEntity> getClassCourseByClassId(Long classId) {
        QueryWrapper<ClassCourseEntity> queryWrapper = new QueryWrapper<ClassCourseEntity>();
        queryWrapper.eq("class_id", classId);
        queryWrapper.eq("delete_flag",0);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ClassCourseEntity> getClassCourseByCourseTypeId(Long courseTypeId) {
        QueryWrapper<ClassCourseEntity> queryWrapper = new QueryWrapper<ClassCourseEntity>();
        queryWrapper.eq("course_type", courseTypeId);
        queryWrapper.eq("delete_flag",0);
        return this.baseMapper.selectList(queryWrapper);
    }
}