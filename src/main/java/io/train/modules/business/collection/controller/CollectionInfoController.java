package io.train.modules.business.collection.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.train.modules.business.collection.entity.CollectionInfoEntity;
import io.train.modules.business.collection.service.CollectionInfoService;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
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
 * @date 2021-07-05 10:12:46
 */
@RestController
@RequestMapping("generator/collectioninfo")
public class CollectionInfoController {
    @Autowired
    private CollectionInfoService collectionInfoService;

    @Autowired
    private CourseTypeService courseTypeService;
    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:collectioninfo:collectioninfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = collectionInfoService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:collectioninfo:collectioninfo:info")
    public R info(@PathVariable("uuid") String uuid){
		CollectionInfoEntity collectionInfo = collectionInfoService.getById(uuid);

        return R.ok().put("collectionInfo", collectionInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("business:collectioninfo:collectioninfo:save")
    public R save(@RequestBody CollectionInfoEntity collectionInfo){
    	collectionInfo.setCreateDate(new Date());
    	//设置课程名称
        QueryWrapper<CourseTypeEntity> queryWrapper = new QueryWrapper<CourseTypeEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("uuid",collectionInfo.getCourseId());
        CourseTypeEntity courseTypeEntity = courseTypeService.getOne(queryWrapper);
        if(null != courseTypeEntity){
            collectionInfo.setCourseName(courseTypeEntity.getCourseTypeName());
            //设置所属课程包
            collectionInfo.setCourseType(String.valueOf(courseTypeEntity.getParentCourseType()));
        }
		collectionInfoService.save(collectionInfo);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:collectioninfo:collectioninfo:update")
    public R update(@RequestBody CollectionInfoEntity collectionInfo){
		collectionInfoService.updateById(collectionInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:collectioninfo:collectioninfo:delete")
    public R delete(@RequestBody String[] uuids){
		collectionInfoService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

}
