package io.train.modules.business.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.paper.service.ExamPaperInfosService;
import io.train.modules.business.question.dao.JudgeQuestionDao;
import io.train.modules.business.question.entity.JudgeQuestionEntity;
import io.train.modules.business.question.service.JudgeQuestionService;
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

@Service("judgeQuestionService")
public class JudgeQuestionServiceImpl extends ServiceImpl<JudgeQuestionDao, JudgeQuestionEntity> implements JudgeQuestionService {
    @Autowired
    CourseService courseService;

    @Autowired
    CourseTypeService courseTypeService;

    @Autowired
    ExamPaperInfosService examPaperInfosService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<JudgeQuestionEntity> queryWrapper = new QueryWrapper<JudgeQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        if(null != params.get("question") && !"".equals(params.get("question").toString())){
            queryWrapper.like("question",params.get("question"));
        }
        queryWrapper.orderByDesc("create_date");
        IPage<JudgeQuestionEntity> page = this.page(
                new Query<JudgeQuestionEntity>().getPage(params),
                queryWrapper
        );
        List<JudgeQuestionEntity> judgeQuestionEntities = new ArrayList<JudgeQuestionEntity>();
        page.getRecords().stream().forEach((JudgeQuestionEntity judgeQuestionEntity) -> {
            //将课程id翻译成课程名称
            CourseTypeEntity courseTypeEntity = courseTypeService.getById(judgeQuestionEntity.getSubject());
            judgeQuestionEntity.setSubject(courseTypeEntity.getCourseTypeName());
            judgeQuestionEntities.add(judgeQuestionEntity);
        });
        page.setRecords(judgeQuestionEntities);
        return new PageUtils(page);
    }

    @Override
    public List<JudgeQuestionEntity> getListByQuestionId(String questionId) {
        QueryWrapper<JudgeQuestionEntity> queryWrapper = new QueryWrapper<JudgeQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("questionId",questionId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<JudgeQuestionEntity> updateWrapper = new UpdateWrapper<JudgeQuestionEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public List<JudgeQuestionEntity> getAllQuestionsBySubject(String subject) {
        QueryWrapper<JudgeQuestionEntity> queryWrapper = new QueryWrapper<JudgeQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("subject",subject);
        return this.baseMapper.selectList(queryWrapper);
    }
}
