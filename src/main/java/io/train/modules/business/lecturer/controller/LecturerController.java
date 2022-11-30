package io.train.modules.business.lecturer.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import io.swagger.annotations.ApiOperation;
import io.train.common.lang.StringUtils;
import io.train.common.service.ExcelService;
import io.train.modules.business.lecturer.entity.LecturerEntity;
import io.train.modules.business.lecturer.service.LecturerService;
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
 * @date 2021-07-05 10:41:24
 */
@RestController
@RequestMapping("generator/lecturer")
public class LecturerController extends AbstractController {

    //用户激活url
    @Value("${user.active.url}")
    private String activeUrl;

    //邮件发送者
    @Value("${spring.mail.username}")
    private String mailSender;

    @Resource
    private JavaMailSender javaMailSender;

    @Autowired
    private LecturerService lecturerService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    ExcelService excelService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("business:lecturer:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = lecturerService.queryPage(params);

        return R.ok().put("page", page);
    }

    /**
     * 查询所有的讲师
     * @author: liyajie
     * @date: 2022/1/24 17:29
     * @param params
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/listAll")
    public R listAll(@RequestParam Map<String, Object> params){
        List<LecturerEntity> lecturerEntityList = lecturerService.getAllLecturer("");
        return R.ok().put("data", lecturerEntityList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{uuid}")
    @RequiresPermissions("business:lecturer:info")
    public R info(@PathVariable("uuid") String uuid){
		LecturerEntity lecturer = lecturerService.getById(uuid);

        return R.ok().put("lecturer", lecturer);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("business:lecturer:add")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public R save(@RequestBody LecturerEntity lecturer){
        SysUserEntity sysUser = sysUserService.queryByAccount(lecturer.getAccount());
        if(null != sysUser){
            return R.error("账号["+lecturer.getAccount()+"]已存在，请重新填写");
        }
		lecturerService.save(lecturer);
        //保存到系统用户
        List<Long> roleList = new ArrayList<Long>();
        roleList.add(3l);
        SysUserEntity sysUserEntity = new SysUserEntity();
        sysUserEntity.setUserId(lecturer.getUuid());
        sysUserEntity.setUsername(lecturer.getAccount());
        sysUserEntity.setCode(UUID.randomUUID().toString());
        sysUserEntity.setPassword("123456");
        sysUserEntity.setEmail(lecturer.getLecturerEmail());
        sysUserEntity.setMobile(lecturer.getLecturerMobile());
        sysUserEntity.setRoleIdList(roleList);
        sysUserEntity.setStatus(0);
        sysUserEntity.setCreateUserId(1l);
        sysUserEntity.setTenantId(getUser().getTenantId());
        sysUserEntity.setRelateUserId(""+lecturer.getUuid());
        sysUserService.saveUser(sysUserEntity);
        //发送邮件
        MailVo mailVo = new MailVo();
        mailVo.setRecipient(lecturer.getLecturerEmail());
        mailVo.setSubject("在线培训平台注册成功,请点击邮件里的链接激活后使用");
        mailVo.setContent("恭喜" + sysUserEntity.getUsername() + "注册成功，请点击" + activeUrl + sysUserEntity.getCode());
        MailUtil.sendEmail(mailSender,mailVo,javaMailSender);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("business:lecturer:update")
    public R update(@RequestBody LecturerEntity lecturer){
		lecturerService.updateById(lecturer);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("business:lecturer:delete")
    public R delete(@RequestBody String[] uuids){
		lecturerService.removeByIds(Arrays.asList(uuids));

        return R.ok();
    }

    /**
     * 根据名称模糊查询讲师
     * @author: liyajie
     * @date: 2021/8/29 22:40
     * @param params
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @RequestMapping("/getLecturerLikeName")
    @RequiresPermissions("business:lecturer:list")
    public R getLecturerLikeName(@RequestParam Map<String, Object> params){
        List<LecturerEntity> lecturerEntityList = lecturerService.getLecturerLikeName(String.valueOf(params.get("lecturerName")));
        return R.ok().put("data", lecturerEntityList);
    }

    /**
     * 下载讲师导入模板
     * @author: liyajie
     * @date: 2022/3/4 11:51
     * @param response
     * @param templateName
     * @return void
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @GetMapping("downLecturerTemplate")
    public void downLecturerTemplate(HttpServletResponse response, String templateName)throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition", "attachment; filename=" + templateName);
        OutputStream outputStream = excelService.downExcelTemplate(response.getOutputStream(), templateName);
        outputStream.flush();
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * 导入讲师
     * @author: liyajie 
     * @date: 2022/5/25 16:00
     * @param file
     * @param request
     * @return io.train.common.utils.R
     * @exception:
     * @update:
     * @updatePerson:
     **/
    @ApiOperation(value="导入excel", notes="导入excel")
    @PostMapping("importExcel")
    @Transactional(rollbackFor = {Exception.class, Error.class})
    public R importExcel(@RequestBody MultipartFile file, HttpServletRequest request) {
        Collection<Map> map = excelService.importExcel(file, request);
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
                //保存讲师信息
                LecturerEntity lecturerEntity = new LecturerEntity();
                lecturerEntity.setSurname(String.valueOf(item.get("姓氏")));
                lecturerEntity.setLecturerName(String.valueOf(item.get("名字")));
                lecturerEntity.setAccount(String.valueOf(item.get("账户")));
                lecturerEntity.setLecturerMobile(String.valueOf(item.get("手机号")));
                lecturerEntity.setLecturerEmail(String.valueOf(item.get("邮箱")));
                if(StringUtils.isNotBlank(String.valueOf(item.get("讲师简介"))) && !"null".equals(String.valueOf(item.get("讲师简介")))){
                    lecturerEntity.setLecturerIntroduction(String.valueOf(item.get("讲师简介")));
                }
                if(StringUtils.isNotBlank(String.valueOf(item.get("讲师证书"))) && !"null".equals(String.valueOf(item.get("讲师证书")))){
                    lecturerEntity.setLecturerCertificate(String.valueOf(item.get("讲师证书")));
                }
                if(StringUtils.isNotBlank(String.valueOf(item.get("讲师著作"))) && !"null".equals(String.valueOf(item.get("讲师著作")))){
                    lecturerEntity.setLecturerBook(String.valueOf(item.get("讲师著作")));
                }
                if(StringUtils.isNotBlank(String.valueOf(item.get("性别"))) && !"null".equals(String.valueOf(item.get("性别")))){
                    if("男".equals(String.valueOf(item.get("性别")))){
                        lecturerEntity.setSex("0");
                    }
                    if("女".equals(String.valueOf(item.get("性别")))){
                        lecturerEntity.setSex("1");
                    }
                }
                if(StringUtils.isNotBlank(String.valueOf(item.get("年龄"))) && !"null".equals(String.valueOf(item.get("年龄")))){
                    lecturerEntity.setAge(Integer.parseInt(String.valueOf(item.get("年龄"))));
                }
                if(StringUtils.isNotBlank(String.valueOf(item.get("备注"))) && !"null".equals(String.valueOf(item.get("备注")))){
                    lecturerEntity.setRemark(String.valueOf(item.get("备注")));
                }
                lecturerEntity.setDeleteFlag("0");
                lecturerEntity.setCreateDate(new Date());
                lecturerService.save(lecturerEntity);
                //保存到系统用户
                List<Long> roleList = new ArrayList<Long>();
                roleList.add(3l);
                SysUserEntity sysUserEntity = new SysUserEntity();
                sysUserEntity.setUserId(lecturerEntity.getUuid());
                sysUserEntity.setUsername(lecturerEntity.getSurname() + lecturerEntity.getLecturerName());
                sysUserEntity.setAccount(lecturerEntity.getAccount());
                sysUserEntity.setCode(UUID.randomUUID().toString());
                sysUserEntity.setPassword("123456");
                sysUserEntity.setEmail(lecturerEntity.getLecturerEmail());
                sysUserEntity.setMobile(lecturerEntity.getLecturerMobile());
                sysUserEntity.setRoleIdList(roleList);
                sysUserEntity.setStatus(0);
                sysUserEntity.setCreateUserId(1l);
                sysUserEntity.setRelateUserId("" + lecturerEntity.getUuid());
                sysUserEntity.setTenantId(getUser().getTenantId());
                sysUserEntity.setCreateTime(new Date());
                sysUserService.saveUser(sysUserEntity);
                //发送邮件
                MailVo mailVo = new MailVo();
                mailVo.setRecipient(lecturerEntity.getLecturerEmail());
                mailVo.setSubject("在线培训平台注册成功,请点击邮件里的链接激活后使用");
                mailVo.setContent("恭喜" + sysUserEntity.getUsername() + "注册成功，请点击" + activeUrl + sysUserEntity.getCode());
                MailUtil.sendEmail(mailSender,mailVo,javaMailSender);
            }
        }
        return R.ok("导入成功");
    }

    /**
     * 导入讲师预校验
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
            if(!"".equals(result)){
                item.put("序号","第"+i+"行");
                item.put("校验失败",result);
                list.add(item);
            }
            
        }
        return list;
    }

    /**
     * 导出讲师
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
        lecturerService.exportExcel(response, fileName);
    }
        
}
