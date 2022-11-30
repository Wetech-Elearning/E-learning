package io.train.modules.business.questionnaire.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.business.questionnaire.dao.QuestionnaireRecordDao;
import io.train.modules.business.questionnaire.entity.QuestionnaireRecordEntity;
import io.train.modules.business.questionnaire.service.QuestionnaireRecordService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


@Service("questionnaireRecordService")
public class QuestionnaireRecordServiceImpl extends ServiceImpl<QuestionnaireRecordDao, QuestionnaireRecordEntity> implements QuestionnaireRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QuestionnaireRecordEntity> page = this.page(
                new Query<QuestionnaireRecordEntity>().getPage(params),
                new QueryWrapper<QuestionnaireRecordEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<QuestionnaireRecordEntity> updateWrapper = new UpdateWrapper<QuestionnaireRecordEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }
}