package io.train.modules.business.exam.service.impl;

import io.train.modules.business.exam.dao.ExamInfoDao;
import io.train.modules.business.exam.entity.ExamInfoEntity;
import io.train.modules.business.exam.service.ExamInfoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;


@Service("examInfoService")
public class ExamInfoServiceImpl extends ServiceImpl<ExamInfoDao, ExamInfoEntity> implements ExamInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ExamInfoEntity> page = this.page(
                new Query<ExamInfoEntity>().getPage(params),
                new QueryWrapper<ExamInfoEntity>()
        );

        return new PageUtils(page);
    }

	@Override
	public List<ExamInfoEntity> queryAll(Map<String, Object> params) {
		return this.list(new QueryWrapper<ExamInfoEntity>());
	}

}