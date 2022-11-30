package io.train.modules.business.organization.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.train.modules.business.organization.entity.OrganizationEntity;
import io.train.modules.business.organization.service.OrganizationService;
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
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:42:16
 */
@RestController
@RequestMapping("generator/organization")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private StudentService studentService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:organization:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = organizationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:organization:info")
    public R info(@PathVariable("uuid") String uuid){
		OrganizationEntity organization = organizationService.getById(uuid);

        return R.ok().put("organization", organization);
    }

    /**
     * 查询指定父级下的字级组织
     * @author: liyajie
     * @date: 2021/8/7 13:01
     * @param parentOrgId-父级组织id
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/getByParentOrgId/{parentOrgId}")
    @RequiresPermissions("business:organization:info")
    public R getByParentOrgId(@PathVariable("parentOrgId") Long parentOrgId){
        List<OrganizationEntity> organizationEntityList = organizationService.getByParentOrgId(parentOrgId);

        return R.ok().put("data", organizationEntityList);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:organization:add")
    public R save(@RequestBody OrganizationEntity organization){
		organizationService.save(organization);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:organization:update")
    public R update(@RequestBody OrganizationEntity organization){
		organizationService.updateById(organization);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:organization:delete")
    public R delete(@RequestBody String[] uuids){
        for (String uuid : uuids) {
            //判断是否有子组织，有子组织的不允许删除
            List<OrganizationEntity> organizationEntityList = organizationService.getByParentOrgId(Long.parseLong(uuid));
            if(null != organizationEntityList && organizationEntityList.size() > 0){
                return R.error("该组织有下级组织，不允许删除");
            }
            //判断科室下是否有学员，如果有的话，不允许删除
            List<StudentEntity> studentEntityList = studentService.getStudentByRelateOffice(Long.parseLong(uuid));
            if(null != studentEntityList && studentEntityList.size() > 0){
                return R.error("该组织有学员，不允许删除");
            }
        }

		organizationService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }
}
