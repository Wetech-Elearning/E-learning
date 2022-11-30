package io.train.modules.business.coursetype.service.impl;

import io.train.modules.business.course.entity.CourseEntity;
import io.train.modules.business.course.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.modules.business.coursetype.dao.CourseTypeDao;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;

import io.train.modules.oss.entity.SysOssEntity;
import io.train.modules.oss.service.AwsService;
import io.train.modules.oss.service.SysOssService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;



@Service("courseTypeService")
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeDao, CourseTypeEntity> implements CourseTypeService {

	@Autowired
	SysOssService sysOssService;

	@Autowired
	CourseService courseService;

	@Autowired
	AwsService awsService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
		QueryWrapper<CourseTypeEntity> queryWrapper = new QueryWrapper<CourseTypeEntity>();
		if(params.containsKey("notinids")){
			String uuids = ""+params.get("notinids");
			String[] ids = uuids.split(",");
			queryWrapper.notIn("uuid",ids);
		}
		queryWrapper.eq("delete_flag",0);
		if(null != params.get("type") && !"".equals(params.get("type").toString())){
			if(!"all".equals(params.get("type"))){
				queryWrapper.eq("type",params.get("type"));
			}
		}
		if(null != params.get("courseTypeName") && !"".equals(params.get("courseTypeName").toString())){
			queryWrapper.like("course_type_name",params.get("courseTypeName"));
		}
		if(null != params.get("level") && !"".equals(params.get("level").toString())){
			if("one".equals(params.get("level").toString())){
				queryWrapper.eq("parent_course_type","0");
			}
			if("two".equals(params.get("level").toString())){
				queryWrapper.ne("parent_course_type","0");
			}
		}
		// 课程中心显示时排除未添加课程的二级课程包
		if(null != params.get("from") && !"".equals(params.get("from").toString())){
			List<CourseEntity> courseEntityList = courseService.listAll(new HashMap<>());
			queryWrapper.in("uuid",courseEntityList.stream().map(CourseEntity::getRelatedCourseTypeId).collect(Collectors.toList()));
		}
		queryWrapper.orderByDesc("create_date");
        IPage<CourseTypeEntity> page = this.page(
                new Query<CourseTypeEntity>().getPage(params),
				queryWrapper
        );
		if(page.getRecords().size() > 0){
			page.getRecords().stream().forEach((CourseTypeEntity courseTypeEntity)->{
				//获取封面图片url
				SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",courseTypeEntity.getUuid()));
				if(null != sysOssEntity){
					courseTypeEntity.setCourseCover(awsService.downFile(sysOssEntity.getOssFileName()));
				}
				//获取课程子包
				/*List<CourseTypeEntity> courseTypeEntityList = getByParentCouseType(String.valueOf(courseTypeEntity.getUuid()));
				if(null != courseTypeEntityList && courseTypeEntityList.size() > 0){
					courseTypeEntityList.stream().forEach((CourseTypeEntity childrenCourseTypeEntity)->{
						//获取封面图片url
						SysOssEntity childrenSysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",childrenCourseTypeEntity.getUuid()));
						if(null != childrenSysOssEntity){
							childrenCourseTypeEntity.setCourseCover(childrenSysOssEntity.getUrl());
						}
					});
				}
				courseTypeEntity.setChildren(courseTypeEntityList);*/
			});
		}
        return new PageUtils(page);
    }

	@Override
	public List<CourseTypeEntity> getCourseTypeList(Map<String,Object> params) {
    	Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("page",(int)params.get("page") - 1);
		paramMap.put("limit",params.get("limit"));
		if(params.containsKey("notinids")){
			String uuids = ""+params.get("notinids");
			String[] ids = uuids.split(",");
			if(ids.length > 0){
				paramMap.put("uuid", ids);
			}
		}
		if(null != params.get("type") && !"".equals(params.get("type").toString())){
			if(!"all".equals(params.get("type"))){
				paramMap.put("type", params.get("type"));
			}
		}
		if(null != params.get("courseTypeName") && !"".equals(params.get("courseTypeName").toString())){
			paramMap.put("courseTypeName", params.get("courseTypeName"));
		}
		if(null != params.get("level") && !"".equals(params.get("level").toString())){
			if("one".equals(params.get("level").toString())){
				paramMap.put("parentCourseType", "one");
			}
			if("two".equals(params.get("level").toString())){
				paramMap.put("parentCourseType", "two");
			}
		}
		List<CourseTypeEntity> courseTypeEntityList = this.baseMapper.getCourseTypeList(paramMap);
		if(null != courseTypeEntityList && courseTypeEntityList.size() > 0){
			for (CourseTypeEntity courseTypeEntity : courseTypeEntityList) {
				//获取封面图片url
				SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",courseTypeEntity.getUuid()));
				if(null != sysOssEntity){
					courseTypeEntity.setCourseCover(awsService.downFile(sysOssEntity.getOssFileName()));
				}
			}
		}
		return courseTypeEntityList;
	}

	@Override
	public List<CourseTypeEntity> getAllCouseType(Map<String, Object> params) {
		String courseTypeName=params.containsKey("courseTypeName") ? ""+params.get("courseTypeName"):"";
		return this.list(new QueryWrapper<CourseTypeEntity>().like(StringUtils.isNotBlank(courseTypeName),"course_type_name", courseTypeName).eq("parent_course_type","0"));
	}

	@Override
	public List<CourseTypeEntity> listOneLevelCourseType(Map<String, Object> params) {
		QueryWrapper<CourseTypeEntity> queryWrapper = new QueryWrapper<CourseTypeEntity>();
		queryWrapper.eq("delete_flag",0);
		queryWrapper.eq("parent_course_type",0);
		return this.baseMapper.selectList(queryWrapper);
	}

	@Override
	public List<CourseTypeEntity> listTwoLevelCourseType(Map<String, Object> params) {
		QueryWrapper<CourseTypeEntity> queryWrapper = new QueryWrapper<CourseTypeEntity>();
		queryWrapper.eq("delete_flag",0);
		queryWrapper.ne("parent_course_type",0);
		queryWrapper.eq("type","public");
		if(null != params.get("courseTypeId") && !"".equals(params.get("courseTypeId").toString())){
			queryWrapper.like("uuid",params.get("courseTypeId"));
		}
		return this.baseMapper.selectList(queryWrapper);
	}

	@Override
	public List<CourseTypeEntity> getAllCouseTypeByStudent(String studentId) {
		List<CourseTypeEntity> list  = baseMapper.getCouseTypeByStudent(studentId,""+new Date().getTime());
		return list;
	}

	public List<CourseTypeEntity> formatterChildrenTypeInfos(List<CourseTypeEntity> list,String studentId){
		for(int i=0;i<list.size();i++){
			CourseTypeEntity table = list.get(i);
			table.setListCourseEntity(courseService.getCourseEntityByCourseType(""+table.getUuid(),studentId));
			List<CourseTypeEntity> childrenlist = getChildrenList(""+list.get(i).getUuid());
			formatterChildrenTypeInfos(childrenlist,studentId);
			table.setChildrenCourseTypeEntity(childrenlist);
		}
		return list;
	}

	public List<CourseTypeEntity> getChildrenList(String parentTypeId){
		return this.list(new QueryWrapper<CourseTypeEntity>().eq(StringUtils.isNotBlank(""+parentTypeId),"parent_course_type", parentTypeId));
	}

	@Override
	public Map<String,Object> showHome() {
    	Map<String, Object> map = new HashMap<String, Object>();
		List<CourseEntity> courseEntityList = courseService.listAll(new HashMap<>());
    	// 热门课程
		QueryWrapper hotQueryWrapper =  new QueryWrapper<CourseTypeEntity>()
				.eq("delete_flag",0)
				.eq("type","hot")
				.ne("parent_course_type","0")
				.in("uuid",courseEntityList.stream().map(CourseEntity::getRelatedCourseTypeId).collect(Collectors.toList()))
				.last("limit 4");
		List<CourseTypeEntity> courseTypeEntityList = this.baseMapper.selectList(hotQueryWrapper);
		List<CourseTypeEntity> hotList = new ArrayList<CourseTypeEntity>();
		courseTypeEntityList.stream().forEach((CourseTypeEntity courseTypeEntity)->{
			SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",courseTypeEntity.getUuid()));
			if(null != sysOssEntity){
				courseTypeEntity.setCourseCover(awsService.downFile(sysOssEntity.getOssFileName()));
			}
			hotList.add(courseTypeEntity);
		});
		//公开课程
		QueryWrapper pubQueryWrapper = new QueryWrapper<CourseTypeEntity>()
				.eq("delete_flag",0)
				.eq("type","public")
				.ne("parent_course_type","0")
				.in("uuid",courseEntityList.stream().map(CourseEntity::getRelatedCourseTypeId).collect(Collectors.toList()))
				.last("limit 4");
		List<CourseTypeEntity> courseTypeEntityList2= this.baseMapper.selectList(pubQueryWrapper);
		List<CourseTypeEntity> publicList = new ArrayList<CourseTypeEntity>();
		courseTypeEntityList2.stream().forEach((CourseTypeEntity courseTypeEntity)->{
			SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",courseTypeEntity.getUuid()));
			if(null != sysOssEntity){
				courseTypeEntity.setCourseCover(awsService.downFile(sysOssEntity.getOssFileName()));
			}
			publicList.add(courseTypeEntity);
		});
		map.put("hot",hotList);
		map.put("public",publicList);
		return map;
	}

	@Override
	public List<CourseTypeEntity> getByParentCouseType(String parentCouseType) {
		QueryWrapper<CourseTypeEntity> queryWrapper = new QueryWrapper<CourseTypeEntity>();
		queryWrapper.eq("delete_flag","0");
		queryWrapper.eq("parent_course_type",parentCouseType);
		return this.baseMapper.selectList(queryWrapper);
	}

	@Override
	public CourseTypeEntity getAllCouseTypeByStudent(String couseTypeId, String studentId) {
		CourseTypeEntity obj =  this.getById(couseTypeId);
		List<CourseTypeEntity> list = getChildrenList(couseTypeId);
		obj.setListCourseEntity(courseService.getCourseEntityByCourseType(""+obj.getUuid(),studentId));
		formatterChildrenTypeInfos(list,studentId);
		obj.setChildrenCourseTypeEntity(list);
		return  obj;
	}

	@Override
	public void removeByIds(List<String> uuids) {
		UpdateWrapper<CourseTypeEntity> updateWrapper = new UpdateWrapper<CourseTypeEntity>();
		updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
		updateWrapper.in("uuid",uuids);
		this.baseMapper.update(null,updateWrapper);
	}

	@Override
	public List<CourseTypeEntity> getByParentCouseTypeByIds(String[] ids) {
		List<CourseTypeEntity> list = this.listByIds(Arrays.asList(ids));
		for (int i = 0; i < list.size(); i++) {
			SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",list.get(i).getUuid()));
			if(null != sysOssEntity){
				list.get(i).setCourseCover(awsService.downFile(sysOssEntity.getOssFileName()));
			}
		}
		return list;
	}

	@Override
	public List<CourseTypeEntity> getTwoLevelByCertificateId(String[] certificateIds) {
    	QueryWrapper<CourseTypeEntity> queryWrapper = new QueryWrapper<CourseTypeEntity>();
		queryWrapper.eq("delete_flag",0);
		queryWrapper.ne("parent_course_type",0);
		queryWrapper.in("certificate_id",certificateIds);
		return this.baseMapper.selectList(queryWrapper);
	}
}
