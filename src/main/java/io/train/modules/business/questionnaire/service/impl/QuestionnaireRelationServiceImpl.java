package io.train.modules.business.questionnaire.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.business.questionnaire.dao.QuestionnaireRelationDao;
import io.train.modules.business.questionnaire.entity.QuestionnaireEntity;
import io.train.modules.business.questionnaire.entity.QuestionnaireRelationEntity;
import io.train.modules.business.questionnaire.service.QuestionnaireRelationService;
import io.train.modules.business.questionnaire.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service("questionnaireRelationService")
public class QuestionnaireRelationServiceImpl extends ServiceImpl<QuestionnaireRelationDao, QuestionnaireRelationEntity> implements QuestionnaireRelationService {

    @Autowired
    private QuestionnaireService questionnaireService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<QuestionnaireRelationEntity> queryWrapper = new QueryWrapper<QuestionnaireRelationEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("status",0);
        if(null != params.get("userId") && !"".equals(params.get("userId").toString())){
            queryWrapper.eq("accept_user_id",params.get("userId"));
        }
        IPage<QuestionnaireRelationEntity> page = this.page(
                new Query<QuestionnaireRelationEntity>().getPage(params),
                queryWrapper
        );
        if(page.getRecords().size() > 0){
            page.getRecords().stream().forEach((QuestionnaireRelationEntity questionnaireRelationEntity)->{
                QuestionnaireEntity questionnaireEntity = questionnaireService.getById(questionnaireRelationEntity.getQuestionnaireId());
                if(null != questionnaireEntity){
                    questionnaireRelationEntity.setQuestionnaireName(questionnaireEntity.getQuestionnaireName());
                }
            });
        }
        return new PageUtils(page);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<QuestionnaireRelationEntity> updateWrapper = new UpdateWrapper<QuestionnaireRelationEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public void changeStatusByIds(List<String> uuids,String userId) {
        UpdateWrapper<QuestionnaireRelationEntity> updateWrapper = new UpdateWrapper<QuestionnaireRelationEntity>();
        updateWrapper.set("status", 1);
        updateWrapper.in("questionnaire_id",uuids);
        updateWrapper.eq("accept_user_id",userId);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public Integer countByUserId(String userId) {
        QueryWrapper<QuestionnaireRelationEntity> queryWrapper = new QueryWrapper<QuestionnaireRelationEntity>();
        queryWrapper.eq("delete_flag", "0");
        queryWrapper.eq("status","0");
        queryWrapper.eq("accept_user_id",userId);
        return this.baseMapper.selectCount(queryWrapper);
    }

    @Override
    public List<String> getByUserId(String userId, String status) {
        QueryWrapper<QuestionnaireRelationEntity> queryWrapper = new QueryWrapper<QuestionnaireRelationEntity>();
        queryWrapper.eq("delete_flag", "0");
        queryWrapper.eq("status", status);
        queryWrapper.eq("accept_user_id",userId);
        return null;
    }
}
