package io.train.modules.job.task;

import io.train.modules.business.course.entity.CourseEntity;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.record.entity.StudentStudyRecordEntity;
import io.train.modules.business.record.service.StudentStudyRecordService;
import io.train.modules.business.relation.entity.ClassCourseEntity;
import io.train.modules.business.relation.entity.ClassStudentEntity;
import io.train.modules.business.relation.service.ClassCourseService;
import io.train.modules.business.relation.service.ClassStuentService;
import io.train.modules.business.site.entity.SiteInfoEntity;
import io.train.modules.business.site.service.SiteInfoService;
import io.train.modules.business.student.entity.StudentEntity;
import io.train.modules.business.student.service.StudentService;
import io.train.modules.sys.entity.SysUserEntity;
import io.train.modules.sys.service.SysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/***
 * 站内信定时任务
 * @author: liyajie
 * @version: 1.0
 * @date: 2021/07/18 22:46
 */
@Component("siteInfo")
public class SendSiteInfoTask implements ITask {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    SiteInfoService siteInfoService;
    
    @Autowired
    StudentService studentService;
    
    @Autowired
    ClassStuentService classStuentService;
    
    @Autowired
    ClassCourseService classCourseService;
    
    @Autowired
    CourseTypeService courseTypeService;
    
    @Autowired
    CourseService courseService;
    
    @Autowired
    StudentStudyRecordService studentStudyRecordService;
    
    @Autowired
    SysUserService sysUserService;
    
    
    @Override
    public void run(String params) {
        //发送给学员
        List<StudentEntity> studentEntityList = studentService.list();
        for (StudentEntity studentEntity:studentEntityList) {
            //查询学员的用户id
            SysUserEntity sysUserEntity = sysUserService.queryByAccount(studentEntity.getAccount());
            List<ClassStudentEntity> classStudentEntityList = classStuentService.getClassStuentEntityByStudentId(studentEntity.getUuid());
            if(null != classStudentEntityList && classStudentEntityList.size() > 0){
                for(ClassStudentEntity classStudentEntity : classStudentEntityList){
                    Long classId = classStudentEntity.getClassId();
                    List<ClassCourseEntity> classCourseEntityList = classCourseService.getClassCourseByClassId(classId);
                    if(null != classCourseEntityList && classCourseEntityList.size() > 0){
                        for(ClassCourseEntity classCourseEntity : classCourseEntityList){
                            double totalTime = 0;
                            double studyTime = 0;
                            Long courseTypeId = classCourseEntity.getCourseType();
                            CourseTypeEntity courseTypeEntity = courseTypeService.getById(courseTypeId);
                            List<CourseEntity> courseEntityList = courseService.getCourseEntityByCourseTypeAndStatus(courseTypeEntity.getUuid(),"876ff807b5c84374b101be71ba7efd3f");
                            if(null != courseEntityList && courseEntityList.size() > 0){
                                for(CourseEntity courseEntity : courseEntityList){
                                    StudentStudyRecordEntity studentStudyRecordEntity = studentStudyRecordService.getRecordByUserIdAndCourseId(String.valueOf(studentEntity.getUuid()),courseEntity.getUuid());
                                    if(null != studentStudyRecordEntity){
                                        studyTime = new BigDecimal(studyTime).add(new BigDecimal(studentStudyRecordEntity.getLearnTime())).doubleValue();
                                        totalTime = new BigDecimal(totalTime).add(new BigDecimal(studentStudyRecordEntity.getCourseTotalTime())).doubleValue();
                                    }else {
                                        totalTime = new BigDecimal(totalTime).add(new BigDecimal(courseEntity.getTotalTime())).doubleValue();
                                    }
                                }
                            }else{
                                SiteInfoEntity siteInfo = new SiteInfoEntity();
                                siteInfo.setContent("您当前有课程【" + courseTypeEntity.getCourseTypeName() + "】学习进度已落后，请尽快处理");
                                siteInfo.setSendDate(new Date());
                                siteInfo.setIsAllCompany(1);
                                siteInfo.setSendUserId("1");
                                siteInfo.setType(2);
                                siteInfo.setAcceptUserId(String.valueOf(sysUserEntity.getUserId()));
                                siteInfoService.save(siteInfo);
                                continue;
                            }
                            if(totalTime != 0 && new BigDecimal(studyTime).divide(new BigDecimal(totalTime),2, RoundingMode.HALF_UP).doubleValue() < 0.5){
                                SiteInfoEntity siteInfo = new SiteInfoEntity();
                                siteInfo.setContent("您当前有课程【" + courseTypeEntity.getCourseTypeName() + "】学习进度已落后，请尽快处理");
                                siteInfo.setSendDate(new Date());
                                siteInfo.setIsAllCompany(1);
                                siteInfo.setSendUserId("1");
                                siteInfo.setType(2);
                                siteInfo.setAcceptUserId(String.valueOf(sysUserEntity.getUserId()));
                                siteInfoService.save(siteInfo);
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }
}
