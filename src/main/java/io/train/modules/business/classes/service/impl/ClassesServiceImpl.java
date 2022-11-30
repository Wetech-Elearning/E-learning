package io.train.modules.business.classes.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.modules.business.classes.dao.ClassesDao;
import io.train.modules.business.classes.entity.ClassesCourseEntity;
import io.train.modules.business.classes.entity.ClassesEntity;
import io.train.modules.business.classes.service.ClassesCourseService;
import io.train.modules.business.classes.service.ClassesService;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.student.entity.StudentEntity;
import io.train.modules.business.student.service.StudentService;

import io.train.modules.sys.entity.SysUserEntity;
import io.train.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;


@Service("classesService")
public class ClassesServiceImpl extends ServiceImpl<ClassesDao, ClassesEntity> implements ClassesService {


	@Autowired
	private StudentService studentService;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private ClassesCourseService classesCourseService;

	@Autowired
	private CourseTypeService courseTypeService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
		QueryWrapper<ClassesEntity> queryWrapper = new QueryWrapper<ClassesEntity>();
		queryWrapper.eq("delete_flag",0);
		if(null != params.get("className") && !"".equals(params.get("className").toString())){
			queryWrapper.like("class_name",params.get("className"));
		}
        queryWrapper.orderByDesc("create_date");
        IPage<ClassesEntity> page = this.page(
                new Query<ClassesEntity>().getPage(params),
				queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
	public ClassesEntity getStudentList(ClassesEntity classEntity) {
    	ClassesEntity classEntitys = this.getById(classEntity.getUuid());
    	classEntitys.setStudentEntityList(studentService.getStudentListByClassId(classEntity.getUuid()));
		return classEntitys;
	}

	@Override
	public int addStudent2Class(String classUuid, String[] studentUuids) {
		Map<String,Object> datamap = new HashMap<String,Object>();
		datamap.put("class_id", classUuid);
		List<String> list = new ArrayList<String>();
		for(int i=0;i<studentUuids.length;i++){
			datamap.put("user_id", studentUuids[i]);
			List  tmplist = baseMapper.searchClassStudentStudent(datamap);
			if(tmplist.size() == 0){
				StudentEntity studentEntity = studentService.getById(studentUuids[i]);
				SysUserEntity sysUserEntity = sysUserService.queryByAccount(studentEntity.getAccount());
				list.add(String.valueOf(sysUserEntity.getUserId()));
			}
		}
		baseMapper.addStudent2Class(classUuid, list.toArray());
		return list.size();
	}

	@Override
	public void delStudentFromClass(String classUuid, String[] studentUuids) {
		baseMapper.delStudentFromClass(classUuid, studentUuids);
	}

	@Override
	public List<ClassesEntity> queryPageByStudentId(String studentId,String className) {
        return baseMapper.getListByStudentId(studentId,className,""+new Date().getTime());
	}

	@Override
	public void removeByIds(List<String> uuids) {
		UpdateWrapper<ClassesEntity> updateWrapper = new UpdateWrapper<ClassesEntity>();
		updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
		updateWrapper.in("uuid",uuids);
		this.baseMapper.update(null,updateWrapper);
	}

	@Override
	public List<CourseTypeEntity> listAllCorusesByStudentId(String studentId) {
    	List<CourseTypeEntity> courseTypeEntityList = new ArrayList<CourseTypeEntity>(); 
		List<ClassesEntity> classesEntityList = baseMapper.getListByStudentId(studentId,"",""+new Date().getTime());
		if(null != classesEntityList && classesEntityList.size() > 0){
			for (ClassesEntity classesEntity: classesEntityList) {
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("classId", classesEntity.getUuid());
				List<ClassesCourseEntity> classesCourseEntityList = classesCourseService.listAll(params);
				if(null != classesCourseEntityList && classesCourseEntityList.size() > 0){
					for (ClassesCourseEntity classesCourseEntity : classesCourseEntityList) {
						CourseTypeEntity courseTypeEntity = courseTypeService.getById(classesCourseEntity.getCourseType());
						if(null != courseTypeEntity){
							courseTypeEntityList.add(courseTypeEntity);
						}
					}
				}
			}
		}
		return courseTypeEntityList;
	}

	@Override
	public List<ClassesEntity> showFullCalendar(String studentId) {
		List<CourseTypeEntity> courseTypeEntityList = new ArrayList<CourseTypeEntity>();
		List<ClassesEntity> classesEntityList = baseMapper.getListByStudentId(studentId,"",""+new Date().getTime());
		if(null != classesEntityList && classesEntityList.size() > 0){
			for (ClassesEntity classesEntity: classesEntityList) {
				Map<String,Object> params = new HashMap<String,Object>();
				params.put("classId", classesEntity.getUuid());
				List<ClassesCourseEntity> classesCourseEntityList = classesCourseService.listAll(params);
				if(null != classesCourseEntityList && classesCourseEntityList.size() > 0){
					for (ClassesCourseEntity classesCourseEntity : classesCourseEntityList) {
						CourseTypeEntity courseTypeEntity = courseTypeService.getById(classesCourseEntity.getCourseType());
						if(null != courseTypeEntity){
							courseTypeEntityList.add(courseTypeEntity);
						}
					}
				}
				classesEntity.setCourseEntityList(courseTypeEntityList);
			}
		}
		return classesEntityList;
	}
}
