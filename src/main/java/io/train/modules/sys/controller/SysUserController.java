package io.train.modules.sys.controller;

import io.train.common.annotation.SysLog;
import io.train.common.utils.Constant;
import io.train.common.utils.PageUtils;
import io.train.common.utils.R;
import io.train.common.validator.Assert;
import io.train.common.validator.ValidatorUtils;
import io.train.common.validator.group.AddGroup;
import io.train.common.validator.group.UpdateGroup;
import io.train.modules.sys.dto.ChangeUserStatusDto;
import io.train.modules.sys.dto.UserInfoDto;
import io.train.modules.sys.entity.SysUserEntity;
import io.train.modules.sys.form.PasswordForm;
import io.train.modules.sys.service.SysUserRoleService;
import io.train.modules.sys.service.SysUserService;
import io.train.modules.sys.vo.SysUserVO;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;


	/**
	 * 所有用户列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		if(getUserId() != Constant.SUPER_ADMIN){
			params.put("createUserId", getUserId());
		}
		PageUtils page = sysUserService.queryPage(params);

		return R.ok().put("page", page);
	}

	/*
	 * 根据用户名模糊查询用户
	 * @author: liyajie
	 * @date: 2021/7/18 21:12
	 * @param userName
	 * @exception:
	 * @update:
	 * @updatePerson:
	 */
	@GetMapping("/getUserLikeName")
	public R getUserLikeName(@RequestParam Map<String, Object> params){
		String userName = String.valueOf(params.get("userName"));
		List<SysUserEntity> sysUserEntityList = sysUserService.getUserLikeName(userName);
		List<SysUserVO> sysUserVOList = new ArrayList<SysUserVO>();
		sysUserEntityList.stream().forEach((SysUserEntity sysUserEntity)->{
			SysUserVO sysUserVO = new SysUserVO();
			sysUserVO.setId(sysUserEntity.getUserId());
			sysUserVO.setValue(sysUserEntity.getUsername());
			sysUserVOList.add(sysUserVO);
		});
		return R.ok().put("data", sysUserVOList);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@GetMapping("/info")
	public R info(){
		return R.ok().put("user", getUser());
	}
	
	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@PostMapping("/password")
	public R password(@RequestBody PasswordForm form){
		Assert.isBlank(form.getNewPassword(), "新密码不为能空");
		
		//sha256加密
		String password = new Sha256Hash(form.getPassword(), getUser().getSalt()).toHex();
		//sha256加密
		String newPassword = new Sha256Hash(form.getNewPassword(), getUser().getSalt()).toHex();
				
		//更新密码
		boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(!flag){
			return R.error("原密码不正确");
		}
		
		return R.ok();
	}
	
	/**
	 * 用户信息
	 */
	@GetMapping("/info/{userId}")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.getById(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@PostMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);
		
		user.setCreateUserId(getUserId());
		sysUserService.saveUser(user);
		
		return R.ok();
	}
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@PostMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		user.setCreateUserId(getUserId());
		sysUserService.update(user);
		
		return R.ok();
	}

	/**
	 * 修改用户状态
	 */
	@SysLog("修改用户状态")
	@PostMapping("/changeUserStatus")
	public R changeUserStatus(@RequestBody ChangeUserStatusDto changeUserStatusDto) {
		sysUserService.changeUserStatus(changeUserStatusDto);
		return R.ok();
	}

	/**
	 * 修改用户个人信息
	 */
	@SysLog("修改用户个人信息")
	@PostMapping("/changeUserInfo")
	public R changeUserInfo(@RequestBody UserInfoDto userInfoDto) {
		sysUserService.changeUserInfo(userInfoDto);
		return R.ok();
	}
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@PostMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}
		
		sysUserService.deleteBatch(userIds);
		
		return R.ok();
	}
	
	
}
