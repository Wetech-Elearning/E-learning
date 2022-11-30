package io.train.modules.business.classes.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.train.modules.business.classes.entity.ClassesCourseEntity;
import io.train.modules.business.classes.entity.ClassesEntity;
import io.train.modules.business.classes.service.ClassesCourseService;
import io.train.modules.business.classes.service.ClassesService;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;



/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 11:24:08
 */
@RestController
@RequestMapping("generator/classes")
public class ClassesController {
    @Autowired
    private ClassesService classesService;
    @Autowired
    private ClassesCourseService classesCourseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:classes:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = classesService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:classes:info")
    public R info(@PathVariable("uuid") String uuid){
		ClassesEntity classes = classesService.getById(uuid);
        return R.ok().put("classes", classes);
    }
    
    /**
     * 查看班级信息
     * @param uuid
     * @return
     */
    @RequestMapping("/classInfos/{uuid}")
    @RequiresPermissions("business:classes:info")
    public R classInfos(@PathVariable("uuid") Long uuid){
    	ClassesEntity classParams = new ClassesEntity();
    	classParams.setUuid(uuid);
		ClassesEntity classes = classesService.getStudentList(classParams);
        return R.ok().put("classes", classes);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:classes:add")
    public R save(@RequestBody ClassesEntity classes){
		classesService.save(classes);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:classes:update")
    public R update(@RequestBody ClassesEntity classes){
		classesService.updateById(classes);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:classes:delete")
    public R delete(@RequestBody String[] uuids){
		classesService.removeByIds(Arrays.asList(uuids));
        return R.ok();
    }
    
    @RequestMapping("/addstudent2Class/{uuid}")
    @RequiresPermissions("business:classes:info")
    public R addStudent2classInfos(@PathVariable("uuid") String uuid,@RequestBody String[] uuids){
    	int saveTotal = 0;
    	for(int i=0;i<uuids.length;i++){
    		String[] studentids = new String[]{uuids[i]};
    		try {
    			saveTotal = classesService.addStudent2Class(uuid,studentids);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	Map<String,String> result = new HashMap<String,String>();
    	result.put("total", ""+uuids.length);
    	result.put("saved", ""+saveTotal);
        return R.ok().put("data", result);
    }
    
    @RequestMapping("/delstudentfromClass/{uuid}")
    @RequiresPermissions("business:classes:info")
    public R delStudent2classInfos(@PathVariable("uuid") String uuid,@RequestBody String[] uuids){
		classesService.delStudentFromClass(uuid,uuids);
        return R.ok();
    }
    
    @RequestMapping("/listcourse/{classuuid}")
    //@RequiresPermissions("business:classes:classes:list")
    public R listCourse(@PathVariable("classuuid") String uuid){
    	Map<String,Object> params = new HashMap<String,Object>();
    	params.put("classId", uuid);
        List<ClassesCourseEntity> list = classesCourseService.listAll(params);
        return R.ok().put("data", list);
    }

    @RequestMapping("/delCoursefromClass/{classuuid}")
    @RequiresPermissions("business:classes:info")
    public R delCourse2classInfos(@PathVariable("classuuid") String classuuid,@RequestBody String[] uuids){
    	classesCourseService.removeByIds(Arrays.asList(uuids));
    	return R.ok();
    }
    /**
     * 保存
     */
    @RequestMapping("/addcourse2Class")
    @RequiresPermissions("business:classes:add")
    public R save(@RequestBody ClassesCourseEntity classes){
    	classesCourseService.save(classes);
    	return R.ok();
    }
    
    /**
     * 保存
     */
    @RequestMapping("/listByStudentId")
//    @RequiresPermissions("business:classes:classes:save")
    public R listByStudentId(@RequestParam Map<String, Object> params){
    	String studentId = ""+params.get("studentId");
    	String className = "";
        if(null != params.get("className") && !"".equals(params.get("className").toString())){
            className = "%" + params.get("className") + "%";
        }
    	List<ClassesEntity> page = classesService.queryPageByStudentId(studentId,className);
        return R.ok().put("data", page);
    }

    @RequestMapping("/listCorusesByStudentId")
    public R listCorusesByStudentId(@RequestParam Map<String, Object> params){
        String studentId = "" + params.get("studentId");
        List<CourseTypeEntity> courseTypeEntityList = classesService.listAllCorusesByStudentId(studentId);
        return R.ok().put("data", courseTypeEntityList);
    }

    /**
     * 学习日程展示 
     * @author: liyajie 
     * @date: 2022/7/7 7:55
     * @param params
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/showFullCalendar")
    public R showFullCalendar(@RequestParam Map<String, Object> params){
        String studentId = "" + params.get("studentId");
        List<ClassesEntity> classesEntityList = classesService.showFullCalendar(studentId);
        return R.ok().put("data", classesEntityList);
    }
}
