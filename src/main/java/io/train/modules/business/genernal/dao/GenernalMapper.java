package io.train.modules.business.genernal.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.annotation.SqlParser;

@Mapper
public interface GenernalMapper {
	
	@SqlParser(filter=true)
	public List<Map<String,Object>> getWeekGenerinfos(@Param("userId") String studentId,@Param("startTime") String startTime,@Param("date") String date);
	
	@SqlParser(filter=true)
	public List<Map<String,Object>> getWeekGenerinfosByClass(@Param("userId") String studentId,@Param("startTime") String startTime,@Param("date") String date);
	
	@SqlParser(filter=true)
	public Map<String,Object> getTotalLeantimeAndCoursetime(@Param("userId") String studentId);

}
