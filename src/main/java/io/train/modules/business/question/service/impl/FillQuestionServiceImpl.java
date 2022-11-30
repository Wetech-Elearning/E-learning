package io.train.modules.business.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.paper.service.ExamPaperInfosService;
import io.train.modules.business.question.dao.FillQuestionDao;
import io.train.modules.business.question.entity.FillQuestionEntity;
import io.train.modules.business.question.service.FillQuestionService;
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


@Service("fillQuestionService")
public class FillQuestionServiceImpl extends ServiceImpl<FillQuestionDao, FillQuestionEntity> implements FillQuestionService {
    @Autowired
    CourseService courseService;

    @Autowired
    CourseTypeService courseTypeService;

    @Autowired
    FillQuestionDao fillQuestionDao;

    @Autowired
    ExamPaperInfosService examPaperInfosService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<FillQuestionEntity> queryWrapper = new QueryWrapper<FillQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        if(null != params.get("question") && !"".equals(params.get("question").toString())){
            queryWrapper.like("question",params.get("question"));
        }
        queryWrapper.orderByDesc("create_date");
        IPage<FillQuestionEntity> page = this.page(
                new Query<FillQuestionEntity>().getPage(params),
                queryWrapper
        );
        List<FillQuestionEntity> fillQuestionEntities = new ArrayList<FillQuestionEntity>();
        page.getRecords().stream().forEach((FillQuestionEntity fillQuestionEntity) -> {
            //将课程id翻译成课程名称
            CourseTypeEntity courseTypeEntity = courseTypeService.getById(fillQuestionEntity.getSubject());
            fillQuestionEntity.setSubject(courseTypeEntity.getCourseTypeName());
            fillQuestionEntities.add(fillQuestionEntity);
        });
        page.setRecords(fillQuestionEntities);
        return new PageUtils(page);
    }

    @Override
    public List<FillQuestionEntity> getListByQuestionId(String questionId) {
        QueryWrapper<FillQuestionEntity> queryWrapper = new QueryWrapper<FillQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("questionId",questionId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<FillQuestionEntity> updateWrapper = new UpdateWrapper<FillQuestionEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public List<FillQuestionEntity> getAllQuestionsBySubject(String subject) {
        QueryWrapper<FillQuestionEntity> queryWrapper = new QueryWrapper<FillQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("subject",subject);
        return this.baseMapper.selectList(queryWrapper);
    }
}
