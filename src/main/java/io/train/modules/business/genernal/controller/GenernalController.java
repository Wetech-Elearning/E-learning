package io.train.modules.business.genernal.controller;

import java.util.List;
import java.util.Map;

import io.train.modules.business.genernal.service.GenernalService;
import io.train.modules.business.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.train.common.utils.R;

@Controller
@RequestMapping("genernal/genertotal")
public class GenernalController {
	
	@Autowired
	private GenernalService genernalService;
	@Autowired
	private StudentService studentService;

	@RequestMapping("/lineByWeekinfos")
	@ResponseBody
    public R list(@RequestParam Map<String, Object> params){
		String studentId = ""+params.get("userId");
		String time = ""+params.get("time");
		List<Map<String,Object>> list = genernalService.getWeekGenerinfos(studentId,time);
        return R.ok().put("data", list);
    }
	
	@RequestMapping("/lineByWeekinfosOrder")
	@ResponseBody
    public R lineByWeekinfosOrder(@RequestParam Map<String, Object> params){
		String studentId = ""+params.get("userId");
		String time = ""+params.get("time");
		List<Map<String,Object>> list = genernalService.getWeekGenerinfosOrder(studentId,time);
        return R.ok().put("data", list);
    }
	
	@RequestMapping("/cirClueTotalinfosOrder")
	@ResponseBody
	public R cirClueTotalinfosOrder(@RequestParam Map<String, Object> params){
		String studentId = ""+params.get("userId");
		Map<String,Object> list = genernalService.cirClueTotalinfosOrder(studentId);
		return R.ok().put("data", list);
	}
	
	
}
