package io.train.modules.business.genernal.service;

import java.util.List;
import java.util.Map;

public interface GenernalService {

	public List<Map<String,Object>> getWeekGenerinfos(String studentId,String time );
	public List<Map<String,Object>> getWeekGenerinfosOrder(String studentId,String time );
	public Map<String,Object> cirClueTotalinfosOrder(String studentId);
}
