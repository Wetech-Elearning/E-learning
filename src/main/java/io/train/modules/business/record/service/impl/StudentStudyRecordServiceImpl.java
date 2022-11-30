package io.train.modules.business.record.service.impl;

import io.train.modules.business.classes.service.ClassesService;
import io.train.modules.business.course.entity.CourseEntity;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.business.record.dao.StudentStudyRecordDao;
import io.train.modules.business.record.entity.StudentStudyRecordEntity;
import io.train.modules.business.record.service.StudentStudyRecordService;
import io.train.modules.business.student.entity.StudentEntity;
import io.train.modules.business.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;


@Service("studentStudyRecordService")
public class StudentStudyRecordServiceImpl extends ServiceImpl<StudentStudyRecordDao, StudentStudyRecordEntity> implements StudentStudyRecordService {
	@Autowired
    StudentService studentService;

	@Autowired
    ClassesService classesService;

	@Autowired
    CourseService courseService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
		QueryWrapper<StudentStudyRecordEntity> queryWrapper = new QueryWrapper<StudentStudyRecordEntity>();
		if(null != params.get("studentName") && !"".equals(params.get("studentName").toString())){
			List<StudentEntity> studentEntityList = studentService.getStudentLikeName(params.get("studentName").toString());
			queryWrapper.in("user_id",studentEntityList.stream().map(StudentEntity::getUuid).collect(Collectors.toList()));
		}
		queryWrapper.orderByDesc("date_time");
        IPage<StudentStudyRecordEntity> page = this.page(
                new Query<StudentStudyRecordEntity>().getPage(params),
				queryWrapper
        );
		if(page.getRecords().size() > 0){
			for (StudentStudyRecordEntity studentStudyRecordEntity:page.getRecords()) {
				// 翻译学员id
				StudentEntity studentEntity = studentService.getById(studentStudyRecordEntity.getUserId());
				if(null != studentEntity){
					String surname = null == studentEntity.getSurname()? "" : studentEntity.getSurname();
					studentStudyRecordEntity.setUserId(surname + studentEntity.getUserName());
				}
				// 翻译班级id
				//classesService.getById(studentStudyRecordEntity.getc)
				// 翻译课程id
				Map<String, Object>  param = new HashMap<String, Object>();
				param.put("courseTypeId",studentStudyRecordEntity.getCourseId());
				CourseEntity courseEntity = courseService.getById(studentStudyRecordEntity.getCourseId());
				if(null != courseEntity){
					studentStudyRecordEntity.setCourseId(courseEntity.getCourseName());
				}
			}
		}
        return new PageUtils(page);
    }

	@Override
	public void saveOrupdateRecoard(StudentStudyRecordEntity entity) {
		StudentStudyRecordEntity obj = this.getOne(new QueryWrapper<StudentStudyRecordEntity>().eq("course_id", entity.getCourseId()).eq("user_id", entity.getUserId()));
		if(obj!=null){
			entity.setUuid(obj.getUuid());
			this.updateById(entity);
		} else {
			this.save(entity);
		}
	}

	@Override
	public void asyncStudyRecord(StudentStudyRecordEntity studentStudyRecord) {
		this.baseMapper.asyncStudyRecord(studentStudyRecord);
	}

	@Override
	public StudentStudyRecordEntity getRecordByUserIdAndCourseId(String userId, Long courseId) {
    	QueryWrapper<StudentStudyRecordEntity> queryWrapper = new QueryWrapper<StudentStudyRecordEntity>();
    	queryWrapper.eq("user_id",userId);
    	queryWrapper.eq("course_id",courseId);
		return this.baseMapper.selectOne(queryWrapper);
	}
}
