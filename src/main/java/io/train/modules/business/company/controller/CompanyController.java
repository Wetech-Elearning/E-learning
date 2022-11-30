package io.train.modules.business.company.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.train.config.MyContext;
import io.train.modules.business.company.entity.CompanyEntity;
import io.train.modules.business.company.service.CompanyService;
import io.train.modules.business.company.vo.CompanyDictVO;
import io.train.modules.business.organization.entity.OrganizationEntity;
import io.train.modules.business.organization.service.OrganizationService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("generator/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private MyContext myContext;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:company:company:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = companyService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    //@RequiresPermissions("business:company:company:info")
    public R info(@PathVariable("uuid") String uuid){
		CompanyEntity company = companyService.getById(uuid);

        return R.ok().put("company", company);
    }

    /**
     * 信息
     */
    @RequestMapping("/getCompanyInfo/{uuid}")
    public R getCompanyInfo(@PathVariable("uuid") String uuid){
        CompanyEntity company = companyService.getById(uuid);

        return R.ok().put("company", company);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:company:company:save")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public R save(@RequestBody CompanyEntity company){
        CompanyEntity companyEntity = companyService.getByCompanyName(company.getCompanyName());
        if(null != companyEntity){
            return R.error("该企业名称已存在");
        }
        companyService.save(company);
        myContext.setCurrentTenantId(Long.parseLong(company.getUuid()));
		//将公司信息保存到组织信息
        OrganizationEntity organizationEntity = new OrganizationEntity();
        organizationEntity.setUuid(Long.parseLong(company.getUuid()));
        organizationEntity.setOrgName(company.getCompanyName());
        organizationEntity.setOrgType("company");
        organizationEntity.setParentOrg("0");
        organizationEntity.setCreater(company.getCreater());
        organizationEntity.setCreateDate(company.getCreateDate());
        organizationEntity.setUpdater(company.getUpdater());
        organizationEntity.setUpdateDate(company.getUpdateDate());
        organizationEntity.setRemark(company.getRemark());
        organizationEntity.setDeleteFlag(company.getDeleteFlag());
        organizationService.save(organizationEntity);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:company:company:update")
    public R update(@RequestBody CompanyEntity company){
		companyService.updateCompany(company);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:company:company:delete")
    public R delete(@RequestBody String[] uuids){
		companyService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

    /**
     * 初始化所属企业枚举
     * @author: liyajie
     * @date: 2021/7/18 14:57
     * @param
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     */
    @RequestMapping("/initCompanyOptions")
    public R initCompanyOptions(){
        List<CompanyEntity> companyEntityList =  companyService.initCompanyOptions();
        List<CompanyDictVO> companyDictVOList = new ArrayList<CompanyDictVO>();
        companyEntityList.stream().forEach((CompanyEntity companyEntity)->{
            CompanyDictVO companyDictVO = new CompanyDictVO();
            companyDictVO.setId(Long.parseLong(companyEntity.getUuid()));
            companyDictVO.setValue(companyEntity.getCompanyName());
            companyDictVOList.add(companyDictVO);
        });
        return R.ok().put("data",companyDictVOList);
    }

    /**
     * 根据用户id查询所属公司
     * @author: liyajie
     * @date: 2022/3/2 9:48
     * @param userId
     * @return io.train.modules.business.common.company.entity.CompanyEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/getByUserId/{userId}")
    public R getByUserId(@PathVariable("userId") String userId){
        CompanyEntity company = companyService.getByUserId(userId);
        return R.ok().put("company", new CompanyDictVO(Long.parseLong(company.getUuid()),company.getCompanyName()));
    }

}
