package io.train.modules.business.certificateinfo.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.train.modules.business.certificateinfo.dto.CertificatePreviewDto;
import io.train.modules.business.certificateinfo.entity.CertificateInfoEntity;
import io.train.modules.business.certificateinfo.entity.CertificateUserInfoEntity;
import io.train.modules.business.certificateinfo.service.CertificateInfoService;
import io.train.modules.business.certificateinfo.service.CertificateUserInfoService;
import io.train.modules.business.course.entity.CourseEntity;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.lecturer.entity.LecturerEntity;
import io.train.modules.business.lecturer.service.LecturerService;
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
@RequestMapping("generator/certificateinfo")
public class CertificateInfoController {
    @Autowired
    private CertificateInfoService certificateInfoService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseTypeService courseTypeService;
    
    @Autowired
    private LecturerService lecturerService;
    
    @Autowired
    private CertificateUserInfoService certificateUserInfoService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:certificateinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = certificateInfoService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 查询所有证书
     * @author: liyajie
     * @date: 2022/2/11 16:14
     * @param params
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/listall")
    public R listAll(@RequestParam Map<String, Object> params){
    	List<CertificateInfoEntity> certificateInfoEntityList = certificateInfoService.listAll(params);
    	return R.ok().put("data", certificateInfoEntityList);
    }

    /**
     * 查询未关联的二级课程的证书
     * @author: liyajie 
     * @date: 2022/6/30 11:31
     * @param params
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/getUnRelatedCertificateInfoEntityList")
    public R getUnRelatedCertificateInfoEntityList(@RequestParam Map<String, Object> params){
        List<CertificateInfoEntity> certificateInfoEntityList = certificateInfoService.getUnRelatedCertificateInfoEntityList(params);
        return R.ok().put("data", certificateInfoEntityList);
    }
    

    /**
     * 根据用户查询证书
     * @author: liyajie
     * @date: 2022/2/11 16:15
     * @param params
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/listByUser")
    public R listByUser(@RequestParam Map<String, Object> params){
    	String userId = ""+params.get("userId");
    	List<CertificateInfoEntity> certificateInfoEntityList = certificateInfoService.listByUser(userId);

    	return R.ok().put("data", certificateInfoEntityList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:certificateinfo:info")
    public R info(@PathVariable("uuid") String uuid){
		CertificateInfoEntity certificateInfo = certificateInfoService.getById(uuid);
        LecturerEntity lecturerEntity = lecturerService.getByAccount(certificateInfo.getCreater());
        if(null != lecturerEntity){
            certificateInfo.setCreater(lecturerEntity.getSurname() + lecturerEntity.getLecturerName());
        }
        List<CourseTypeEntity> courseTypeEntityList = courseTypeService.getTwoLevelByCertificateId(new String[]{String.valueOf(certificateInfo.getUuid())});
        if(null != courseTypeEntityList && courseTypeEntityList.size() > 0){
            certificateInfo.setCourseName(courseTypeEntityList.get(0).getCourseTypeName());
        }
        return R.ok().put("certificateInfo", certificateInfo);
    }

    /**
     * 信息
     */
    @RequestMapping("/preview")
    @RequiresPermissions("business:certificateinfo:info")
    public R preview(@RequestBody CertificatePreviewDto certificatePreviewDto){
        //证书创建者
        CertificateInfoEntity certificateInfo = certificateInfoService.getById(certificatePreviewDto.getUuid());
        LecturerEntity lecturerEntity = lecturerService.getByAccount(certificateInfo.getCreater());
        if(null != lecturerEntity){
            certificateInfo.setCreater(lecturerEntity.getSurname() + lecturerEntity.getLecturerName());
        }
        //课程名称
        List<CourseTypeEntity> courseTypeEntityList = courseTypeService.getTwoLevelByCertificateId(new String[]{String.valueOf(certificateInfo.getUuid())});
        if(null != courseTypeEntityList && courseTypeEntityList.size() > 0){
            certificateInfo.setCourseName(courseTypeEntityList.get(0).getCourseTypeName());
        }
        //获取证书的时间
        CertificateUserInfoEntity certificateUserInfoEntity = certificateUserInfoService.getByUserIdAndCertificateId(certificatePreviewDto.getUserId(),certificatePreviewDto.getUuid());
        if(null != certificateUserInfoEntity){
            certificateInfo.setCreateDate(certificateUserInfoEntity.getCreateDate());
        }
        return R.ok().put("certificateInfo", certificateInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:certificateinfo:add")
    public R save(@RequestBody CertificateInfoEntity certificateInfo){
		certificateInfoService.save(certificateInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:certificateinfo:update")
    public R update(@RequestBody CertificateInfoEntity certificateInfo){
		certificateInfoService.updateById(certificateInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:certificateinfo:delete")
    public R delete(@RequestBody String[] uuids){
        // 校验证书是否关联了课程包，已关联的禁止删除
        List<CourseTypeEntity> courseTypeEntityList = courseTypeService.getTwoLevelByCertificateId(uuids);
        if(null != courseTypeEntityList && courseTypeEntityList.size() > 0){
            String errMsg = "";
            for (CourseTypeEntity courseTypeEntity : courseTypeEntityList) {
                errMsg += courseTypeEntity.getCourseTypeName() + "课程包;";
            }
            return R.error(errMsg.substring(0,errMsg.length()-1) + "关联了该证书，禁止删除");
        }
        // 校验证书是否关联了课程，已关联的禁止删除
        List<CourseEntity> courseEntityList = courseService.getByCertificateIds(uuids);
        if(null != courseEntityList && courseEntityList.size() > 0){
            String errMsg = "";
            for (CourseEntity courseEntity : courseEntityList) {
                errMsg += courseEntity.getCourseName() + "课程;";
            }
            return R.error(errMsg.substring(0,errMsg.length()-1) + "关联了该证书，禁止删除");
        }
		certificateInfoService.removeByIds(Arrays.asList(uuids));
        return R.ok();
    }

}
