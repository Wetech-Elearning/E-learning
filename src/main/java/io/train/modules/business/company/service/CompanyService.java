package io.train.modules.business.company.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.company.entity.CompanyEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:12:46
 */
public interface CompanyService extends IService<CompanyEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据公司id查询公司
     * @author: liyajie
     * @date: 2021/11/2 21:32
     * @param uuid
     * @return io.train.modules.business.common.company.entity.CompanyEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    CompanyEntity getById(String uuid);

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
     * 初始化所属企业枚举
     * @author: liyajie
     * @date: 2021/9/5 16:21
     * @param
     * @return java.util.List<io.train.modules.business.common.company.entity.CompanyEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<CompanyEntity> initCompanyOptions();

    /**
     * 根据企业名称查询企业信息
     * @author: liyajie
     * @date: 2021/11/1 0:22
     * @param companyName
     * @return io.train.modules.business.common.company.entity.CompanyEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    CompanyEntity getByCompanyName(String companyName);

    /**
     * 更新企业信息
     * @author: liyajie
     * @date: 2021/11/1 0:22
     * @param companyEntity
     * @return io.train.modules.business.common.company.entity.CompanyEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    void updateCompany(CompanyEntity companyEntity);

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
    CompanyEntity getByUserId(String userId);
}

