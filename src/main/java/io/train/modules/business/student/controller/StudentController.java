package io.train.modules.business.student.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import io.swagger.annotations.ApiOperation;
import io.train.common.lang.StringUtils;
import io.train.common.service.ExcelService;
import io.train.modules.business.company.entity.CompanyEntity;
import io.train.modules.business.company.service.CompanyService;
import io.train.modules.business.organization.entity.OrganizationEntity;
import io.train.modules.business.organization.service.OrganizationService;
import io.train.modules.business.student.entity.StudentEntity;
import io.train.modules.business.student.service.StudentService;
import io.train.modules.mail.util.MailUtil;
import io.train.modules.mail.vo.MailVo;
import io.train.modules.sys.controller.AbstractController;
import io.train.modules.sys.entity.SysUserEntity;
import io.train.modules.sys.service.SysUserService;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:44:03
 */
@RestController
@RequestMapping("generator/student")
public class StudentController extends AbstractController {

    //用户激活url
    @Value("${user.active.url}")
    private String activeUrl;

    //邮件发送者
    @Value("${spring.mail.username}")
    private String mailSender;

    @Resource
    private JavaMailSender javaMailSender;

    @Autowired
    private StudentService studentService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    ExcelService excelService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:student:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = studentService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 查询所有的学员
     * @author: liyajie
     * @date: 2022/1/24 17:37
     * @param params
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/listAll")
    public R listAll(@RequestParam Map<String, Object> params){
        List<StudentEntity> studentEntityList = studentService.getAllStudent("");
        return R.ok().put("data", studentEntityList);
    }

    /**
     * 列表
     */
    @RequestMapping("/listbystudent")
    @RequiresPermissions("business:student:list")
    public R listbystudent(@RequestParam Map<String, Object> params){
        PageUtils page = studentService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    // @RequiresPermissions("business:student:info")
    public R info(@PathVariable("uuid") String uuid){
		StudentEntity student = studentService.getById(uuid);
        //将所属部门id翻译成部门名称
        OrganizationEntity organizationEntity1 = organizationService.getById(student.getRelatedDepartment());
        if(organizationEntity1 != null){
            student.setRelatedDepartment(organizationEntity1.getOrgName());
        }
        //将所属科室id翻译成科室名称
        OrganizationEntity organizationEntity2 = organizationService.getById(student.getRelatedOffice());
        if(organizationEntity2 != null){
            student.setRelatedOffice(organizationEntity2.getOrgName());
        }
        return R.ok().put("student", student);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:student:add")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public R save(@RequestBody StudentEntity student){
        SysUserEntity sysUser = sysUserService.queryByAccount(student.getAccount());
        if(null != sysUser){
            return R.error("账号["+student.getAccount()+"]已存在，请重新填写");
        }
		studentService.save(student);
        //保存到系统用户
        List<Long> roleList = new ArrayList<Long>();
        roleList.add(4l);
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUserId(student.getUuid());
        sysUserEntity.setUsername(student.getSurname() + student.getUserName());
        sysUserEntity.setAccount(student.getAccount());
        sysUserEntity.setCode(UUID.randomUUID().toString());
        sysUserEntity.setPassword("123456");
        sysUserEntity.setEmail(student.getUserEmail());
        sysUserEntity.setMobile(student.getUserMobile());
        sysUserEntity.setRoleIdList(roleList);
        sysUserEntity.setStatus(0);
        sysUserEntity.setCreateUserId(1l);
        sysUserEntity.setRelateUserId(""+student.getUuid());
        sysUserEntity.setTenantId(getUser().getTenantId());
        sysUserEntity.setCreateTime(new Date());
        sysUserService.saveUser(sysUserEntity);
        //发送邮件
        MailVo mailVo = new MailVo();
        mailVo.setRecipient(student.getUserEmail());
        mailVo.setSubject("在线培训平台注册成功,请点击邮件里的链接激活后使用");
        mailVo.setContent("恭喜" + sysUserEntity.getUsername() + "注册成功，请点击" + activeUrl + sysUserEntity.getCode());
        MailUtil.sendEmail(mailSender,mailVo,javaMailSender);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:student:update")
    public R update(@RequestBody StudentEntity student){
		studentService.updateById(student);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:student:delete")
    public R delete(@RequestBody String[] uuids){
		studentService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

    /**
     * 根据名称模糊查询学员
     * @author: liyajie
     * @date: 2021/8/29 22:40
     * @param params
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/getStudentLikeName")
    public R getStudentLikeName(@RequestParam Map<String, Object> params){
        List<StudentEntity> studentEntityList = studentService.getStudentLikeName(String.valueOf(params.get("studentName")));
        return R.ok().put("data", studentEntityList);
    }

    /**
     * 导入学员
     * @author: liyajie
     * @date: 2021/9/7 10:26
     * @param
     * @return com.ancient.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @ApiOperation(value="导入excel", notes="导入excel")
    @PostMapping("importExcel")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public R importExcel(@RequestBody MultipartFile file, HttpServletRequest request){
        Collection<Map> map = excelService.importExcel(file,request);
        if(null == map){
            return R.error("导入失败");
        }else{
            //校验行数，导入行数最大不能超过1000
            if(map.size() > 1000){
                return R.error("导入行数最大不能超过1000");
            }
            //预校验
            List<Map> validationResult = this.importExcelValidation(map);
            if(validationResult.size() > 0){
                return R.error("预校验失败").put("data",validationResult);
            }
            for (Map item : map) {
                //保存学生信息
                StudentEntity student = new StudentEntity();
                student.setDeleteFlag("0");
                student.setCreateDate(new Date());
                student.setSurname(String.valueOf(item.get("姓氏")));
                student.setUserName(String.valueOf(item.get("名字")));
                student.setAccount(String.valueOf(item.get("账户")));
                student.setUserMobile(String.valueOf(item.get("手机号")));
                student.setUserEmail(String.valueOf(item.get("邮箱")));
                if(StringUtils.isNotBlank(String.valueOf(item.get("入职年数"))) && !"null".equals(String.valueOf(item.get("入职年数")))){
                    student.setEmploymentTime(String.valueOf(item.get("入职年数")));
                }
                if(StringUtils.isNotBlank(String.valueOf(item.get("已培训科目"))) && !"null".equals(String.valueOf(item.get("已培训科目")))){
                    student.setSubjects(String.valueOf(item.get("已培训科目")));
                }
                if(StringUtils.isNotBlank(String.valueOf(item.get("性别"))) && !"null".equals(String.valueOf(item.get("性别")))){
                    if("男".equals(String.valueOf(item.get("性别")))){
                        student.setSex("0");
                    }
                    if("女".equals(String.valueOf(item.get("性别")))){
                        student.setSex("1");
                    }
                }
                if(StringUtils.isNotBlank(String.valueOf(item.get("年龄"))) && !"null".equals(String.valueOf(item.get("年龄")))){
                    student.setAge(Integer.parseInt(String.valueOf(item.get("年龄"))));
                }
                CompanyEntity companyEntity = companyService.getByCompanyName(String.valueOf(item.get("所属公司")));
                if(null != companyEntity){
                    List<OrganizationEntity> organizationEntityList = organizationService.getByOrgNameAndParentOrgId(String.valueOf(item.get("所属公司")),0l);
                    if(null != organizationEntityList && organizationEntityList.size() > 0){
                        OrganizationEntity organizationEntity = organizationEntityList.get(0);
                        student.setRelatedCompany(String.valueOf(organizationEntity.getUuid()));
                        List<OrganizationEntity> departmentList = organizationService.getByParentOrgId(organizationEntity.getUuid());
                        if(null != departmentList && departmentList.size() > 0){
                            String itemDepartment = String.valueOf(item.get("所属部门"));
                            Boolean isExistDepartmentFlag = false;
                            for (OrganizationEntity department: departmentList) {
                                if(itemDepartment.equals(department.getOrgName())){
                                    isExistDepartmentFlag = true;
                                    student.setRelatedDepartment(String.valueOf(department.getUuid()));
                                }
                            }
                            if(!isExistDepartmentFlag){
                                return R.error("导入数据中的所属部门【"+item.get("所属部门")+"】信息错误,请检查");
                            }else{
                                List<OrganizationEntity> officeList = organizationService.getByParentOrgId(Long.parseLong(student.getRelatedDepartment()));
                                if(null != officeList && officeList.size() > 0){
                                    String itemOffice = String.valueOf(item.get("所属科室"));
                                    Boolean isExistOfficeFlag = false;
                                    for (OrganizationEntity office: officeList) {
                                        if(itemOffice.equals(office.getOrgName())){
                                            isExistOfficeFlag = true;
                                            student.setRelatedOffice(String.valueOf(office.getUuid()));
                                        }
                                    }
                                    if(!isExistOfficeFlag){
                                        return R.error("导入数据中的所属科室【"+item.get("所属科室")+"】信息错误,请检查");
                                    }
                                }else{
                                    return R.error("导入数据中的所属科室【"+item.get("所属科室")+"】信息错误,请检查");
                                }
                            }
                        }else{
                            return R.error("导入数据中的所属部门【"+item.get("所属部门")+"】信息错误,请检查");
                        }
                    }
                }else{
                    return R.error("导入数据中的所属公司【"+item.get("所属公司")+"】信息错误,请检查");
                }
                studentService.save(student);
                //保存到系统用户
                List<Long> roleList = new ArrayList<Long>();
                roleList.add(4l);
                SysUserEntity sysUserEntity = new SysUserEntity();
                sysUserEntity.setUserId(student.getUuid());
                sysUserEntity.setUsername(student.getAccount());
                sysUserEntity.setCode(UUID.randomUUID().toString());
                sysUserEntity.setPassword("123456");
                sysUserEntity.setEmail(student.getUserEmail());
                sysUserEntity.setMobile(student.getUserMobile());
                sysUserEntity.setRoleIdList(roleList);
                sysUserEntity.setStatus(0);
                sysUserEntity.setCreateUserId(1l);
                sysUserEntity.setRelateUserId(""+student.getUuid());
                sysUserEntity.setTenantId(getUser().getTenantId());
                sysUserEntity.setCreateTime(new Date());
                sysUserService.saveUser(sysUserEntity);
                //发送邮件
                MailVo mailVo = new MailVo();
                mailVo.setRecipient(student.getUserEmail());
                mailVo.setSubject("在线培训平台注册成功,请点击邮件里的链接激活后使用");
                mailVo.setContent("恭喜" + sysUserEntity.getUsername() + "注册成功，请点击" + activeUrl + sysUserEntity.getCode());
                MailUtil.sendEmail(mailSender,mailVo,javaMailSender);
            }
        }
        return R.ok("导入成功");
    }

    /**
     * 下载学生导入模板
     * @author: liyajie
     * @date: 2022/3/4 11:51
     * @param response
     * @param templateName
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @GetMapping("downStudentTemplate")
    public void downStudentTemplate(HttpServletResponse response, String templateName)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=" + templateName);
        OutputStream outputStream = excelService.downExcelTemplate(response.getOutputStream(), templateName);
        outputStream.flush();
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * 导入学员预校验
     * @author: liyajie
     * @date: 2021/9/7 10:26
     * @param
     * @return String
     * @exception:
     * @update:
     * @updatePerson:
     **/
    private List<Map> importExcelValidation(Collection<Map> map) {
        List<Map> list = new ArrayList();
        int i = 0;
        for (Map item : map) {
            i = i + 1;
            String result = "";
            Object surname = item.get("姓氏");
            Object name = item.get("名字");
            Object account = item.get("账户");
            Object mail = item.get("邮箱");
            Object mobile = item.get("手机号");
            Object company = item.get("所属公司");
            Object department = item.get("所属部门");
            Object office = item.get("所属科室");
            if(null == surname){
                result += "姓氏不能为空;";
            }
            if(null == name){
                result += "名字不能为空;";
            }
            if(null == account){
                result += "账号不能为空;";
            }else{
                SysUserEntity sysUserEntity = sysUserService.queryByAccount(String.valueOf(account));
                if(null != sysUserEntity){
                    result += "账号【" + account + "】已存在;";
                }
            }
            if(null == mail){
                result += "邮箱不能为空;";
            }
            if(null == mobile){
                result += "手机号不能为空;";
            }
            if(null == company){
                result += "所属公司不能为空;";
            }else{
                String departmentId = "";
                CompanyEntity companyEntity = companyService.getByCompanyName(String.valueOf(item.get("所属公司")));
                if(null != companyEntity){
                    if(null == department){
                        result += "【所属部门】不能为空;";
                    }else{
                        List<OrganizationEntity> organizationEntityList = organizationService.getByOrgNameAndParentOrgId(String.valueOf(item.get("所属公司")),0l);
                        if(null != organizationEntityList && organizationEntityList.size() > 0){
                            OrganizationEntity organizationEntity = organizationEntityList.get(0);
                            List<OrganizationEntity> departmentList = organizationService.getByParentOrgId(organizationEntity.getUuid());
                            if(null != departmentList && departmentList.size() > 0){
                                String itemDepartment = String.valueOf(item.get("所属部门"));
                                Boolean isExistDepartmentFlag = false;
                                for (OrganizationEntity departmentOrg: departmentList) {
                                    if(itemDepartment.equals(departmentOrg.getOrgName())){
                                        isExistDepartmentFlag = true;
                                        departmentId = String.valueOf(departmentOrg.getUuid());
                                        break;
                                    }
                                }
                                if(!isExistDepartmentFlag){
                                    result += "导入数据中的所属部门【"+item.get("所属部门")+"】信息错误";
                                }else{
                                    if(null == office){
                                        result += "所属科室不能为空;";
                                    }else{
                                        List<OrganizationEntity> officeList = organizationService.getByParentOrgId(Long.parseLong(departmentId));
                                        if(null != officeList && officeList.size() > 0){
                                            String itemOffice = String.valueOf(item.get("所属科室"));
                                            Boolean isExistOfficeFlag = false;
                                            for (OrganizationEntity officeOrg: officeList) {
                                                if(itemOffice.equals(officeOrg.getOrgName())){
                                                    isExistOfficeFlag = true;
                                                    break;
                                                }
                                            }
                                            if(!isExistOfficeFlag){
                                                result += "导入数据中的所属科室【"+item.get("所属科室")+"】信息错误";
                                            }
                                        }else{
                                            result += "导入数据中的所属科室【"+item.get("所属科室")+"】信息错误";
                                        }
                                    }
                                }
                            }else{
                                result += "导入数据中的所属部门【"+item.get("所属部门")+"】信息错误";
                            }
                        }
                    }
                }else{
                    result += "导入数据中的所属公司【"+item.get("所属公司")+"】信息错误";
                }
            }
            if(!"".equals(result)){
                item.put("序号","第"+i+"行");
                item.put("校验失败",result);
                list.add(item);
            }
        }
        return list;
    }

    /**
     * 导出学员
     * @author: liyajie 
     * @date: 2022/7/4 12:35
     * @param request
     * @param response
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @ApiOperation(value="导出excel", notes="导出excel")
    @GetMapping("exportExcel")
    public void exportExcel(HttpServletRequest request,HttpServletResponse response){
        String fileName = request.getParameter("fileName");
        studentService.exportExcel(response, fileName);
    }
}
