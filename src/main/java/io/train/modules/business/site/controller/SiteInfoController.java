package io.train.modules.business.site.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.train.config.MyContext;
import io.train.modules.business.lecturer.entity.LecturerEntity;
import io.train.modules.business.lecturer.service.LecturerService;
import io.train.modules.business.site.entity.SiteInfoEntity;
import io.train.modules.business.site.service.SiteInfoService;
import io.train.modules.business.student.entity.StudentEntity;
import io.train.modules.business.student.service.StudentService;
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
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:55:23
 */
@RestController
@RequestMapping("generator/siteinfo")
public class SiteInfoController {
    @Autowired
    private SiteInfoService siteInfoService;

    @Autowired
    private MyContext apiContext;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private StudentService studentService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("business:siteinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = siteInfoService.queryPage(params);
        return R.ok().put("page", page);
    }



    /**
     * 计数
     */
    @RequestMapping("/count")
    public R count(@RequestParam Map<String, Object> params){
        String userId = String.valueOf(params.get("userId"));
        Integer count = siteInfoService.countLikeUserId(userId);

        return R.ok().put("data", count);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:siteinfo:info")
    public R info(@PathVariable("uuid") String uuid){
        SiteInfoEntity siteInfo = siteInfoService.getById(uuid);

        return R.ok().put("siteInfo", siteInfo);
    }

    /**
     * 将站内信更新为已读
     */
    @RequestMapping("/updateSiteInfo/{uuid}")
    public R updateSiteInfo(@PathVariable("uuid") String uuid){
        siteInfoService.updateSiteInfo(uuid);
        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:siteinfo:add")
    public R save(@RequestBody SiteInfoEntity siteInfo){
        //isAllCompany: 0表示发送给全体员工,1发送给单个人员
        if(0 == siteInfo.getIsAllCompany()){
            Long tenantId = apiContext.getCurrentTenantId();
            //发送给讲师
            List<LecturerEntity> lecturerEntityList = lecturerService.list();
            for (LecturerEntity lecturerEntity:lecturerEntityList) {
                siteInfo.setType(1);
                siteInfo.setAcceptUserId(String.valueOf(lecturerEntity.getUuid()));
                siteInfoService.save(siteInfo);
            }
            //发送给学员
            List<StudentEntity> studentEntityList = studentService.list();
            for (StudentEntity studentEntity:studentEntityList) {
                siteInfo.setType(2);
                siteInfo.setAcceptUserId(String.valueOf(studentEntity.getUuid()));
                siteInfoService.save(siteInfo);
            }
        }else {
            siteInfoService.save(siteInfo);
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:siteinfo:update")
    public R update(@RequestBody SiteInfoEntity siteInfo){
        siteInfoService.updateById(siteInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:siteinfo:delete")
    public R delete(@RequestBody String[] uuids){
        siteInfoService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

}
