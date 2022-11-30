package io.train.modules.business.coursereport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.models.auth.In;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.business.coursereport.dao.CourseReportRecordDao;
import io.train.modules.business.coursereport.entity.CourseReportEntity;
import io.train.modules.business.coursereport.entity.CourseReportRecordEntity;
import io.train.modules.business.coursereport.service.CourseReportRecordService;
import io.train.modules.business.coursereport.service.CourseReportService;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.student.entity.StudentEntity;
import io.train.modules.business.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("courseReportRecordService")
public class CourseReportRecordServiceImpl extends ServiceImpl<CourseReportRecordDao, CourseReportRecordEntity> implements CourseReportRecordService {

    @Autowired
    CourseTypeService courseTypeService;
    
    @Autowired
    CourseReportService courseReportService;
    
    @Autowired
    StudentService studentService;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CourseReportRecordEntity> page = this.page(
                new Query<CourseReportRecordEntity>().getPage(params),
                new QueryWrapper<CourseReportRecordEntity>()
        );
        if(page.getRecords().size() > 0){
            page.getRecords().stream().forEach((CourseReportRecordEntity courseReportRecordEntity)->{
                CourseReportEntity courseReportEntity = courseReportService.getById(courseReportRecordEntity.getCourseReportId());
                if(null != courseReportEntity){
                    courseReportRecordEntity.setCourseReportTitle(courseReportEntity.getCourseReportTitle());
                    //翻译二级课程包名称
                    CourseTypeEntity courseTypeEntity = courseTypeService.getById(courseReportEntity.getCourseTypeId());
                    if(null != courseTypeEntity){
                        courseReportRecordEntity.setCourseTypeName(courseTypeEntity.getCourseTypeName());
                    }
                }
                StudentEntity studentEntity = studentService.getById(courseReportRecordEntity.getStudentId());
                if(null != studentEntity){
                    courseReportRecordEntity.setStudentName(studentEntity.getSurname() + studentEntity.getUserName());
                }
            });
        }
        return new PageUtils(page);
    }

    @Override
    public CourseReportRecordEntity getByCourseReportIdAndStudentId(String courseReportId, String studentId) {
        QueryWrapper<CourseReportRecordEntity> queryWrapper = new QueryWrapper<CourseReportRecordEntity>();
        queryWrapper.eq("course_report_id",courseReportId);
        queryWrapper.eq("student_id",studentId);
        queryWrapper.last("limit 1");
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public Integer getSubmitNums(String courseReportId, String studentId) {
        Integer submitNums = 0;
        QueryWrapper<CourseReportRecordEntity> queryWrapper = new QueryWrapper<CourseReportRecordEntity>();
        queryWrapper.eq("course_report_id",courseReportId);
        queryWrapper.eq("student_id",studentId);
        List<CourseReportRecordEntity> reportRecordEntityList = this.baseMapper.selectList(queryWrapper);
        if(null != reportRecordEntityList && reportRecordEntityList.size() > 0){
            submitNums = reportRecordEntityList.size();
        }
        return submitNums;
    }

    @Override
    public void markScore(String courseReportId, String score, String comment) {
        UpdateWrapper<CourseReportRecordEntity> updateWrapper = new UpdateWrapper<CourseReportRecordEntity>();
        updateWrapper.eq("uuid",courseReportId);
        updateWrapper.set("score", score);
        updateWrapper.set("status", 1);
        updateWrapper.set("comment", comment);
        this.baseMapper.update(null,updateWrapper);
    }
}