package io.train.modules.business.organization.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.modules.business.company.service.CompanyService;
import io.train.modules.business.organization.dao.OrganizationDao;
import io.train.modules.business.organization.entity.OrganizationEntity;
import io.train.modules.business.organization.service.OrganizationService;
import io.train.modules.sys.entity.SysUserEntity;
import io.train.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;


@Service("organizationService")
public class OrganizationServiceImpl extends ServiceImpl<OrganizationDao, OrganizationEntity> implements OrganizationService {

    @Autowired
    CompanyService companyService;

    @Autowired
    SysUserService sysUserService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //获取企业信息
        String userId = String.valueOf(params.get("userId"));
        SysUserEntity sysUserEntity = sysUserService.getById(userId);
        QueryWrapper<OrganizationEntity> queryWrapper = new QueryWrapper<OrganizationEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("org_type","company");
        queryWrapper.eq("uuid",sysUserEntity.getTenantId());
        if(null != params.get("orgName") && !"".equals(params.get("orgName").toString())){
            queryWrapper.like("org_name",params.get("orgName"));
        }
        //查询组织信息
        IPage<OrganizationEntity> page = this.page(
                new Query<OrganizationEntity>().getPage(params),
                queryWrapper
        );
        List<OrganizationEntity> organizationEntityList = new ArrayList<OrganizationEntity>();
        page.getRecords().stream().forEach((OrganizationEntity companyEntity) -> {
            //查询部门
            List<OrganizationEntity> departmentList = this.getByParentOrgId(companyEntity.getUuid());
            departmentList.stream().forEach((OrganizationEntity departmentItem)->{
                //查询科室
                List<OrganizationEntity> officeList = this.getByParentOrgId(departmentItem.getUuid());
                departmentItem.setChildren(officeList);
            });
            companyEntity.setChildren(departmentList);
            organizationEntityList.add(companyEntity);
        });
        page.setRecords(organizationEntityList);

        return new PageUtils(page);
    }

    @Override
    public List<OrganizationEntity> getByParentOrgId(Long parentOrgId) {
        QueryWrapper<OrganizationEntity> queryWrapper = new QueryWrapper<OrganizationEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("parent_org",parentOrgId);
        return this.list(queryWrapper);
    }

    @Override
    public List<OrganizationEntity> getByOrgNameAndParentOrgId(String orgName, Long parentOrgId) {
        QueryWrapper<OrganizationEntity> queryWrapper = new QueryWrapper<OrganizationEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("parent_org",parentOrgId);
        queryWrapper.eq("org_name",orgName);
        return this.list(queryWrapper);
    }

    @Override
    public List<OrganizationEntity> initCompanyOptions() {
        QueryWrapper<OrganizationEntity> queryWrapper = new QueryWrapper<OrganizationEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("org_type","company");
        return this.list(queryWrapper);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<OrganizationEntity> updateWrapper = new UpdateWrapper<OrganizationEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public void updateByOrgName(String oldOrgName,String newOrgName) {
        UpdateWrapper<OrganizationEntity> updateWrapper = new UpdateWrapper<OrganizationEntity>();
        updateWrapper.set("org_name", newOrgName);
        updateWrapper.eq("org_name",oldOrgName);
        this.baseMapper.update(null,updateWrapper);
    }
}
