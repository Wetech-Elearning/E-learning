package io.train.modules.business.course.dao;

import io.train.modules.business.course.entity.CourseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:39:34
 */
@Mapper
public interface CourseDao extends BaseMapper<CourseEntity> {
	
	 public List<CourseEntity> getCourseEntityByClassId(String classId,@Param(value="date") String date);
	 
	 public List<CourseEntity> getPageByStudent(String studentId,int start,int limit,@Param(value="date") String date);
	 
	 public List<CourseEntity> getPageByStudentCouseType(@Param(value="studentId") String studentId,@Param(value="courseType")String courseType,@Param(value="date") String date);
	 
	 public CourseEntity  getPageByStudentCourseId(@Param(value="studentId") String studentId,@Param(value="uuid")String uuid,@Param(value="date") String date);
	 
}
