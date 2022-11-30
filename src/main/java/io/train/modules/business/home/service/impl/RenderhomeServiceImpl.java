package io.train.modules.business.home.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.modules.business.home.dao.RenderHomeDao;
import io.train.modules.business.home.entity.RenderHomeManager;
import io.train.modules.business.home.service.RenderhomeService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;


@Service("renderhomeService")
public class RenderhomeServiceImpl extends ServiceImpl<RenderHomeDao, RenderHomeManager> implements RenderhomeService {

	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
		QueryWrapper<RenderHomeManager> queryWrapper = new QueryWrapper<RenderHomeManager>();
		queryWrapper.eq("delete_flag",0);
        IPage<RenderHomeManager> page = this.page(
                new Query<RenderHomeManager>().getPage(params),
				queryWrapper
        );
        return new PageUtils(page);
    }
   
	@Override
	public void removeByIds(List<String> uuids) {
		UpdateWrapper<RenderHomeManager> updateWrapper = new UpdateWrapper<RenderHomeManager>();
		updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
		updateWrapper.in("uuid",uuids);
		this.baseMapper.update(null,updateWrapper);
	}
}