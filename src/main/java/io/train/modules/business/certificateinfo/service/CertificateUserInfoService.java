package io.train.modules.business.certificateinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.certificateinfo.entity.CertificateUserInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:51:01
 */
public interface CertificateUserInfoService extends IService<CertificateUserInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<CertificateUserInfoEntity> listAll(Map<String, Object> params);
    
    
    List<CertificateUserInfoEntity> listByUser(String userId);

    /**
     * 根据学员id和证书id查询证书信息
     * @author: liyajie 
     * @date: 2022/6/27 17:57
     * @param userId
     * @param certificateId
     * @return io.train.modules.business.common.certificateinfo.entity.CertificateUserInfoEntity
     * @exception:
     * @update:
     * @updatePerson:
     **/
    CertificateUserInfoEntity getByUserIdAndCertificateId(String userId, String certificateId);
}

