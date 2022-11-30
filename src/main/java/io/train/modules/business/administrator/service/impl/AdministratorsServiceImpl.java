package io.train.modules.business.administrator.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.service.ExcelService;
import io.train.common.utils.DateUtils;
import io.train.modules.business.administrator.dao.AdministratorsDao;
import io.train.modules.business.administrator.entity.AdministratorsEntity;
import io.train.modules.business.administrator.service.AdministratorsService;
import io.train.modules.business.company.entity.CompanyEntity;
import io.train.modules.business.company.service.CompanyService;
import io.train.modules.sys.entity.SysUserEntity;
import io.train.modules.sys.service.SysUserService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;

import javax.servlet.http.HttpServletResponse;


@Service("administratorsService")
public class AdministratorsServiceImpl extends ServiceImpl<AdministratorsDao, AdministratorsEntity> implements AdministratorsService {
    @Autowired
    CompanyService companyService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    ExcelService excelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<AdministratorsEntity> queryWrapper = new QueryWrapper<AdministratorsEntity>();
        queryWrapper.eq("delete_flag",0);
        if(null != params.get("userName") && !"".equals(params.get("userName").toString())){
            queryWrapper.and(
                    QueryWrapper -> QueryWrapper.like("user_name", params.get("userName"))
                            .or().like("surname", params.get("userName"))
            );
        }
        queryWrapper.orderByDesc("create_date");
        IPage<AdministratorsEntity> page = this.page(
                new Query<AdministratorsEntity>().getPage(params),
                queryWrapper
        );
        if(page.getRecords().size() > 0){
            page.getRecords().stream().forEach((AdministratorsEntity administratorsEntity)->{
                CompanyEntity companyEntity = companyService.getById(administratorsEntity.getRelatedCompany());
                if(null != companyEntity){
                    administratorsEntity.setRelatedCompany(companyEntity.getCompanyName());
                }
                //状态
                SysUserEntity sysUserEntity = sysUserService.queryByAccount(administratorsEntity.getAccount());
                if(null != sysUserEntity){
                    administratorsEntity.setStatus(sysUserEntity.getStatus());
                }else {
                    administratorsEntity.setStatus(0);
                }
            });
        }
        return new PageUtils(page);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<AdministratorsEntity> updateWrapper = new UpdateWrapper<AdministratorsEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public void exportExcel(HttpServletResponse response, String fileName) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            // 获取输出流
            OutputStream os = response.getOutputStream();
            // 重置输出流
            response.reset();
            //查询数据
            QueryWrapper<AdministratorsEntity> queryWrapper = new QueryWrapper<AdministratorsEntity>();
            queryWrapper.eq("delete_flag", "0");
            List<Map<String, Object>> administratorsEntityList = this.listMaps(queryWrapper);
            for (Map<String, Object> map : administratorsEntityList) {
                Map<String, Object> temp = new HashMap<String, Object>();
                temp.put("userName",String.valueOf(map.get("surname")) + String.valueOf(map.get("userName")));
                temp.put("account",map.get("account"));
                temp.put("userMobile",map.get("userMobile"));
                temp.put("userEmail",map.get("userEmail"));
                temp.put("sex",map.get("sex"));
                temp.put("age",map.get("age"));
                temp.put("remark",map.get("remark"));
                // 性别
                temp.put("sex","0".equals(String.valueOf(map.get("sex")))?"男":"女");
                // 状态
                SysUserEntity sysUserEntity = sysUserService.queryByAccount(String.valueOf(map.get("account")));
                if(null != sysUserEntity){
                    temp.put("status",1 == sysUserEntity.getStatus() ? "正常" : "禁用");
                }else {
                    temp.put("status","禁用");
                }
                // 所属公司
                CompanyEntity companyEntity = companyService.getById(String.valueOf(map.get("relatedCompany")));
                if(null != companyEntity){
                    temp.put("company",companyEntity.getCompanyName());
                }
                list.add(temp);
            }
            // 组装表头
            Map headMap = new LinkedHashMap();
            headMap.put("userName","名称");
            headMap.put("account","账户");
            headMap.put("userMobile","手机号");
            headMap.put("userEmail","邮箱");
            headMap.put("company","公司");
            headMap.put("sex","性别");
            headMap.put("age","年龄");
            headMap.put("status","状态");
            headMap.put("remark","备注");
            // 导出
            excelService.exportExcel(list, headMap, os);
            // 设定输出文件头
            response.setHeader("Content-disposition","attachment; filename=" + new String(fileName.getBytes("utf-8"), "8859_1") + ".xls");
            // 定义输出类型
            response.setContentType("application/msexcel");
            IOUtils.closeQuietly(os);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
