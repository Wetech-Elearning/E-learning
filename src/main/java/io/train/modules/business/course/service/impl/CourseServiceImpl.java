package io.train.modules.business.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.modules.business.course.dao.CourseDao;
import io.train.modules.business.course.entity.CourseEntity;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.business.translate.entity.TranslateEntity;
import io.train.modules.business.translate.service.TranslateService;
import io.train.modules.oss.entity.SysOssEntity;
import io.train.modules.oss.service.AwsService;
import io.train.modules.oss.service.SysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import io.train.common.utils.DateUtils;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;


@Service("courseService")
public class CourseServiceImpl extends ServiceImpl<CourseDao, CourseEntity> implements CourseService {
	@Autowired
	public SysOssService sysOssService;

	@Autowired
	public AwsService awsService;
	
	@Autowired
    TranslateService translateService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
		QueryWrapper<CourseEntity> queryWrapper = new QueryWrapper<CourseEntity>();
		queryWrapper.eq("delete_flag",0);

		if(params.containsKey("params") && "coursetype".equals(params.get("fromPage").toString())){
			if(null != params.get("creater") && !"".equals(params.get("creater").toString())){
				queryWrapper.like("creater",params.get("creater"));
			}
		}
		if(null != params.get("courseName") && !"".equals(params.get("courseName").toString())){
			queryWrapper.like("course_name",params.get("courseName"));
		}
		if(null != params.get("relatedCourseTypeId") && !"".equals(params.get("relatedCourseTypeId").toString())){
			queryWrapper.eq("related_course_type_id",params.get("relatedCourseTypeId"));
		}
		queryWrapper.orderByDesc("create_date");
        IPage<CourseEntity> page = this.page(
                new Query<CourseEntity>().getPage(params),
				queryWrapper
        );
        if(page.getRecords().size() > 0){
			page.getRecords().stream().forEach((CourseEntity courseEntity)->{
				SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",courseEntity.getUuid()));
				if(null != sysOssEntity){
					courseEntity.setPercent(sysOssEntity.getPercent());
					courseEntity.setFileUrl(awsService.downFile(sysOssEntity.getOssFileName()));
					//更新课程表课件的url
					//UpdateWrapper<CourseEntity> updateWrapper1 = new UpdateWrapper<CourseEntity>();
					//updateWrapper1.eq("uuid",courseEntity.getUuid()).set("file_url", sysOssEntity.getUrl());
					//this.update(null,updateWrapper1);
				}
				if("translate".equals(courseEntity.getFileType())){
					List<TranslateEntity> translateEntityList = translateService.getTranslateEntityByRelatedCourseId(String.valueOf(courseEntity.getUuid()));
					if(null != translateEntityList && translateEntityList.size() > 0){
						courseEntity.setPercent(100);
					}
				}
			});
		}
        return new PageUtils(page);
    }

	@Override
	public List<CourseEntity> getCourseEntityByClassId(String classId) {
		return baseMapper.getCourseEntityByClassId(classId,""+new Date().getTime());
	}

	@Override
	public PageUtils queryPageByStudent(Map<String, Object> params) {
		QueryWrapper<CourseEntity> queryWrapper = new QueryWrapper<CourseEntity>();
		queryWrapper.eq("delete_flag",0);
		IPage<CourseEntity> page = this.page(
                new Query<CourseEntity>().getPage(params),
				queryWrapper
        );
        return new PageUtils(page);
	}

	@Override
	public List<CourseEntity> getCourseEntityByCourseType(String courseType) {
		QueryWrapper<CourseEntity> queryWrapper = new QueryWrapper<CourseEntity>();
		queryWrapper.eq("delete_flag",0);
		queryWrapper.eq("related_course_type_id", courseType);
		return this.list(queryWrapper);
	}

	@Override
	public List<CourseEntity> getCourseEntityByCourseTypeAndStatus(String courseType, String Status) {
		QueryWrapper<CourseEntity> queryWrapper = new QueryWrapper<CourseEntity>();
		queryWrapper.eq("delete_flag",0);
		queryWrapper.eq("related_course_type_id", courseType);
		queryWrapper.eq("status", Status);
		return this.list(queryWrapper);
	}

	@Override
	public List<CourseEntity> getCourseEntityByCourseTypeIdAndCourseType(String relatedCourseTypeId, String courseType) {
		QueryWrapper<CourseEntity> queryWrapper = new QueryWrapper<CourseEntity>();
		queryWrapper.eq("delete_flag",0);
		queryWrapper.eq("related_course_type_id", relatedCourseTypeId);
		queryWrapper.eq("file_type", courseType);
		return this.list(queryWrapper);
	}

	@Override
	public List<CourseEntity> listAll(Map<String, Object> params) {
		QueryWrapper<CourseEntity> queryWrapper = new QueryWrapper<CourseEntity>();
		queryWrapper.eq("delete_flag",0);
		return this.list(queryWrapper);
	}

	@Override
	public List<CourseEntity> getCourseEntityByCourseType(String courseType, String studentId) {
		return this.baseMapper.getPageByStudentCouseType(studentId,courseType,""+new Date().getTime());
	}
	 @Override
	    public void removeByIds(List<String> uuids) {
	        UpdateWrapper<CourseEntity> updateWrapper = new UpdateWrapper<CourseEntity>();
	        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
	        updateWrapper.in("uuid",uuids);
	        this.baseMapper.update(null,updateWrapper);
	    }

	@Override
	public CourseEntity getPageByStudentCourseId(String courseId, String studentId) {
		return this.baseMapper.getPageByStudentCourseId(studentId,courseId,""+new Date().getTime());
	}

	@Override
	public void release(String uuid) {
		UpdateWrapper<CourseEntity> updateWrapper = new UpdateWrapper<CourseEntity>();
		updateWrapper.set("status", "876ff807b5c84374b101be71ba7efd3f");
		updateWrapper.set("status_label", "发布");
		updateWrapper.eq("uuid",uuid);
		this.baseMapper.update(null,updateWrapper);
	}

	@Override
	public List<CourseEntity> getByCertificateIds(String[] certificateIds) {
    	QueryWrapper<CourseEntity> queryWrapper = new QueryWrapper<CourseEntity>();
		queryWrapper.eq("delete_flag","0");
		queryWrapper.in("certificate_id",certificateIds);
		return this.baseMapper.selectList(queryWrapper);
	}
}
