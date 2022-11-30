package io.train.modules.business.questionnaire.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.business.questionnaire.dao.QuestionnaireDetailDao;
import io.train.modules.business.questionnaire.entity.QuestionnaireDetailEntity;
import io.train.modules.business.questionnaire.service.QuestionnaireDetailService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("questionnaireDetailService")
public class QuestionnaireDetailServiceImpl extends ServiceImpl<QuestionnaireDetailDao, QuestionnaireDetailEntity> implements QuestionnaireDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QuestionnaireDetailEntity> page = this.page(
                new Query<QuestionnaireDetailEntity>().getPage(params),
                new QueryWrapper<QuestionnaireDetailEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<QuestionnaireDetailEntity> updateWrapper = new UpdateWrapper<QuestionnaireDetailEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public void changeStatusByIds(List<String> uuids) {
        UpdateWrapper<QuestionnaireDetailEntity> updateWrapper = new UpdateWrapper<QuestionnaireDetailEntity>();
        updateWrapper.set("status", 1);
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public List<QuestionnaireDetailEntity> getQuestionnaireById(String uuid) {
        QueryWrapper<QuestionnaireDetailEntity> queryWrapper = new QueryWrapper<QuestionnaireDetailEntity>();
        queryWrapper.eq("delete_flag",0);
        queryWrapper.eq("questionnaire_id",uuid);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteByQuestionIds(List<String> questionIds, String questionnaireId) {
        QueryWrapper<QuestionnaireDetailEntity> queryWrapper = new QueryWrapper<QuestionnaireDetailEntity>();
        queryWrapper.in("questionnaire_id",questionnaireId);
        queryWrapper.in("question_id",questionIds);
        this.baseMapper.delete(queryWrapper);
    }

    @Override
    public QuestionnaireDetailEntity getByQuestionIdAndQuestionnaireId(String questionnaireId, String questionId) {
        QueryWrapper<QuestionnaireDetailEntity> queryWrapper = new QueryWrapper<QuestionnaireDetailEntity>();
        queryWrapper.eq("questionnaire_id",questionnaireId);
        queryWrapper.eq("question_id",questionId);
        queryWrapper.eq("delete_flag",0);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
