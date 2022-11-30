package io.train.modules.business.leavemessage.service.impl;

import io.train.modules.business.course.entity.CourseEntity;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.leavemessage.dao.LeaveMessageDao;
import io.train.modules.business.leavemessage.entity.LeaveMessageEntity;
import io.train.modules.business.leavemessage.service.LeaveMessageService;

import io.train.modules.business.student.service.StudentService;
import io.train.modules.oss.entity.SysOssEntity;
import io.train.modules.oss.service.AwsService;
import io.train.modules.oss.service.SysOssService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;


@Service("leaveMessageService")
public class LeaveMessageServiceImpl extends ServiceImpl<LeaveMessageDao, LeaveMessageEntity> implements LeaveMessageService {
    
    @Autowired
    CourseService courseService;
    
    @Autowired
    StudentService studentService;
    
    @Autowired
    CourseTypeService courseTypeService;

    @Autowired
    public SysOssService sysOssService;

    @Autowired
    public AwsService awsService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	Map<String, Object> userParams = new HashMap<String,Object>();
    	userParams.putAll(params);
        IPage<LeaveMessageEntity> page = this.page(
                new Query<LeaveMessageEntity>().getPage(params),
                new QueryWrapper<LeaveMessageEntity>().isNull("parent_uuid").eq(params.containsKey("courseTypeId"),"course_type_id", params.get("courseTypeId")).eq(params.containsKey("courseId"),"course_id", ""+params.get("courseId"))
                .eq(params.containsKey("isPrivate"), "is_private", ""+params.get("isPrivate")).orderByDesc("create_time")
        );
        List<LeaveMessageEntity> list = page.getRecords();
        if(userParams.containsKey("isPrivate") && (""+userParams.get("isPrivate")).equals("0") && userParams.containsKey("studentId")){
            IPage<LeaveMessageEntity> userPage = this.page(
                    new Query<LeaveMessageEntity>().getPage(userParams),
                    new QueryWrapper<LeaveMessageEntity>().isNull("parent_uuid").eq("is_private", "1").eq(params.containsKey("courseId"),"course_id", ""+params.get("courseId"))
                            .orderByDesc("create_time")
            );
            list.addAll(0, userPage.getRecords());
        }
        for(int i=0;i<list.size();i++){
            LeaveMessageEntity tmp = list.get(i);
            //翻译课程包名称
            CourseTypeEntity courseTypeEntity = courseTypeService.getById(tmp.getCourseTypeId());
            tmp.setCourseTypeName(courseTypeEntity.getCourseTypeName());
            //获取封面图片url
            SysOssEntity sysOssEntity = sysOssService.getOne(new QueryWrapper<SysOssEntity>().eq("obj_id",courseTypeEntity.getUuid()));
            if(null != sysOssEntity){
                tmp.setCourseCover(awsService.downFile(sysOssEntity.getOssFileName()));
            }
            //翻译课程名称和课程类型
            CourseEntity courseEntity = courseService.getById(tmp.getCourseId());
            if(null != courseEntity){
                tmp.setCourseName(courseEntity.getCourseName());
                tmp.setCourseType(getCourseType(courseEntity.getFileType()));
                tmp.setCourseStatus(courseEntity.getStatusLabel());
            }

            tmp.setChildrenList(getlist(tmp.getUuid()));
        }
     
        return new PageUtils(page);
    }

    public List<LeaveMessageEntity> getlist(String parentid){
    	return this.list(new QueryWrapper<LeaveMessageEntity>().like(StringUtils.isNotBlank(parentid),"parent_uuid", parentid));
    }

    @Override
    public List<LeaveMessageEntity> listGroupByCourseTypeId(Map<String, Object> params) {
        QueryWrapper<LeaveMessageEntity> queryWrapper = new QueryWrapper<LeaveMessageEntity>();
        queryWrapper.select("course_type_id,count(1) as num");
        queryWrapper.groupBy("course_type_id");
        queryWrapper.isNull("parent_uuid");
        List<LeaveMessageEntity> leaveMessageEntityList = this.baseMapper.selectList(queryWrapper);
        for (LeaveMessageEntity leaveMessageEntity : leaveMessageEntityList) {
            CourseTypeEntity courseTypeEntity = courseTypeService.getById(leaveMessageEntity.getCourseTypeId());
            leaveMessageEntity.setCourseTypeName(courseTypeEntity.getCourseTypeName());
            leaveMessageEntity.setCourseTypeIntroduction(courseTypeEntity.getCourseTypeIntroduction());
            leaveMessageEntity.setCourseTypeIsRequired(courseTypeEntity.getType());
        }
        return leaveMessageEntityList;
    }

    @Override
    public void removeByIds(List<String> uuids) {
        //删除评论
        QueryWrapper<LeaveMessageEntity> queryWrapper = new QueryWrapper<LeaveMessageEntity>();
        queryWrapper.in("uuid",uuids);
        this.baseMapper.delete(queryWrapper);
        //删除回复
        QueryWrapper<LeaveMessageEntity> queryWrapperReply = new QueryWrapper<LeaveMessageEntity>();
        queryWrapperReply.in("parent_uuid",uuids);
        this.baseMapper.delete(queryWrapperReply);
    }

    @Override
    public void reply(LeaveMessageEntity leaveMessageEntity) {
        this.baseMapper.insert(leaveMessageEntity);
    }
    
    private String getCourseType(String type){
        if("video".equals(type)){
            return "视频";
        }
        if("translate".equals(type)){
            return "中日翻译";
        }
        return type;
    }
}