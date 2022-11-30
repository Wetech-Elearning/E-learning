package io.train.modules.business.relation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.train.modules.business.relation.dao.UserExamInfoDao;
import io.train.modules.business.relation.entity.UserExamInfoEntity;
import io.train.modules.business.relation.service.UserExamInfoService;
import org.springframework.stereotype.Service;

import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;


@Service("userExamInfoService")
public class UserExamInfoServiceImpl extends ServiceImpl<UserExamInfoDao, UserExamInfoEntity> implements UserExamInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserExamInfoEntity> page = this.page(
                new Query<UserExamInfoEntity>().getPage(params),
                new QueryWrapper<UserExamInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Integer getMaxNum(String uuid, String userId) {
        QueryWrapper<UserExamInfoEntity> queryWrapper = new QueryWrapper<UserExamInfoEntity>();
        queryWrapper.eq("exam_paper_id", uuid).eq("user_id", userId);
        return baseMapper.selectList(queryWrapper).size();
    }

    @Override
    public void saveUserExamInfo(UserExamInfoEntity userExamInfo) {
        QueryWrapper<UserExamInfoEntity> queryWrapper = new QueryWrapper<UserExamInfoEntity>();
        queryWrapper.eq("exam_paper_id", userExamInfo.getExamPaperId()).eq("user_id", userExamInfo.getUserId());
        Integer num = this.baseMapper.selectList(queryWrapper).size();
        userExamInfo.setExamNum(num + 1);
        this.save(userExamInfo);
    }

    @Override
    public Integer getMaxExamNumByPaperIdAndUserId(String paperId, String userId) {
        Integer examNum = 0;
        UserExamInfoEntity userExamInfoEntity = this.getOne(new LambdaQueryWrapper<UserExamInfoEntity>().eq(UserExamInfoEntity::getExamPaperId, paperId).eq(UserExamInfoEntity::getUserId, userId).orderByDesc(UserExamInfoEntity::getExamNum).last("limit 1"));
        if(null != userExamInfoEntity){
            examNum = userExamInfoEntity.getExamNum();
        }
        return examNum;
    }
}