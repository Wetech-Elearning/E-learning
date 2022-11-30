package io.train.modules.business.genernal.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.train.modules.business.genernal.dao.GenernalMapper;
import io.train.modules.business.genernal.service.GenernalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenernalServiceImpl implements GenernalService {

	@Autowired
	private GenernalMapper genernalMapper;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public List<Map<String,Object>> getWeekGenerinfos(String studentId,String time) {
		Date date = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - new Integer(time));
		date = now.getTime();
		return genernalMapper.getWeekGenerinfos(studentId,sdf.format(date),""+new Date().getTime());
	}

	@Override
	public List<Map<String, Object>> getWeekGenerinfosOrder(String studentId, String time) {
		Date date = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(date);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - new Integer(time));
		date = now.getTime();
		return genernalMapper.getWeekGenerinfosByClass(studentId,sdf.format(date),""+new Date().getTime());
	}

	@Override
	public Map<String, Object> cirClueTotalinfosOrder(String studentId) {
		return genernalMapper.getTotalLeantimeAndCoursetime(studentId);
	}

}
