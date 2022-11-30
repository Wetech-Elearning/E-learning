package io.train.modules.business.coursetype.dao;

import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:40:30
 */
@Mapper
public interface CourseTypeDao extends BaseMapper<CourseTypeEntity> {
	
	
	List<CourseTypeEntity> getCouseTypeByStudent(@Param(value="studentId") String studentId,@Param(value="date") String date);
	
	/**
	 * 获取课程包列表
	 * @author: liyajie 
	 * @date: 2022/6/20 18:11
	 * @param paramMap
	 * @return java.util.List<io.train.modules.business.common.coursetype.entity.CourseTypeEntity>
	 * @exception:
	 * @update:
	 * @updatePerson:
	 **/
	List<CourseTypeEntity> getCourseTypeList(Map<String,Object> paramMap);
	
}
