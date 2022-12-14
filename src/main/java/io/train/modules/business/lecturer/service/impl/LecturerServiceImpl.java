package io.train.modules.business.lecturer.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.service.ExcelService;
import io.train.common.utils.DateUtils;
import io.train.modules.business.company.service.CompanyService;
import io.train.modules.business.lecturer.dao.LecturerDao;
import io.train.modules.business.lecturer.entity.LecturerEntity;
import io.train.modules.business.lecturer.service.LecturerService;
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


@Service("lecturerService")
public class LecturerServiceImpl extends ServiceImpl<LecturerDao, LecturerEntity> implements LecturerService {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    ExcelService excelService;

    @Autowired
    CompanyService companyService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<LecturerEntity> queryWrapper = new QueryWrapper<LecturerEntity>();
        queryWrapper.eq("delete_flag",0);
        if(null != params.get("lecturerName") && !"".equals(params.get("lecturerName").toString())){
            queryWrapper.and(
                QueryWrapper -> QueryWrapper.like("lecturer_name", params.get("lecturerName"))
                        .or().like("surname", params.get("lecturerName"))
            );
        }
        queryWrapper.orderByDesc("create_date");
        IPage<LecturerEntity> page = this.page(
                new Query<LecturerEntity>().getPage(params),
                queryWrapper
        );
        page.getRecords().stream().forEach((LecturerEntity lecturerEntity) -> {
            //??????
            SysUserEntity sysUserEntity = sysUserService.queryByAccount(lecturerEntity.getAccount());
            if(null != sysUserEntity){
                lecturerEntity.setStatus(sysUserEntity.getStatus());
            }else {
                lecturerEntity.setStatus(0);
            }
        });

        return new PageUtils(page);
    }

    @Override
    public List<LecturerEntity> getLecturerLikeName(String lecturerName) {
        QueryWrapper<LecturerEntity> queryWrapper = new QueryWrapper<LecturerEntity>();
        queryWrapper.like("lecturer_name",lecturerName);
        queryWrapper.eq("delete_flag",0);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<LecturerEntity> getAllLecturer(String lecturerName) {
        QueryWrapper<LecturerEntity> queryWrapper = new QueryWrapper<LecturerEntity>();
        // queryWrapper.eq("lecturer_name",lecturerName);
        queryWrapper.eq("delete_flag",0);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public LecturerEntity getByAccount(String account) {
        QueryWrapper<LecturerEntity> queryWrapper = new QueryWrapper<LecturerEntity>();
        queryWrapper.eq("account",account);
        queryWrapper.eq("delete_flag",0);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<LecturerEntity> updateWrapper = new UpdateWrapper<LecturerEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

    @Override
    public void exportExcel(HttpServletResponse response, String fileName) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            // ???????????????
            OutputStream os = response.getOutputStream();
            // ???????????????
            response.reset();
            //????????????
            QueryWrapper<LecturerEntity> queryWrapper = new QueryWrapper<LecturerEntity>();
            queryWrapper.eq("delete_flag", "0");
            List<Map<String, Object>> administratorsEntityList = this.listMaps(queryWrapper);
            for (Map<String, Object> map : administratorsEntityList) {
                Map<String, Object> temp = new HashMap<String, Object>();
                temp.put("userName",String.valueOf(map.get("surname")) + String.valueOf(map.get("lecturerName")));
                temp.put("account",map.get("account"));
                temp.put("userMobile",map.get("lecturerMobile"));
                temp.put("lecturerIntroduction",map.get("lecturerIntroduction"));
                temp.put("sex",map.get("sex"));
                temp.put("age",map.get("age"));
                temp.put("remark",map.get("remark"));
                // ??????
                temp.put("sex","0".equals(String.valueOf(map.get("sex")))?"???":"???");
                // ??????
                SysUserEntity sysUserEntity = sysUserService.queryByAccount(String.valueOf(map.get("account")));
                if(null != sysUserEntity){
                    temp.put("status",1 == sysUserEntity.getStatus() ? "??????" : "??????");
                }else {
                    temp.put("status","??????");
                }
                list.add(temp);
            }
            // ????????????
            Map headMap = new LinkedHashMap();
            headMap.put("userName","??????");
            headMap.put("account","??????");
            headMap.put("userMobile","????????????");
            headMap.put("lecturerIntroduction","??????");
            headMap.put("sex","??????");
            headMap.put("age","??????");
            headMap.put("status","??????");
            headMap.put("remark","??????");
            // ??????
            excelService.exportExcel(list, headMap, os);
            // ?????????????????????
            response.setHeader("Content-disposition","attachment; filename=" + new String(fileName.getBytes("utf-8"), "8859_1") + ".xls");
            // ??????????????????
            response.setContentType("application/msexcel");
            IOUtils.closeQuietly(os);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
