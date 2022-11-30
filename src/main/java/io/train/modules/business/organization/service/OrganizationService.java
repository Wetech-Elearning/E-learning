package io.train.modules.business.organization.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.organization.entity.OrganizationEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:42:16
 */
public interface OrganizationService extends IService<OrganizationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询指定父级下的字级组织
     * @author: liyajie
     * @date: 2021/8/7 12:52
     * @param parentOrgId
     * @return java.util.List<io.train.modules.business.organization.entity.OrganizationEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<OrganizationEntity> getByParentOrgId(Long parentOrgId);

    /**
     * 查询指定父级组织
     * @author: liyajie
     * @date: 2021/8/7 12:52
     * @param orgName
     * @param parentOrgId
     * @return java.util.List<io.train.modules.business.organization.entity.OrganizationEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<OrganizationEntity> getByOrgNameAndParentOrgId(String orgName, Long parentOrgId);

    /**
     * 查询企业枚举
     * @author: liyajie
     * @date: 2021/8/8 17:48
     * @param
     * @return java.util.List<io.train.modules.busines.organization.entity.OrganizationEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<OrganizationEntity> initCompanyOptions();

    /**
     * 删除
     * @author: liyajie
     * @date: 2021/8/30 0:14
     * @param uuids
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void removeByIds(List<String> uuids);

    /**
     * 根据组织名称更新
     * @author: liyajie
     * @date: 2021/8/30 0:14
     * @param oldOrgName-旧组织名称
     * @param newOrgName-新组织名称
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void updateByOrgName(String oldOrgName,String newOrgName);
}

