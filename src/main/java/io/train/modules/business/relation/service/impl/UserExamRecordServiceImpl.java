package io.train.modules.business.relation.service.impl;

import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.business.relation.dao.UserExamRecordDao;
import io.train.modules.business.relation.entity.UserExamRecordEntity;
import io.train.modules.business.relation.service.UserExamRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



@Service("userExamRecordService")
public class UserExamRecordServiceImpl extends ServiceImpl<UserExamRecordDao, UserExamRecordEntity> implements UserExamRecordService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserExamRecordEntity> page = this.page(
                new Query<UserExamRecordEntity>().getPage(params),
                new QueryWrapper<UserExamRecordEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public UserExamRecordEntity getCurrentUserAnswers(String paperId, String questionId, String userId, Integer examNum) {
        QueryWrapper<UserExamRecordEntity> queryWrapper = new QueryWrapper<UserExamRecordEntity>();
        queryWrapper.eq("paper_id",paperId);
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("question_id",questionId);
        queryWrapper.eq("exam_num",examNum);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<UserExamRecordEntity> getCurrentUserPapteAnswers(String paperId, String userId, Integer examNum) {
        QueryWrapper<UserExamRecordEntity> queryWrapper = new QueryWrapper<UserExamRecordEntity>();
        queryWrapper.eq("paper_id",paperId);
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("exam_num",examNum);
        return baseMapper.selectList(queryWrapper);
    }
}