package io.train.modules.business.coursetype.controller;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.train.modules.business.collection.entity.CollectionInfoEntity;
import io.train.modules.business.collection.service.CollectionInfoService;
import io.train.modules.business.company.service.CompanyService;
import io.train.modules.business.course.entity.CourseEntity;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.lecturer.entity.LecturerEntity;
import io.train.modules.business.lecturer.service.LecturerService;
import io.train.modules.business.paper.entity.ExamPaperInfosEntity;
import io.train.modules.business.paper.service.ExamPaperInfosService;
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
 * @date 2021-07-05 10:40:30
 */
@RestController
@RequestMapping("generator/coursetype")
public class CourseTypeController {
    @Autowired
    private CourseTypeService courseTypeService;

    @Autowired
    public SysOssService sysOssService;

    @Autowired
    public CourseService courseService;

    @Autowired
    public CompanyService companyService;

    @Autowired
    public ExamPaperInfosService examPaperInfosService;

    @Autowired
    public CollectionInfoService collectionInfoService;

    @Autowired
    AwsService awsService;
    
    @Autowired
    LecturerService lecturerService;

    /**
     * ??????
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:coursetype:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = courseTypeService.queryPage(params);
        
        return R.ok().put("page", page);
    }

    /**
     * ??????
     */
    @RequestMapping("/showHome")
    public R list2(@RequestParam Map<String, Object> params){
        Map<String,Object> map = courseTypeService.showHome();

        return R.ok().put("data", map);
    }

    @RequestMapping("/listall")
    public R listAll(@RequestParam Map<String, Object> params){
    	List<CourseTypeEntity> data = courseTypeService.getAllCouseType(params);
        return R.ok().put("data", data);
    }

    @RequestMapping("/listallBynode")
    public R listallBynode(@RequestParam Map<String, Object> params){
    	List<CourseTypeEntity> data = courseTypeService.getAllCouseType(params);
    	List<CourseTypeEntity> result = new ArrayList<CourseTypeEntity>();
    	for(int i=0;i<data.size();i++){
    		if(!(Long.parseLong(data.get(i).getParentCourseType())>0)){
    			result.add(data.get(i));
    		}
    	}
        return R.ok().put("data", result);
    }

    /**
     * ?????????????????????
     * @author: liyajie
     * @date: 2021/9/1 22:58
     * @param params
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/listOneLevelCourseType")
    @RequiresPermissions("business:coursetype:list")
    public R listOneLevelCourseType(@RequestParam Map<String, Object> params){
        List<CourseTypeEntity> data = courseTypeService.listOneLevelCourseType(params);
        return R.ok().put("data", data);
    }

    /**
     * ?????????????????????
     * @author: liyajie
     * @date: 2021/9/1 22:58
     * @param params
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/listTwoLevelCourseType")
    @RequiresPermissions("business:coursetype:list")
    public R listTwoLevelCourseType(@RequestParam Map<String, Object> params){
        List<CourseTypeEntity> data = courseTypeService.listTwoLevelCourseType(params);
        return R.ok().put("data", data);
    }

    @RequestMapping("/listByStudent")
    public R listByStudent(@RequestParam Map<String, Object> params){
    	String studentId = ""+params.get("studentId");
    	List<CourseTypeEntity> data = courseTypeService.getAllCouseTypeByStudent(studentId);
    	for(int i=0;i<data.size();i++) {
    		SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",data.get(i).getUuid()));
            if(null != sysOssEntity){
            	data.get(i).setCourseCover(awsService.downFile(sysOssEntity.getOssFileName()));
            }
    	}
        return R.ok().put("data", data);
    }

    @RequestMapping("/listByStudentCourseType")
    public R listByCourseType(@RequestParam Map<String, Object> params){
    	String couseTypeId=""+params.get("relatedCourseTypeId");
    	String studentId = ""+params.get("studentId");
    	CourseTypeEntity data = courseTypeService.getAllCouseTypeByStudent(couseTypeId,studentId);
        SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",data.getUuid()));
        if(null != sysOssEntity){ 
            data.setCourseCover(awsService.downFile(sysOssEntity.getOssFileName()));
        }
        //??????????????????
        LecturerEntity lecturerEntity = lecturerService.getByAccount(data.getCreater());
        if(null != lecturerEntity){
            data.setCreater(lecturerEntity.getSurname() + lecturerEntity.getLecturerName());
        }
    	return R.ok().put("data", data);
    }

    /**
     * ??????
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:coursetype:info")
    public R info(@PathVariable("uuid") String uuid){
		CourseTypeEntity courseType = courseTypeService.getById(uuid);
        SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",courseType.getUuid()));
        if(null != sysOssEntity){
            courseType.setCourseCover(awsService.downFile(sysOssEntity.getOssFileName()));
        }
        //??????????????????
        LecturerEntity lecturerEntity = lecturerService.getByAccount(courseType.getCreater());
        if(null != lecturerEntity){
            courseType.setCreater(lecturerEntity.getSurname() + lecturerEntity.getLecturerName());
        }
        return R.ok().put("courseType", courseType);
    }

    @RequestMapping("/courseDetail")
    @RequiresPermissions("business:coursetype:info")
    public R courseDetail(@RequestParam Map<String, Object> params){
        String courseId = params.get("courseId").toString();
        String userId = params.get("userId").toString();
        Map map = new HashMap();
        //???????????????
        CourseTypeEntity courseType = courseTypeService.getById(params.get("courseId").toString());
        //??????????????????
        LecturerEntity lecturerEntity = lecturerService.getByAccount(courseType.getCreater());
        if(null != lecturerEntity){
            courseType.setCreater(lecturerEntity.getSurname() + lecturerEntity.getLecturerName());
        }
        //????????????
        SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",courseType.getUuid()));
        if(null != sysOssEntity){
            courseType.setCourseCover(awsService.downFile(sysOssEntity.getOssFileName()));
        }
        //??????????????????
        CollectionInfoEntity collectionInfoEntity = collectionInfoService.getByUserIdAndCourseId(userId,courseId);
        if(null != collectionInfoEntity){
            courseType.setCollectFlag(true);
        }else{
            courseType.setCollectFlag(false);
        }
        map.put("courseType",courseType);
        //?????????????????????
        QueryWrapper<CourseEntity> queryWrapper = new QueryWrapper<CourseEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("related_course_type_id",courseId);
        queryWrapper.eq("status","876ff807b5c84374b101be71ba7efd3f");
        List<CourseEntity> courseEntityList = courseService.list(queryWrapper);
        if(courseEntityList.size() > 0){
            courseEntityList.stream().forEach((CourseEntity courseEntity)->{
                SysOssEntity sysOssEntity1 = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",courseEntity.getUuid()));
                if(null != sysOssEntity1){
                    courseEntity.setFileUrl(awsService.downFile(sysOssEntity1.getOssFileName()));
                }
            });
        }
        List<CourseTypeEntity> leveldata = courseTypeService.getByParentCouseType(courseId);
        List<ExamPaperInfosEntity> examPaperList = new ArrayList<ExamPaperInfosEntity>();
        /*if(leveldata.size()>0) {
        	for(int i=0;i<leveldata.size();i++) {
        		examPaperList.addAll(examPaperInfosService.getExamPaperInfosEntityBySubject(leveldata.get(i).getUuid()));
        	}
        }*/
        examPaperList.addAll(examPaperInfosService.getExamPaperInfosEntityBySubject(courseId));
        //????????????
        map.put("courseList",courseEntityList);
        map.put("examPaperList",examPaperList);
        return R.ok().put("data", map);
    }

    /**
     * ??????
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:coursetype:add")
    public R save(@RequestBody CourseTypeEntity courseType){
		courseTypeService.save(courseType);

        return R.ok();
    }

    /**
     * ??????
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:coursetype:update")
    public R update(@RequestBody CourseTypeEntity courseType){
        if("".equals(courseType.getCertificateId())){
            courseType.setCertificateName("");
        }
		courseTypeService.updateById(courseType);

        return R.ok();
    }

    /**
     * ??????
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:coursetype:delete")
    public R delete(@RequestBody String[] uuids){
		courseTypeService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

}
