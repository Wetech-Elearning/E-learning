package io.train.modules.business.certificateinfo.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.train.modules.business.certificateinfo.entity.CertificateUserInfoEntity;
import io.train.modules.business.certificateinfo.service.CertificateInfoService;
import io.train.modules.business.certificateinfo.service.CertificateUserInfoService;

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
 * @date 2021-07-05 10:51:01
 */
@RestController
@RequestMapping("generator/certificateuserinfo")
public class CertificateUserInfoController {
    @Autowired
    private CertificateUserInfoService certificateUserInfoService;

    @Autowired
    private CertificateInfoService certificateInfoService;

    @Autowired
    private CourseTypeService courseTypeService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("business:certificateinfo:certificateinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = certificateUserInfoService.queryPage(params);

        return R.ok().put("page", page);
    }
    @RequestMapping("/listall")
    //@RequiresPermissions("business:certificateinfo:certificateinfo:list")
    public R listAll(@RequestParam Map<String, Object> params){
    	List<CertificateUserInfoEntity> page = certificateUserInfoService.listAll(params);
    	return R.ok().put("data", page);
    }

    @RequestMapping("/listByUser")
    //@RequiresPermissions("business:certificateinfo:certificateinfo:list")
    public R listByUser(@RequestParam Map<String, Object> params){
    	String userId = ""+params.get("userId");
    	List<CertificateUserInfoEntity> page = certificateUserInfoService.listByUser(userId);

    	return R.ok().put("data", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    //@RequiresPermissions("business:certificateinfo:certificateinfo:info")
    public R info(@PathVariable("uuid") String uuid){
    	CertificateUserInfoEntity certificateInfo = certificateUserInfoService.getById(uuid);

        return R.ok().put("certificateInfo", certificateInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("business:certificateinfo:certificateinfo:save")
    public R save(@RequestBody CertificateUserInfoEntity certificateUserInfoEntity){
        CourseTypeEntity courseTypeEntity = courseTypeService.getById(certificateUserInfoEntity.getCourseId());
        if(null != courseTypeEntity){
            certificateUserInfoEntity.setCertificateUuid(courseTypeEntity.getCertificateId());
        }
        //关联课程包证书
        certificateUserInfoEntity.setCreateDate(new Date());
        certificateUserInfoService.save(certificateUserInfoEntity);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("business:certificateinfo:certificateinfo:update")
    public R update(@RequestBody CertificateUserInfoEntity certificateInfo){
        certificateUserInfoService.updateById(certificateInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:certificateinfo:certificateinfo:delete")
    public R delete(@RequestBody String[] uuids){
        certificateUserInfoService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

}
