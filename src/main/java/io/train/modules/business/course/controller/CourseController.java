package io.train.modules.business.course.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.train.modules.business.course.entity.CourseEntity;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.business.course.vo.CourseDictVO;
import io.train.modules.oss.entity.SysOssEntity;
import io.train.modules.oss.service.AwsService;
import io.train.modules.oss.service.SysOssService;
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
 * @date 2021-07-05 10:39:34
 */
@RestController
@RequestMapping("generator/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @Autowired
    SysOssService sysOssService;

    @Autowired
    AwsService awsService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:course:list")
    //@RequiresPermissions("business:course:course:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = courseService.queryPage(params);

        return R.ok().put("page", page);
    }
    /**
     * 列表
     */
    @RequestMapping("/listByCourseType")
    public R listByCourseType(@RequestParam Map<String, Object> params){
    	String couseTypeId=""+params.get("relatedCourseTypeId");
    	List<CourseEntity> page = courseService.getCourseEntityByCourseType(couseTypeId);

    	return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:course:info")
    public R info(@PathVariable("uuid") String uuid){
		CourseEntity course = courseService.getById(uuid);
        SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",course.getUuid()));
        if(null != sysOssEntity){
            course.setFileUrl(awsService.downFile(sysOssEntity.getOssFileName()));
        }
        return R.ok().put("course", course);
    }

    @RequestMapping("/infos/{uuid}")
    @RequiresPermissions("business:course:info")
    public R infos(@PathVariable("uuid") String uuid,@RequestParam Map<String, Object> params){
    	String studentId = ""+params.get("studentId");
    	CourseEntity course = courseService.getPageByStudentCourseId(uuid,studentId);
    	SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",course.getUuid()));
    	if(null != sysOssEntity){
    		course.setFileUrl(awsService.downFile(sysOssEntity.getOssFileName()));
    	}
    	return R.ok().put("course", course);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:course:add")
    public R save(@RequestBody CourseEntity course){
		courseService.save(course);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:course:update")
    public R update(@RequestBody CourseEntity course){
        if("".equals(course.getCertificateId())){
            course.setCertificateName("");
        }
		courseService.updateById(course);

        return R.ok();
    }

    /**
     * 发布
     */
    @RequestMapping("/release/{uuid}")
    public R release(@PathVariable("uuid") String uuid){
        courseService.release(uuid);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:course:delete")
    public R delete(@RequestBody String[] uuids){
		courseService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

    /**
     * 初始化课程枚举
     * @author: liyajie
     * @date: 2021/7/28 23:41
     * @param
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     */
    @RequestMapping("/initCourseOptions")
    @RequiresPermissions("business:course:course:initCourseOptions")
    public R initCourseOptions(){
        List<CourseEntity> courseEntityList =  courseService.list();
        List<CourseDictVO> courseDictVOS = new ArrayList<CourseDictVO>();
        courseEntityList.stream().forEach((CourseEntity courseEntity)->{
            CourseDictVO courseDictVO = new CourseDictVO();
            courseDictVO.setId(courseEntity.getUuid());
            courseDictVO.setValue(courseEntity.getCourseName());
            courseDictVOS.add(courseDictVO);
        });
        return R.ok().put("data",courseDictVOS);
    }
}
