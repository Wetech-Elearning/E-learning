package io.train.modules.business.questionnaire.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.business.questionnaire.dao.QuestionnaireDao;
import io.train.modules.business.questionnaire.entity.QuestionnaireEntity;
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


@Service("questionnaireService")
public class QuestionnaireServiceImpl extends ServiceImpl<QuestionnaireDao, QuestionnaireEntity> implements QuestionnaireService {

    @Autowired
    QuestionnaireRelationService questionnaireRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<QuestionnaireEntity> queryWrapper = new QueryWrapper<QuestionnaireEntity>();
        queryWrapper.orderByDesc("create_date");
        if(null != params.get("questionnaireName") && !"".equals(params.get("questionnaireName"))){
            queryWrapper.like("questionnaire_name", params.get("questionnaireName"));
        }
        IPage<QuestionnaireEntity> page = this.page(
                new Query<QuestionnaireEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<QuestionnaireEntity> updateWrapper = new UpdateWrapper<QuestionnaireEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public void updateStatus(int status, String uuid) {
        UpdateWrapper<QuestionnaireEntity> updateWrapper = new UpdateWrapper<QuestionnaireEntity>();
        updateWrapper.set("status", status);
        updateWrapper.eq("uuid",uuid);
        this.baseMapper.update(null,updateWrapper);
    }
}
