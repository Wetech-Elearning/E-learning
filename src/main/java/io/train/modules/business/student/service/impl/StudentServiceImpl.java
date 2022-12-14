package io.train.modules.business.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.train.common.service.ExcelService;
import io.train.common.utils.DateUtils;
import io.train.modules.business.company.entity.CompanyEntity;
import io.train.modules.business.company.service.CompanyService;
import io.train.modules.business.organization.entity.OrganizationEntity;
import io.train.modules.business.organization.service.OrganizationService;
import io.train.modules.business.student.dao.StudentDao;
import io.train.modules.business.student.entity.StudentEntity;
import io.train.modules.business.student.service.StudentService;
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


@Service("studentService")
public class StudentServiceImpl extends ServiceImpl<StudentDao, StudentEntity> implements StudentService {

    @Autowired
    CompanyService companyService;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    ExcelService excelService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
    	QueryWrapper<StudentEntity> queryWrapper = new QueryWrapper<StudentEntity>();
    	if(params.containsKey("delids")){
    		String content = ""+params.get("delids");
    		content.split(",");
    		queryWrapper.notIn("uuid", content.split(","));
    	}
        if(params.containsKey("studentName")){
            queryWrapper.and(
                    QueryWrapper -> QueryWrapper.like("user_name", params.get("studentName"))
                            .or().like("surname", params.get("studentName"))
            );
        }
        queryWrapper.eq("delete_flag",0);
        if(null != params.get("userName") && !"".equals(params.get("userName").toString())){
            queryWrapper.and(
                    QueryWrapper -> QueryWrapper.like("user_name", params.get("userName"))
                            .or().like("surname", params.get("userName"))
            );
        }
        queryWrapper.orderByDesc("create_date");
        IPage<StudentEntity> page = this.page(
                new Query<StudentEntity>().getPage(params),
                queryWrapper
        );
        List<StudentEntity> studentEntityList = new ArrayList<StudentEntity>();
        page.getRecords().stream().forEach((StudentEntity studentEntity) -> {
            //???????????????id?????????????????????
            OrganizationEntity organizationEntity = organizationService.getById(studentEntity.getRelatedCompany());
            if(organizationEntity != null){
                studentEntity.setRelatedCompany(organizationEntity.getOrgName());
            }
            //???????????????id?????????????????????
            OrganizationEntity organizationEntity1 = organizationService.getById(studentEntity.getRelatedDepartment());
            if(organizationEntity1 != null){
                studentEntity.setRelatedDepartment(organizationEntity1.getOrgName());
            }
            //???????????????id?????????????????????
            OrganizationEntity organizationEntity2 = organizationService.getById(studentEntity.getRelatedOffice());
            if(organizationEntity2 != null){
                studentEntity.setRelatedOffice(organizationEntity2.getOrgName());
            }
            //????????????
            SysUserEntity sysUserEntity = sysUserService.queryByAccount(studentEntity.getAccount());
            if(null != sysUserEntity){
                studentEntity.setStatus(sysUserEntity.getStatus());
            }else{
                studentEntity.setStatus(0);
            }
            studentEntityList.add(studentEntity);
        });
        page.setRecords(studentEntityList);

        return new PageUtils(page);
    }

	@Override
	public List<StudentEntity> getStudentListByClassId(Long classUuid) {
		return this.baseMapper.getUserListByClassId(""+classUuid,new Date().getTime());
	}

    @Override
    public List<StudentEntity> getStudentByRelateOffice(Long officeId) {
        QueryWrapper<StudentEntity> queryWrapper = new QueryWrapper<StudentEntity>();
        queryWrapper.eq("related_office",officeId);
        queryWrapper.eq("delete_flag","0");
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<StudentEntity> getStudentLikeName(String studentName) {
        QueryWrapper<StudentEntity> queryWrapper = new QueryWrapper<StudentEntity>();
        queryWrapper.eq("delete_flag","0");
        if(null != studentName && !"".equals(studentName)){
            queryWrapper.and(
                    QueryWrapper -> QueryWrapper.like("user_name", studentName)
                            .or().like("surname", studentName)
            );
        }
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<StudentEntity> getAllStudent(String studentName) {
        QueryWrapper<StudentEntity> queryWrapper = new QueryWrapper<StudentEntity>();
        // queryWrapper.like("user_name",studentName);
        queryWrapper.eq("delete_flag","0");
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void removeByIds(List<String> uuids) {
        UpdateWrapper<StudentEntity> updateWrapper = new UpdateWrapper<StudentEntity>();
        updateWrapper.set("delete_flag", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN2));
        updateWrapper.in("uuid",uuids);
        this.baseMapper.update(null,updateWrapper);
    }

	@Override
	public PageUtils queryByClassPage(Map<String, Object> params) {
		return null;
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
            QueryWrapper<StudentEntity> queryWrapper = new QueryWrapper<StudentEntity>();
            queryWrapper.eq("delete_flag", "0");
            List<Map<String, Object>> studentEntityList = this.listMaps(queryWrapper);
            for (Map<String, Object> map : studentEntityList) {
                Map<String, Object> temp = new HashMap<String, Object>();
                temp.put("userName",String.valueOf(map.get("surname")) + String.valueOf(map.get("userName")));
                temp.put("account",map.get("account"));
                temp.put("userMobile",map.get("userMobile"));
                temp.put("userEmail",map.get("userEmail"));
                temp.put("sex",map.get("sex"));
                // ??????
                temp.put("sex","0".equals(String.valueOf(map.get("sex")))?"???":"???");
                // ??????
                SysUserEntity sysUserEntity = sysUserService.queryByAccount(String.valueOf(map.get("account")));
                if(null != sysUserEntity){
                    temp.put("status",1 == sysUserEntity.getStatus() ? "??????" : "??????");
                }else {
                    temp.put("status","??????");
                }
                // ????????????
                CompanyEntity companyEntity = companyService.getById(String.valueOf(map.get("relatedCompany")));
                if(null != companyEntity){
                    temp.put("company",companyEntity.getCompanyName());
                }
                // ????????????
                OrganizationEntity organizationEntity = organizationService.getById(String.valueOf(map.get("relatedDepartment")));
                if(null != organizationEntity){
                    temp.put("department",organizationEntity.getOrgName());
                }
                list.add(temp);
            }
            // ????????????
            Map headMap = new LinkedHashMap();
            headMap.put("userName","??????");
            headMap.put("account","??????");
            headMap.put("userMobile","??????");
            headMap.put("userEmail","??????");
            headMap.put("company","????????????");
            headMap.put("department","????????????");
            headMap.put("sex","??????");
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
