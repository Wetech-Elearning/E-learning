package io.train.modules.business.paper.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.paper.dao.ExamPaperInfosDao;
import io.train.modules.business.paper.entity.ExamPaperInfosEntity;
import io.train.modules.business.paper.service.ExamPaperInfosService;
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


@Service("examPaperInfosService")
public class ExamPaperInfosServiceImpl extends ServiceImpl<ExamPaperInfosDao, ExamPaperInfosEntity> implements ExamPaperInfosService {

	@Autowired
    CourseTypeService courseTypeService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
		QueryWrapper<ExamPaperInfosEntity> queryWrapper = new QueryWrapper<ExamPaperInfosEntity>();
		queryWrapper.eq("delete_flag",0);
		if(null != params.get("creater") && !"".equals(params.get("creater").toString())){
			queryWrapper.like("creater",params.get("creater"));
		}
		if(null != params.get("examPaperName") && !"".equals(params.get("examPaperName").toString())){
			queryWrapper.like("exam_paper_name",params.get("examPaperName"));
		}
        queryWrapper.orderByDesc("create_date");
        IPage<ExamPaperInfosEntity> page = this.page(
                new Query<ExamPaperInfosEntity>().getPage(params),
				queryWrapper
        );
		page.getRecords().stream().forEach((ExamPaperInfosEntity examPaperInfosEntity) -> {
			CourseTypeEntity courseTypeEntity = courseTypeService.getById(examPaperInfosEntity.getSubject());
			if(null != courseTypeEntity){
				examPaperInfosEntity.setSubject(courseTypeEntity.getCourseTypeName());
				examPaperInfosEntity.setSubjectId(courseTypeEntity.getUuid());
			}
		});
        return new PageUtils(page);
    }

	@Override
	public List<ExamPaperInfosEntity> queryAll(Map<String, Object> params) {
		return this.list(new QueryWrapper<ExamPaperInfosEntity>());
	}

	@Override
	public List<ExamPaperInfosEntity> listByUser(String userId,String examPaperName,int start,int limits) {
		return baseMapper.listByUser(userId,examPaperName,""+new Date().getTime(),start,limits);
	}
	@Override
	public int listByUserCount(String userId,String examPaperName) {
		return baseMapper.listByUserCount(userId,examPaperName);
	}

	@Override
	public List<ExamPaperInfosEntity> listByClassId(String classId) {
		return baseMapper.listByClassId(classId,""+new Date().getTime());
	}

	@Override
	public void removeByIds(List<String> uuids) {
		UpdateWrapper<ExamPaperInfosEntity> updateWrapper = new UpdateWrapper<ExamPaperInfosEntity>();
		updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
		updateWrapper.in("uuid",uuids);
		this.baseMapper.update(null,updateWrapper);
	}

	@Override
	public void updateStatus(String uuid,String status) {
		UpdateWrapper<ExamPaperInfosEntity> updateWrapper = new UpdateWrapper<ExamPaperInfosEntity>();
		updateWrapper.set("status",status);
		updateWrapper.eq("uuid",uuid);
		this.baseMapper.update(null,updateWrapper);
	}

	@Override
	public List<ExamPaperInfosEntity> getExamPaperInfosEntityBySubject(String subject) {
		QueryWrapper<ExamPaperInfosEntity> queryWrapper = new QueryWrapper<ExamPaperInfosEntity>();
		queryWrapper.eq("delete_flag",0);
		queryWrapper.eq("subject",subject);
		return this.baseMapper.selectList(queryWrapper);
	}

	@Override
	public List<ExamPaperInfosEntity> initPaperOptions() {
		QueryWrapper queryWrapper = new QueryWrapper<ExamPaperInfosEntity>();
		queryWrapper.eq("delete_flag",0);
		queryWrapper.eq("status",1);
		return this.baseMapper.selectList(queryWrapper);
	}
}