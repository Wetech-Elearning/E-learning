package io.train.modules.business.questionpark.service.impl;

import io.train.modules.business.questionpark.dao.QuestionParkDao;
import io.train.modules.business.questionpark.entity.QuestionParkContent;
import io.train.modules.business.questionpark.service.QuestionParkService;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;



@Service("questionParkService")
public class QuestionParkServiceImpl extends ServiceImpl<QuestionParkDao, QuestionParkContent> implements QuestionParkService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<QuestionParkContent> page = this.page(
                new Query<QuestionParkContent>().getPage(params),
                new QueryWrapper<QuestionParkContent>()
        );

        return new PageUtils(page);
    }

}