package io.train.modules.business.relation.service.impl;

import io.train.modules.business.relation.dao.ClassStuentDao;
import io.train.modules.business.relation.entity.ClassStudentEntity;
import io.train.modules.business.relation.service.ClassStuentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;



@Service("classStuentService")
public class ClassStuentServiceImpl extends ServiceImpl<ClassStuentDao, ClassStudentEntity> implements ClassStuentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ClassStudentEntity> page = this.page(
                new Query<ClassStudentEntity>().getPage(params),
                new QueryWrapper<ClassStudentEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ClassStudentEntity> getClassStuentEntityByStudentId(Long studentId) {
        QueryWrapper<ClassStudentEntity> queryWrapper = new QueryWrapper<ClassStudentEntity>();
        queryWrapper.eq("user_id", studentId);
        queryWrapper.eq("delete_flag",0);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ClassStudentEntity> getClassStuentEntityByClassId(Long classId) {
        QueryWrapper<ClassStudentEntity> queryWrapper = new QueryWrapper<ClassStudentEntity>();
        queryWrapper.eq("class_id", classId);
        queryWrapper.eq("delete_flag",0);
        return this.baseMapper.selectList(queryWrapper);
    }
}