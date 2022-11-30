package io.train.modules.business.classes.dao;

import io.train.modules.business.classes.entity.ClassesEntity;
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
 * @date 2021-07-05 11:24:08
 */
@Mapper
public interface ClassesDao extends BaseMapper<ClassesEntity> {
	
	public void addStudent2Class(@Param(value="classUuid")String classUuid, @Param(value="studentUuids") Object[] ids);
	
	public void delStudentFromClass(@Param(value="classUuid") String classUuid,@Param(value="studentUuids") String[] studentUuids);
	
	
	public List<Map<String,Object>> searchClassStudentStudent(Map<String,Object> data);
	
	
	public List<ClassesEntity> getListByStudentId(@Param(value="studentId") String studentId,@Param(value = "className") String className,@Param(value="date")String date);
}
