package io.train.modules.business.site.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.utils.DateUtils;
import io.train.modules.business.site.dao.SiteInfoDao;
import io.train.modules.business.site.entity.SiteInfoEntity;
import io.train.modules.business.site.service.SiteInfoService;
import io.train.modules.sys.entity.SysUserEntity;
import io.train.modules.sys.service.SysUserRoleService;
import io.train.modules.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;



@Service("siteInfoService")
public class SiteInfoServiceImpl extends ServiceImpl<SiteInfoDao, SiteInfoEntity> implements SiteInfoService {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SiteInfoEntity> queryWrapper = new QueryWrapper<SiteInfoEntity>();
        queryWrapper.eq("delete_flag",0);
        if(null != params.get("userId") && !"".equals(params.get("userId").toString())){
            List<Long> roles =sysUserRoleService.queryRoleIdList(Long.parseLong(params.get("userId").toString()));
            if(null != roles && roles.size() > 0){
                if(roles.get(0) == 2L){
                    queryWrapper.eq("send_user_id",params.get("userId"));
                }else {
                    queryWrapper.like("accept_user_id",params.get("userId"));
                }
            }
        }
        if(null != params.get("key") && !"".equals(params.get("key").toString())){
            queryWrapper.like("content",params.get("key"));
        }
        queryWrapper.orderByDesc("send_date");
        IPage<SiteInfoEntity> page = this.page(
                new Query<SiteInfoEntity>().getPage(params),
                queryWrapper
        );
        //把发信人和收信人的id翻译成收信人的名称
        List<SiteInfoEntity> siteInfoEntityList = new ArrayList<SiteInfoEntity>();
        page.getRecords().stream().forEach((SiteInfoEntity siteInfoEntity) -> {
            SysUserEntity sendUser = sysUserService.getById(siteInfoEntity.getSendUserId());
            String[] acceptUserIds = siteInfoEntity.getAcceptUserId().split(",");
            String acceptUserNames = "";
            for (String userId : acceptUserIds) {
                SysUserEntity sysUserEntity = sysUserService.getById(userId);
                if(null != sysUserEntity){
                    acceptUserNames += sysUserEntity.getUsername() + ",";
                }
            }
            if(!"".equals(acceptUserNames)){
                siteInfoEntity.setAcceptUserName(acceptUserNames.substring(0,acceptUserNames.length()-1));
            }
            if("admin".equals(sendUser.getAccount())){
                siteInfoEntity.setSendUserName("管理员");
            }else{
                siteInfoEntity.setSendUserName(sendUser.getUsername());
            }
            siteInfoEntityList.add(siteInfoEntity);
        });
        page.setRecords(siteInfoEntityList);
        return new PageUtils(page);
    }

    @Override
    public Integer countLikeUserId(String userId) {
        int count = 0;
        QueryWrapper<SiteInfoEntity> queryWrapper = new QueryWrapper<SiteInfoEntity>();
        queryWrapper.eq("delete_flag",'0');
        queryWrapper.like("accept_user_id",userId);
        List<SiteInfoEntity> siteInfoEntityList = this.baseMapper.selectList(queryWrapper);
        if(null != siteInfoEntityList && siteInfoEntityList.size() > 0){
            for (SiteInfoEntity siteInfoEntity: siteInfoEntityList) {
                String[] acceptUserids = siteInfoEntity.getAcceptUserId().split(",");
                if(acceptUserids.length > 0){
                    for (String item: acceptUserids) {
                        if (userId.equals(item)){
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    @Override
    public Integer updateSiteInfo(String uuid) {
        UpdateWrapper<SiteInfoEntity> updateWrapper = new UpdateWrapper<SiteInfoEntity>();
        updateWrapper.eq("uuid",uuid);
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        return this.baseMapper.update(null,updateWrapper);
    }
}
