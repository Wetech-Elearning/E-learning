package io.train.modules.business.site.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.business.site.entity.SiteInfoEntity;

import java.util.Map;

/**
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:55:23
 */
public interface SiteInfoService extends IService<SiteInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Integer countLikeUserId(String userId);

    Integer updateSiteInfo(String uuid);
}

