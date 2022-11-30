package io.train.modules.business.certificateinfo.service.impl;

import io.train.modules.business.certificateinfo.dao.CertificateUserInfoDao;
import io.train.modules.business.certificateinfo.entity.CertificateUserInfoEntity;
import io.train.modules.business.certificateinfo.service.CertificateUserInfoService;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;


@Service("certificateUserInfoServiceImpl")
public class CertificateUserInfoServiceImpl extends ServiceImpl<CertificateUserInfoDao, CertificateUserInfoEntity> implements CertificateUserInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CertificateUserInfoEntity> page = this.page(
                new Query<CertificateUserInfoEntity>().getPage(params),
                new QueryWrapper<CertificateUserInfoEntity>()
        );

        return new PageUtils(page);
    }

	@Override
	public List<CertificateUserInfoEntity> listAll(Map<String, Object> params) {
		return this.list(new QueryWrapper<CertificateUserInfoEntity>());
	}

	@Override
	public List<CertificateUserInfoEntity> listByUser(String userId) {
		return null;
	}

	@Override
	public CertificateUserInfoEntity getByUserIdAndCertificateId(String userId, String certificateId) {
		QueryWrapper<CertificateUserInfoEntity> queryWrapper = new QueryWrapper<CertificateUserInfoEntity>();
		queryWrapper.eq("user_id",userId);
		queryWrapper.eq("certificate_uuid",certificateId);
		queryWrapper.eq("delete_flag","0");
		return this.baseMapper.selectOne(queryWrapper);
	}
}