package io.train.modules.business.administrator.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import io.swagger.annotations.ApiOperation;
import io.train.common.lang.StringUtils;
import io.train.common.service.ExcelService;
import io.train.config.MyContext;
import io.train.modules.business.administrator.entity.AdministratorsEntity;
import io.train.modules.business.administrator.service.AdministratorsService;
import io.train.modules.business.company.entity.CompanyEntity;
import io.train.modules.business.company.service.CompanyService;
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
 * 
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2021-07-05 10:12:46
 */
@RestController
@RequestMapping("generator/administrators")
public class AdministratorsController extends AbstractController {

    //用户激活url
    @Value("${user.active.url}")
    private String activeUrl;

    //邮件发送者
    @Value("${spring.mail.username}")
    private String mailSender;

    @Resource
    private JavaMailSender javaMailSender;

    @Autowired
    private AdministratorsService administratorsService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    MyContext myContext;

    @Autowired
    ExcelService excelService;

    @Autowired
    private CompanyService companyService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:administrators:administrators:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = administratorsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:administrators:administrators:info")
    public R info(@PathVariable("uuid") String uuid){
		AdministratorsEntity administrators = administratorsService.getById(uuid);

        return R.ok().put("administrators", administrators);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:administrators:administrators:save")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public R save(@RequestBody AdministratorsEntity administrators){
        SysUserEntity sysUser = sysUserService.queryByAccount(administrators.getAccount());
        if(null != sysUser){
            return R.error("账号["+administrators.getAccount()+"]已存在，请重新填写");
        }
		administratorsService.save(administrators);
        //保存到系统用户
        List<Long> roleList = new ArrayList<Long>();
        roleList.add(2l);
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUserId(administrators.getUuid());
        sysUserEntity.setCode(UUID.randomUUID().toString());
        sysUserEntity.setAccount(administrators.getAccount());
        sysUserEntity.setUsername(administrators.getSurname() + administrators.getUserName());
        sysUserEntity.setPassword("123456");
        sysUserEntity.setEmail(administrators.getUserEmail());
        sysUserEntity.setMobile(administrators.getUserMobile());
        sysUserEntity.setRoleIdList(roleList);
        sysUserEntity.setStatus(0);
        sysUserEntity.setCreateUserId(getUserId());
        sysUserEntity.setTenantId(Long.parseLong(administrators.getRelatedCompany()));
        sysUserEntity.setRelateUserId(""+administrators.getUuid());
        sysUserService.saveUser(sysUserEntity);
        //发送邮件
        MailVo mailVo = new MailVo();
        mailVo.setRecipient(administrators.getUserEmail());
        mailVo.setSubject("在线培训平台注册成功,请点击邮件里的链接激活后使用");
        mailVo.setContent("恭喜" + administrators.getUserName() + "注册成功，请点击" + activeUrl + sysUserEntity.getCode());
        MailUtil.sendEmail(mailSender,mailVo,javaMailSender);
        //MailUtil.sendSimpleMail(mailSender,mailVo,javaMailSender);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:administrators:administrators:update")
    public R update(@RequestBody AdministratorsEntity administrators){
		administratorsService.updateById(administrators);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:administrators:administrators:delete")
    public R delete(@RequestBody String[] uuids){
		administratorsService.removeByIds(Arrays.asList(uuids));
        return R.ok();
    }

    /**
     * 下载企业管理员导入模板
     * @author: liyajie
     * @date: 2022/3/4 11:51
     * @param response
     * @param templateName
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @GetMapping("downAdministratorsTemplate")
    public void downAdministratorsTemplate(HttpServletResponse response, String templateName)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=" + templateName);
        OutputStream outputStream = excelService.downExcelTemplate(response.getOutputStream(), templateName);
        outputStream.flush();
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * 企业管理员导入
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
        }else {
            //校验行数，导入行数最大不能超过1000
            if (map.size() > 1000) {
                return R.error("导入行数最大不能超过1000");
            }
            //预校验
            List<Map> validationResult = this.importExcelValidation(map);
            if(validationResult.size() > 0){
                return R.error("预校验失败").put("data",validationResult);
            }
            for (Map item : map) {
                AdministratorsEntity administratorsEntity = new AdministratorsEntity();
                administratorsEntity.setSurname(String.valueOf(item.get("姓氏")));
                administratorsEntity.setUserName(String.valueOf(item.get("名字")));
                administratorsEntity.setAccount(String.valueOf(item.get("账户")));
                administratorsEntity.setUserEmail(String.valueOf(item.get("邮箱")));
                administratorsEntity.setUserMobile(String.valueOf(item.get("手机号")));
                CompanyEntity companyEntity = companyService.getByCompanyName(String.valueOf(String.valueOf(item.get("归属企业"))));
                administratorsEntity.setRelatedCompany(companyEntity.getUuid());
                if(StringUtils.isNotBlank(String.valueOf(item.get("性别"))) && !"null".equals(String.valueOf(item.get("性别")))){
                    if("男".equals(String.valueOf(item.get("性别")))){
                        administratorsEntity.setSex("0");
                    }
                    if("女".equals(String.valueOf(item.get("性别")))){
                        administratorsEntity.setSex("1");
                    }
                }
                if(StringUtils.isNotBlank(String.valueOf(item.get("年龄"))) && !"null".equals(String.valueOf(item.get("年龄")))){
                    administratorsEntity.setAge(Integer.parseInt(String.valueOf(item.get("年龄"))));
                }
                if(StringUtils.isNotBlank(String.valueOf(item.get("备注"))) && !"null".equals(String.valueOf(item.get("备注")))){
                    administratorsEntity.setRemark(String.valueOf(item.get("备注")));
                }
                administratorsEntity.setDeleteFlag("0");
                administratorsEntity.setCreateDate(new Date());
                administratorsService.save(administratorsEntity);
                //保存到系统用户
                List<Long> roleList = new ArrayList<Long>();
                roleList.add(2l);
                SysUserEntity sysUserEntity = new SysUserEntity();
                sysUserEntity.setUserId(administratorsEntity.getUuid());
                sysUserEntity.setUsername(administratorsEntity.getAccount());
                sysUserEntity.setCode(UUID.randomUUID().toString());
                sysUserEntity.setPassword("123456");
                sysUserEntity.setEmail(administratorsEntity.getUserEmail());
                sysUserEntity.setMobile(administratorsEntity.getUserMobile());
                sysUserEntity.setRoleIdList(roleList);
                sysUserEntity.setStatus(0);
                sysUserEntity.setCreateUserId(1l);
                sysUserEntity.setRelateUserId("" + administratorsEntity.getUuid());
                sysUserEntity.setTenantId(getUser().getTenantId());
                sysUserEntity.setCreateTime(new Date());
                sysUserService.saveUser(sysUserEntity);
                //发送邮件
                MailVo mailVo = new MailVo();
                mailVo.setRecipient(administratorsEntity.getUserEmail());
                mailVo.setSubject("在线培训平台注册成功,请点击邮件里的链接激活后使用");
                mailVo.setContent("恭喜" + sysUserEntity.getUsername() + "注册成功，请点击" + activeUrl + sysUserEntity.getCode());
                MailUtil.sendEmail(mailSender,mailVo,javaMailSender);
            }
        }
        return R.ok("导入成功");
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
            Object company = item.get("归属企业");
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
                CompanyEntity companyEntity = companyService.getByCompanyName(String.valueOf(company));
                if(null == companyEntity){
                    result += "导入数据中的所属科室【"+item.get("归属企业")+"】信息错误";
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
     * 导出管理员
     * @author: liyajie 
     * @date: 2022/7/4 12:34
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
        administratorsService.exportExcel(response, fileName);
    }

}
