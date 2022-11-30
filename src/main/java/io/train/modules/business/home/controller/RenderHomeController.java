package io.train.modules.business.home.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.home.entity.RenderHomeManager;
import io.train.modules.business.home.service.RenderhomeService;

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
@RequestMapping("generator/renderhome")
public class RenderHomeController {
    @Autowired
    private RenderhomeService  renderhomeService;
    
    @Autowired
    private CourseTypeService courseTypeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = renderhomeService.queryPage(params);
        RenderHomeManager renderHomeManager = null;
        if(page.getList().size()>0) {
        	renderHomeManager = (RenderHomeManager) page.getList().get(0);
        	if(renderHomeManager.getHotCourseTypeList()!=null && renderHomeManager.getHotCourseTypeList().length()>0) {
        		String[] ids = renderHomeManager.getHotCourseTypeList().split(",");
        		renderHomeManager.setHotList(courseTypeService.getByParentCouseTypeByIds(ids));
        	}
        	if(renderHomeManager.getPubCourseTypeList()!=null&&renderHomeManager.getPubCourseTypeList().length()>0) {
        		String[] ids = renderHomeManager.getPubCourseTypeList().split(",");
        		renderHomeManager.setPubList(courseTypeService.getByParentCouseTypeByIds(ids));
        	}
        }
        return R.ok().put("page", page);
    }

    @RequestMapping("/renderHomeConfig")
    public R getInfoList(@RequestParam Map<String, Object> params){
        PageUtils page = renderhomeService.queryPage(params);
        RenderHomeManager renderHomeManager = null;
        if(page.getList().size()>0) {
        	renderHomeManager = (RenderHomeManager) page.getList().get(0);
        	if(renderHomeManager.getHotCourseTypeList().length()>0) {
        		String[] ids = renderHomeManager.getHotCourseTypeList().split(",");
        		renderHomeManager.setHotList(courseTypeService.getByParentCouseTypeByIds(ids));
        	}
        	if(renderHomeManager.getPubCourseTypeList().length()>0) {
        		String[] ids = renderHomeManager.getPubCourseTypeList().split(",");
        		renderHomeManager.setPubList(courseTypeService.getByParentCouseTypeByIds(ids));
        	}
        }
        return R.ok().put("data", renderHomeManager);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:companyadmin:edit")
    public R info(@PathVariable("uuid") String uuid){
		RenderHomeManager renderHomeManager = renderhomeService.getById(uuid);
        return R.ok().put("renderHomeManager", renderHomeManager);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:companyadmin:edit")
    public R save(@RequestBody RenderHomeManager renderHomeManager){
		renderhomeService.save(renderHomeManager);
        return R.ok();
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save/banners")
    @RequiresPermissions("business:companyadmin:edit")
    public R savebanners(@RequestBody RenderHomeManager renderHomeManager){
    	PageUtils page = renderhomeService.queryPage(new HashMap<String,Object>());
    	if(page.getList().size()>0) {
    		RenderHomeManager demo = (RenderHomeManager) page.getList().get(0);
    		demo.setCarouselList(renderHomeManager.getCarouselList());
    		demo.setDeleteFlag("0");
    		renderhomeService.updateById(demo);
    	} else {
    		renderHomeManager.setDeleteFlag("0");
    		renderhomeService.save(renderHomeManager);
    	}
        return R.ok();
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save/hotCoursertype")
    @RequiresPermissions("business:companyadmin:edit")
    public R saveHotCourseType(@RequestBody RenderHomeManager renderHomeManager){
    	PageUtils page = renderhomeService.queryPage(new HashMap<String,Object>());
    	if(page.getList().size()>0) {
    		RenderHomeManager demo = (RenderHomeManager) page.getList().get(0);
    		demo.setHotCourseTypeList(renderHomeManager.getHotCourseTypeList());
    		demo.setDeleteFlag("0");
    		renderhomeService.updateById(demo);
    	} else {
    		renderHomeManager.setDeleteFlag("0");
    		renderhomeService.save(renderHomeManager);
    	}
        return R.ok();
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save/pubCoursertype")
    @RequiresPermissions("business:companyadmin:edit")
    public R savePubCoursertype(@RequestBody RenderHomeManager renderHomeManager){
    	PageUtils page = renderhomeService.queryPage(new HashMap<String,Object>());
    	if(page.getList().size()>0) {
    		RenderHomeManager demo = (RenderHomeManager) page.getList().get(0);
    		demo.setPubCourseTypeList(renderHomeManager.getPubCourseTypeList());
    		demo.setDeleteFlag("0");
    		renderhomeService.updateById(demo);
    	} else {
    		renderHomeManager.setDeleteFlag("0");
    		renderhomeService.save(renderHomeManager);
    	}
        return R.ok();
    }
    
    /**
     * 保存
     */
    @RequestMapping("/save/baseInfo")
    @RequiresPermissions("business:companyadmin:edit")
    public R saveBaseInfos(@RequestBody RenderHomeManager renderHomeManager){
    	PageUtils page = renderhomeService.queryPage(new HashMap<String,Object>());
    	if(page.getList().size()>0) {
    		RenderHomeManager demo = (RenderHomeManager) page.getList().get(0);
    		demo.setLogoName(renderHomeManager.getLogoImage());
    		demo.setLogoImage(renderHomeManager.getLogoImage());
    		demo.setDeleteFlag("0");
    		renderhomeService.updateById(demo);
    	} else {
    		renderhomeService.save(renderHomeManager);
    	}
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:companyadmin:edit")
    public R update(@RequestBody RenderHomeManager renderHomeManager){
    	renderHomeManager.setDeleteFlag("0");
		renderhomeService.updateById(renderHomeManager);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:companyadmin:edit")
    public R delete(@RequestBody String[] uuids){
		renderhomeService.removeByIds(Arrays.asList(uuids));
        return R.ok();
    }
}