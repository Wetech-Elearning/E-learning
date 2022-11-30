package io.train.modules.business.paper.dao;

import io.train.modules.business.paper.entity.ExamPaperInfosEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:51:50
 */
@Mapper
public interface ExamPaperInfosDao extends BaseMapper<ExamPaperInfosEntity> {
	
	
	public List<ExamPaperInfosEntity> listByUser(@Param("userId") String userId,@Param("examPaperName") String examPaperName,@Param(value="date") String date
			,@Param("start")int start,@Param("limit")int limit);
	
	public int listByUserCount(@Param("userId") String userId,@Param("examPaperName") String examPaperName);
	
	public List<ExamPaperInfosEntity> listByClassId(@Param("classId")String classId,@Param(value="date")String date);
	
}
