package io.train.modules.business.company.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.modules.business.company.dao.CompanyDao;
import io.train.modules.business.company.entity.CompanyEntity;
import io.train.modules.business.company.service.CompanyService;

import io.train.modules.business.organization.service.OrganizationService;
import io.train.modules.sys.entity.SysUserEntity;
import io.train.modules.sys.service.SysDictService;
import io.train.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import org.springframework.transaction.annotation.Transactional;


@Service("companyService")
public class CompanyServiceImpl extends ServiceImpl<CompanyDao, CompanyEntity> implements CompanyService {
    @Autowired
    SysDictService sysDictService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    SysUserService sysUserService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<CompanyEntity> queryWrapper = new QueryWrapper<CompanyEntity>();
        queryWrapper.eq("delete_flag",0);
        if(null != params.get("companyName") && !"".equals(params.get("companyName").toString())){
            queryWrapper.like("company_name",params.get("companyName"));
        }
        queryWrapper.orderByDesc("create_date");
        IPage<CompanyEntity> page = this.page(
                new Query<CompanyEntity>().getPage(params),
                queryWrapper
        );
        if(page.getRecords().size() > 0){
            page.getRecords().stream().forEach((CompanyEntity companyEntity)->{
                Map<String, String> map = sysDictService.findMapByDictCode(companyEntity.getCompanyNature());
                companyEntity.setCompanyNature(map.get(companyEntity.getCompanyNature()));
            });
        }
        return new PageUtils(page);
    }

    @Override
    public CompanyEntity getById(String uuid) {
        QueryWrapper<CompanyEntity> queryWrapper = new QueryWrapper<CompanyEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("uuid",uuid);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<CompanyEntity> updateWrapper = new UpdateWrapper<CompanyEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public List<CompanyEntity> initCompanyOptions() {
        QueryWrapper<CompanyEntity> queryWrapper = new QueryWrapper<CompanyEntity>();
        queryWrapper.eq("delete_flag",0);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public CompanyEntity getByCompanyName(String companyName) {
        QueryWrapper<CompanyEntity> queryWrapper = new QueryWrapper<CompanyEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("company_name",companyName);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void updateCompany(CompanyEntity companyEntity) {
        CompanyEntity oldCompany = this.getById(companyEntity.getUuid());
        this.updateById(companyEntity);
        if(!companyEntity.getCompanyName().equals(oldCompany.getCompanyName())){
            organizationService.updateByOrgName(oldCompany.getCompanyName(),companyEntity.getCompanyName());
        }
    }

    @Override
    public CompanyEntity getByUserId(String userId) {
        SysUserEntity userEntity = sysUserService.getById(userId);
        if(null != userEntity){
            return this.baseMapper.selectById(userEntity.getTenantId());
        }
        return null;
    }
}
