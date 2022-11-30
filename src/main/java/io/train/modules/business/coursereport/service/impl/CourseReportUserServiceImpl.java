package io.train.modules.business.coursereport.service.impl;

import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.business.coursereport.dao.CourseReportUserDao;
import io.train.modules.business.coursereport.entity.CourseReportUserEntity;
import io.train.modules.business.coursereport.service.CourseReportUserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("courseReportUserService")
public class CourseReportUserServiceImpl extends ServiceImpl<CourseReportUserDao, CourseReportUserEntity> implements CourseReportUserService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CourseReportUserEntity> page = this.page(
                new Query<CourseReportUserEntity>().getPage(params),
                new QueryWrapper<CourseReportUserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CourseReportUserEntity> getByStudentId(String studentId) {
        QueryWrapper<CourseReportUserEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",studentId);
        return this.baseMapper.selectList(queryWrapper);
    }
}