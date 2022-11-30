package io.train.modules.business.questionnaire.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.business.questionnaire.dao.QuestionnaireQuestionDao;
import io.train.modules.business.questionnaire.entity.QuestionnaireQuestionEntity;
import io.train.modules.business.questionnaire.service.QuestionnaireQuestionService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("questionnaireQuestionService")
public class QuestionnaireQuestionServiceImpl extends ServiceImpl<QuestionnaireQuestionDao, QuestionnaireQuestionEntity> implements QuestionnaireQuestionService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<QuestionnaireQuestionEntity> queryWrapper = new QueryWrapper<QuestionnaireQuestionEntity>();
        queryWrapper.orderByDesc("create_date");
        if(null != params.get("question") && !"".equals(params.get("question"))){
            queryWrapper.like("question", params.get("question"));
        }
        if(null != params.get("questionType") && !"".equals(params.get("questionType"))){
            queryWrapper.eq("question_type", params.get("questionType"));
        }
        IPage<QuestionnaireQuestionEntity> page = this.page(
                new Query<QuestionnaireQuestionEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<QuestionnaireQuestionEntity> updateWrapper = new UpdateWrapper<QuestionnaireQuestionEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public List<QuestionnaireQuestionEntity> listAll() {
        QueryWrapper<QuestionnaireQuestionEntity> queryWrapper = new QueryWrapper<QuestionnaireQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<QuestionnaireQuestionEntity> getByQuestionIds(List<String> questionIds) {
        QueryWrapper<QuestionnaireQuestionEntity> queryWrapper = new QueryWrapper<QuestionnaireQuestionEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.in("uuid",questionIds);
        List<QuestionnaireQuestionEntity> questionnaireQuestionEntityList = this.baseMapper.selectList(queryWrapper);
        if(null != questionnaireQuestionEntityList && questionnaireQuestionEntityList.size() > 0){
            for (QuestionnaireQuestionEntity questionnaireQuestionEntity : questionnaireQuestionEntityList) {
                String questionType = questionnaireQuestionEntity.getQuestionType();
                if("radio".equals(questionType)){
                    questionnaireQuestionEntity.setQuestionType("单项选择");
                }
                if("multi".equals(questionType)){
                    questionnaireQuestionEntity.setQuestionType("多项选择");
                }
                if("discussion".equals(questionType)){
                    questionnaireQuestionEntity.setQuestionType("简答");
                }
                if("article".equals(questionType)){
                    questionnaireQuestionEntity.setQuestionType("长文");
                }
            }
        }
        return questionnaireQuestionEntityList;
    }
}