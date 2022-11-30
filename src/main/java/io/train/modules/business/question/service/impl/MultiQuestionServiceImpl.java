package io.train.modules.business.question.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.modules.business.course.service.CourseService;
import io.train.modules.business.coursetype.entity.CourseTypeEntity;
import io.train.modules.business.coursetype.service.CourseTypeService;
import io.train.modules.business.paper.service.ExamPaperInfosService;
import io.train.modules.business.paperquestion.service.PaperQuestionService;
import io.train.modules.business.question.dao.MultiQuestionDao;
import io.train.modules.business.question.entity.MultiQuestionEntity;
import io.train.modules.business.question.service.MultiQuestionService;
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

@Service("multiQuestionService")
public class MultiQuestionServiceImpl extends ServiceImpl<MultiQuestionDao, MultiQuestionEntity> implements MultiQuestionService {
    @Autowired
    CourseService courseService;

    @Autowired
    ExamPaperInfosService examPaperInfosService;

    @Autowired
    private CourseTypeService courseTypeService;
    
    @Autowired
    private PaperQuestionService paperQuestionService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<MultiQuestionEntity> queryWrapper = new QueryWrapper<MultiQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        if(null != params.get("question") && !"".equals(params.get("question").toString())){
            queryWrapper.like("question",params.get("question"));
        }
        if(null != params.get("questionType") && !"".equals(params.get("questionType").toString())){
            queryWrapper.like("question_type",params.get("questionType"));
        }
        queryWrapper.orderByDesc("create_date");
        IPage<MultiQuestionEntity> page = this.page(
                new Query<MultiQuestionEntity>().getPage(params),
                queryWrapper
        );
        List<MultiQuestionEntity> multiQuestionEntityList = new ArrayList<MultiQuestionEntity>();
        page.getRecords().stream().forEach((MultiQuestionEntity multiQuestionEntity) -> {
            //将课程id翻译成课程名称
            CourseTypeEntity courseTypeEntity = courseTypeService.getById(multiQuestionEntity.getSubject());
            multiQuestionEntity.setSubject(courseTypeEntity.getCourseTypeName());
            multiQuestionEntityList.add(multiQuestionEntity);
        });
        page.setRecords(multiQuestionEntityList);
        return new PageUtils(page);
    }

    @Override
    public List<MultiQuestionEntity> getListByQuestionId(String questionId) {
        // List<PaperQuestionEntity> paperQuestionEntityList = paperQuestionService.getByPaperId(questionId);
        // List<String > questionIds = paperQuestionEntityList.stream().map(PaperQuestionEntity::getQuestionId).collect(Collectors.toList());
        QueryWrapper<MultiQuestionEntity> queryWrapper = new QueryWrapper<MultiQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        //queryWrapper.in("uuid",questionIds);
        queryWrapper.eq("questionId",questionId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<MultiQuestionEntity> updateWrapper = new UpdateWrapper<MultiQuestionEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public List<MultiQuestionEntity> getAllQuestionsBySubject(String subject) {
        QueryWrapper<MultiQuestionEntity> queryWrapper = new QueryWrapper<MultiQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("subject",subject);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<MultiQuestionEntity> getAllQuestionsBySubjectAndType(String subject, String questionType) {
        QueryWrapper<MultiQuestionEntity> queryWrapper = new QueryWrapper<MultiQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("subject",subject);
        queryWrapper.eq("question_type",questionType);
        return this.baseMapper.selectList(queryWrapper);
    }
}
