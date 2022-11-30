package io.train.modules.business.coursereport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.business.coursereport.dao.CourseReportDao;
import io.train.modules.business.coursereport.entity.CourseReportEntity;
import io.train.modules.business.coursereport.entity.CourseReportUserEntity;
import io.train.modules.business.coursereport.service.CourseReportService;
import io.train.modules.business.coursereport.service.CourseReportUserService;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.relation.entity.ClassCourseEntity;
import io.train.modules.business.relation.entity.ClassStudentEntity;
import io.train.modules.business.relation.service.ClassCourseService;
import io.train.modules.business.relation.service.ClassStuentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("courseReportService")
public class CourseReportServiceImpl extends ServiceImpl<CourseReportDao, CourseReportEntity> implements CourseReportService {

    @Autowired
    CourseTypeService courseTypeService;
    
    @Autowired
    ClassCourseService classCourseService;
    
    @Autowired
    ClassStuentService classStuentService;
    
    @Autowired
    CourseReportUserService courseReportUserService;
    
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<CourseReportEntity> queryWrapper = new QueryWrapper<CourseReportEntity>();
        if(null != params.get("courseReportQuestion") && !"".equals(params.get("courseReportQuestion").toString())){
            queryWrapper.like("course_report_question",params.get("courseReportQuestion"));
        }
        queryWrapper.orderByDesc("create_date");
        IPage<CourseReportEntity> page = this.page(
                new Query<CourseReportEntity>().getPage(params),
                queryWrapper
        );
        if(page.getRecords().size() > 0){
            page.getRecords().stream().forEach((CourseReportEntity courseReportEntity)->{
                //翻译二级课程包名称
                CourseTypeEntity courseTypeEntity = courseTypeService.getById(courseReportEntity.getCourseTypeId());
                if(null != courseTypeEntity){
                    courseReportEntity.setCourseTypeName(courseTypeEntity.getCourseTypeName());
                }
            });
        }
        return new PageUtils(page);
    }

    @Override
    public void release(Long uuid, Long tenantId) {
        List<Long> studentIds = new ArrayList<Long>(); 
        QueryWrapper<CourseReportEntity> queryWrapper = new QueryWrapper<CourseReportEntity>();
        queryWrapper.eq("uuid",uuid);
        CourseReportEntity courseReportEntity = this.baseMapper.selectOne(queryWrapper);
        if(null != courseReportEntity){
            Long courseTypeId = courseReportEntity.getCourseTypeId();
            List<ClassCourseEntity> classCourseEntityList = classCourseService.getClassCourseByCourseTypeId(courseTypeId);
            if(null != classCourseEntityList && classCourseEntityList.size() > 0){
                for (ClassCourseEntity classCourseEntity : classCourseEntityList) {
                    Long classId = classCourseEntity.getClassId();
                    List<ClassStudentEntity> classStudentEntityList = classStuentService.getClassStuentEntityByClassId(classId);
                    if(null != classStudentEntityList && classStudentEntityList.size() > 0){
                        studentIds = classStudentEntityList.stream().map(ClassStudentEntity::getUserId).collect(Collectors.toList());
                    }
                }
            }
        }
        if(studentIds.size() > 0){
            for (Long studentId:studentIds) {
                CourseReportUserEntity courseReportUserEntity = new CourseReportUserEntity();
                courseReportUserEntity.setUserId(String.valueOf(studentId));
                courseReportUserEntity.setCourseReportId(uuid);
                courseReportUserService.save(courseReportUserEntity);
            }
        }
        UpdateWrapper<CourseReportEntity> updateWrapper = new UpdateWrapper<CourseReportEntity>();
        updateWrapper.eq("uuid",uuid);
        updateWrapper.set("status","1");
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public List<CourseReportEntity> listCourseReport(String student, String courseTypeId) {
        List<CourseReportEntity> courseReportEntityList = new ArrayList<CourseReportEntity>();
        List<CourseReportUserEntity> courseReportUserEntityList = courseReportUserService.getByStudentId(student);
        if(null != courseReportUserEntityList && courseReportUserEntityList.size() > 0){
            for (CourseReportUserEntity courseReportUserEntity:courseReportUserEntityList) {
                CourseReportEntity courseReportEntity = this.getById(courseReportUserEntity.getCourseReportId());
                if(courseTypeId.equals(String.valueOf(courseReportEntity.getCourseTypeId()))){
                    courseReportEntityList.add(courseReportEntity);
                }
            }
        }
        return courseReportEntityList;
    }
    
    
}