package io.train.modules.business.certificateinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.certificateinfo.entity.CertificateInfoEntity;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:51:01
 */
public interface CertificateInfoService extends IService<CertificateInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

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
    List<CertificateInfoEntity> listAll(Map<String, Object> params);
    
    /**
     * 查询未关联的二级课程的证书 
     * @author: liyajie 
     * @date: 2022/6/30 11:32
     * @param params
     * @return java.util.List<io.train.modules.business.common.certificateinfo.entity.CertificateInfoEntity>
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<CertificateInfoEntity> getUnRelatedCertificateInfoEntityList(Map<String, Object> params);

    /**
     * 根据用户id查询证书列表
     * @author: liujiaqiang
     * @date: 2021/8/30 0:14
     * @param userId
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    List<CertificateInfoEntity> listByUser(String userId);

    /**
     * 根据课程id查询证书列表
     * @author: liujiaqiang
     * @date: 2021/8/30 0:14
     * @param courseId
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    CertificateInfoEntity getByRelatedCourse(String courseId);

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
}

