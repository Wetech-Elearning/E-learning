package io.train.modules.sys.controller;

import io.train.common.utils.R;
import io.train.config.MyContext;
import io.train.modules.business.company.entity.CompanyEntity;
import io.train.modules.business.company.service.CompanyService;
import io.train.modules.business.student.entity.StudentEntity;
import io.train.modules.business.student.service.StudentService;
import io.train.modules.mail.util.MailUtil;
import io.train.modules.mail.vo.MailVo;
import io.train.modules.sys.entity.SysUserEntity;
import io.train.modules.sys.form.RegisterForm;
import io.train.modules.sys.form.SendMailForm;
import io.train.modules.sys.form.SysLoginForm;
import io.train.modules.sys.service.SysCaptchaService;
import io.train.modules.sys.service.SysUserRoleService;
import io.train.modules.sys.service.SysUserService;
import io.train.modules.sys.service.SysUserTokenService;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登录相关
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
public class SysLoginController extends AbstractController {
	@Value("${spring.mail.username}")
	private String mailSender; //邮件发送者

	@Resource
	private JavaMailSender javaMailSender;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private SysUserTokenService sysUserTokenService;

	@Autowired
	private SysCaptchaService sysCaptchaService;

	@Value("${user.active.url}")
	private String activeUrl;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Autowired
	private StudentService studentService;

	@Autowired
	private MyContext myContext;



	/**
	 * 验证码
	 */
	@GetMapping("captcha.jpg")
	public void captcha(HttpServletResponse response, String uuid)throws IOException {
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		//获取图片验证码
		BufferedImage image = sysCaptchaService.getCaptcha(uuid);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		IOUtils.closeQuietly(out);
	}

	/**
	 * 登录
	 */
	@PostMapping("/sys/login")
	public Map<String, Object> login(@RequestBody SysLoginForm form)throws IOException {
		boolean captcha = sysCaptchaService.validate(form.getUuid(), form.getCaptcha());
		if(!captcha){
			return R.error("验证码不正确");
		}

		//用户信息
		SysUserEntity user = sysUserService.queryByAccount(form.getAccount());

		//账号不存在、密码错误
		if(user == null || !user.getPassword().equals(new Sha256Hash(form.getPassword(), user.getSalt()).toHex())) {
			return R.error("账号或密码不正确");
		}

		//判断用户所属租户是否是禁用状态
		CompanyEntity companyEntity = companyService.getById(user.getTenantId());
		if((null == companyEntity || companyEntity.getStatus() == 0) && user.getTenantId() != 1){
			return R.error("该用户所属企业已被禁用，请联系企业管理员");
		}

		//账号锁定
		if(user.getStatus() == 0){
			return R.error("账号未激活,请登录注册邮箱激活");
		}

		//生成token，并保存到数据库
		R r = sysUserTokenService.createToken(user.getUserId()).put("tenantId",user.getTenantId());
		//查询用户的角色id
		List<Long> roles =sysUserRoleService.queryRoleIdList(user.getUserId());
		return r.put("roles",roles).put("userId", user.getUserId());
	}


	/**
	 * 退出
	 */
	@PostMapping("/sys/logout")
	public R logout() {
		sysUserTokenService.logout(getUserId());
		return R.ok();
	}

	/**
	 * 注册
	 * @author: liyajie
	 * @date: 2021/7/15 23:31
	 * @param registerForm
	 * @return java.util.Map<java.lang.String, java.lang.Object>
	 * @exception:
	 * @update:
	 * @updatePerson:
	 */
	@PostMapping("/sys/register")
	@Transactional(rollbackFor = {Exception.class, Error.class})
	public Map<String, Object> register(@RequestBody RegisterForm registerForm)throws IOException {
		boolean captcha = sysCaptchaService.validate(registerForm.getUuid(), registerForm.getCaptcha());
		if(!captcha){
			return R.error("验证码不正确");
		}
		CompanyEntity companyEntity = companyService.getByCompanyName(registerForm.getCompany());
		if(null == companyEntity){
			return R.error("企业["+registerForm.getCompany()+"]不存在，请重新填写");
		}
		//用户信息
		SysUserEntity user = sysUserService.queryByAccount(registerForm.getAccount());
		if(user != null){
			return R.error("账号["+registerForm.getAccount()+"]已存在，请重新填写");
		}else{
			//保存用户信息
			List<Long> roleList = new ArrayList<Long>();
			roleList.add(4l);
			user = new SysUserEntity();
			user.setCompany(registerForm.getCompany());
			user.setUsername(registerForm.getAccount());
			user.setPassword(registerForm.getPassword());
			user.setMobile(registerForm.getMobile());
			user.setEmail(registerForm.getEmail());
			user.setCode(registerForm.getUuid());
			user.setStatus(0);
			user.setCreateTime(new Date());
			user.setCreateUserId(-1L);
			user.setTenantId(Long.parseLong(companyEntity.getUuid()));
			user.setRoleIdList(roleList);
			sysUserService.saveUser(user);
			// 设置当前的租户id
			myContext.setCurrentTenantId(Long.parseLong(companyEntity.getUuid()));
			//保存学员信息
			StudentEntity studentEntity = new StudentEntity();
			studentEntity.setUuid(user.getUserId());
			studentEntity.setSurname(registerForm.getSurname());
			studentEntity.setUserName(registerForm.getUsername());
			studentEntity.setAccount(registerForm.getAccount());
			studentEntity.setRelatedCompany(String.valueOf(registerForm.getTenantId()));
			studentEntity.setUserMobile(registerForm.getMobile());
			studentEntity.setUserEmail(registerForm.getEmail());
			studentEntity.setRelatedDepartment(registerForm.getDepartment());
			studentEntity.setRelatedOffice(registerForm.getOffice());
			studentEntity.setDeleteFlag("0");
			studentEntity.setCreateDate(new Date());
			studentEntity.setSex(registerForm.getSex());
			studentEntity.setRemark("注册");
			studentService.save(studentEntity);
			//发送邮件
			MailVo mailVo = new MailVo();
			mailVo.setRecipient(user.getEmail());
			mailVo.setSubject("在线培训平台注册成功");
			mailVo.setContent("恭喜" + user.getUsername() + "注册成功，请点击"+ activeUrl + user.getCode());
			//mailVo.setContent("恭喜" + user.getUsername() + "注册成功，请点击http://47.106.14.57:8080/sys/active/"+user.getCode());
			MailUtil.sendEmail(mailSender,mailVo,javaMailSender);
		}
		return R.ok();
	}

	/**
	 * 激活用户
	 * @author: liyajie
	 * @date: 2021/7/16 0:37
	 * @param
	 * @return R
	 * @exception:
	 * @update:
	 * @updatePerson:
	 */
	@GetMapping("/sys/active")
	public R active(HttpServletRequest request)throws IOException {
		String code = request.getParameter("code");
		SysUserEntity sysUserEntity = sysUserService.queryByCode(code);
		if(null != sysUserEntity){
			sysUserEntity.setStatus(1);
			sysUserService.activeUser(sysUserEntity);
		}else{
			return R.error("未发现用户，请注册");
		}
		return R.ok().put("data","激活成功");
	}

	/**
	 * 发送重置密码邮件
	 * @author: liyajie
	 * @date: 2021/11/1 1:17
	 * @param sendMailForm
	 * @return io.train.common.utils.R
	 * @exception:
	 * @update:
	 * @updatePerson:
	 **/
	@PostMapping("/sys/sendResetPassword")
	public R sendResetPassword(@RequestBody SendMailForm sendMailForm)throws IOException {
		String account = sendMailForm.getAccount();
		String email = sendMailForm.getMail();
		SysUserEntity sysUserEntity = sysUserService.queryByEmailOrUserName(email,account);
		if(null == sysUserEntity){
			return R.error("账户不存在或该邮箱未注册用户");
		}else {
			sysUserService.updatePassword(sysUserEntity.getUserId(),sysUserEntity.getPassword(),new Sha256Hash("123456", sysUserEntity.getSalt()).toHex());
		}
		//发送邮件
		MailVo mailVo = new MailVo();
		mailVo.setRecipient(email);
		mailVo.setSubject("在线培训平台重置密码");
		mailVo.setContent("尊敬的用户您好，您的在线培训平台密码已重置，请知晓");
		MailUtil.sendEmail(mailSender,mailVo,javaMailSender);
		return R.ok();
	}
}
