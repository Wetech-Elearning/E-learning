package io.train.modules.business.certificateinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.modules.business.certificateinfo.dao.CertificateInfoDao;
import io.train.modules.business.certificateinfo.entity.CertificateInfoEntity;
import io.train.modules.business.certificateinfo.service.CertificateInfoService;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.oss.entity.SysOssEntity;
import io.train.modules.oss.service.AwsService;
import io.train.modules.oss.service.SysOssService;
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


@Service("certificateInfoService")
public class CertificateInfoServiceImpl extends ServiceImpl<CertificateInfoDao, CertificateInfoEntity> implements CertificateInfoService {

	@Autowired
	public SysOssService sysOssService;

	@Autowired
	public CourseService courseService;

	@Autowired
	AwsService awsService;
	
	@Autowired
    CourseTypeService courseTypeService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
		QueryWrapper<CertificateInfoEntity> queryWrapper = new QueryWrapper<CertificateInfoEntity>();
		queryWrapper.eq("delete_flag",0);
		if(null != params.get("creater") && !"".equals(params.get("creater").toString())){
			queryWrapper.like("creater",params.get("creater"));
		}
		if(null != params.get("certificateName") && !"".equals(params.get("certificateName").toString())){
			queryWrapper.like("certificate_name",params.get("certificateName"));
		}
		queryWrapper.orderByDesc("create_date");
        IPage<CertificateInfoEntity> page = this.page(
                new Query<CertificateInfoEntity>().getPage(params),
				queryWrapper
        );
		if(page.getRecords().size() > 0){
			page.getRecords().stream().forEach((CertificateInfoEntity certificateInfoEntity)->{
				//翻译课程封面
				SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",certificateInfoEntity.getUuid()));
				if(null != sysOssEntity){
					certificateInfoEntity.setCertificateCover(awsService.downFile(sysOssEntity.getOssFileName()));
				}
			});
		}
        return new PageUtils(page);
    }

	@Override
	public List<CertificateInfoEntity> listAll(Map<String, Object> params) {
		QueryWrapper<CertificateInfoEntity> queryWrapper = new QueryWrapper<CertificateInfoEntity>();
		queryWrapper.eq("delete_flag",0);
		List<CertificateInfoEntity> certificateInfoEntityList = this.list(queryWrapper);
		if(null != certificateInfoEntityList && certificateInfoEntityList.size() > 0){
			for (CertificateInfoEntity certificateInfoEntity : certificateInfoEntityList) {
				//翻译课程封面
				SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",certificateInfoEntity.getUuid()));
				if(null != sysOssEntity){
					certificateInfoEntity.setCertificateCover(awsService.downFile(sysOssEntity.getOssFileName()));
				}
			}
		}
		return certificateInfoEntityList;
	}

	@Override
	public List<CertificateInfoEntity> getUnRelatedCertificateInfoEntityList(Map<String, Object> params) {
		List<CertificateInfoEntity> list = new ArrayList<CertificateInfoEntity>();
		QueryWrapper<CertificateInfoEntity> queryWrapper = new QueryWrapper<CertificateInfoEntity>();
		queryWrapper.eq("delete_flag",0);
		List<CertificateInfoEntity> certificateInfoEntityList = this.list(queryWrapper);
		if(null != certificateInfoEntityList && certificateInfoEntityList.size() > 0){
			for (CertificateInfoEntity certificateInfoEntity : certificateInfoEntityList) {
				String[] certificateInfoIds = {String.valueOf(certificateInfoEntity.getUuid())};
				List<CourseTypeEntity> courseTypeEntityList = courseTypeService.getTwoLevelByCertificateId(certificateInfoIds);
//				if(null != courseTypeEntityList && courseTypeEntityList.size() > 0){
//					continue;
//				}
				//翻译课程封面
				SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",certificateInfoEntity.getUuid()));
				if(null != sysOssEntity){
					certificateInfoEntity.setCertificateCover(awsService.downFile(sysOssEntity.getOssFileName()));
				}
				list.add(certificateInfoEntity);
			}
		}
		return list;
	}

	@Override
	public List<CertificateInfoEntity> listByUser(String userId) {
		List<CertificateInfoEntity> certificateInfoEntityList = baseMapper.getCertificateInfoEntityByUser(userId);
		if(null != certificateInfoEntityList && certificateInfoEntityList.size() > 0){
			for (CertificateInfoEntity certificateInfoEntity : certificateInfoEntityList) {
				//翻译课程封面
				SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",certificateInfoEntity.getUuid()));
				if(null != sysOssEntity){
					certificateInfoEntity.setCertificateCover(awsService.downFile(sysOssEntity.getOssFileName()));
				}
			}
		}
		return certificateInfoEntityList;
	}

	@Override
	public CertificateInfoEntity getByRelatedCourse(String courseId) {
    	QueryWrapper<CertificateInfoEntity> queryWrapper = new QueryWrapper<CertificateInfoEntity>();
		queryWrapper.eq("delete_flag",0);
		queryWrapper.eq("related_course",courseId);
		CertificateInfoEntity certificateInfoEntity = baseMapper.selectOne(queryWrapper);
		if(null != certificateInfoEntity){
			//翻译课程封面
			SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",certificateInfoEntity.getUuid()));
			if(null != sysOssEntity){
				certificateInfoEntity.setCertificateCover(awsService.downFile(sysOssEntity.getOssFileName()));
			}
		}
		return certificateInfoEntity;
	}

	@Override
	public void removeByIds(List<String> uuids) {
		UpdateWrapper<CertificateInfoEntity> updateWrapper = new UpdateWrapper<CertificateInfoEntity>();
		updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
		updateWrapper.in("uuid",uuids);
		this.baseMapper.update(null,updateWrapper);
	}
}
