package io.train.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.train.common.exception.RRException;
import io.train.common.utils.Constant;
import io.train.common.utils.PageUtils;
import io.train.common.utils.Query;
import io.train.modules.sys.dao.SysUserDao;
import io.train.modules.sys.dto.ChangeUserStatusDto;
import io.train.modules.sys.dto.UserInfoDto;
import io.train.modules.sys.entity.SysUserEntity;
import io.train.modules.sys.service.SysRoleService;
import io.train.modules.sys.service.SysUserRoleService;
import io.train.modules.sys.service.SysUserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {
	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String username = (String)params.get("username");
		Long createUserId = (Long)params.get("createUserId");

		IPage<SysUserEntity> page = this.page(
			new Query<SysUserEntity>().getPage(params),
			new QueryWrapper<SysUserEntity>()
				.like(StringUtils.isNotBlank(username),"username", username)
				.eq(createUserId != null,"create_user_id", createUserId)
		);

		return new PageUtils(page);
	}

	@Override
	public List<String> queryAllPerms(Long userId) {
		return baseMapper.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return baseMapper.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return baseMapper.queryByUserName(username);
	}

	@Override
	public SysUserEntity queryByAccount(String account) {
		return baseMapper.queryByAccount(account);
	}

	@Override
	public List<SysUserEntity> getUserLikeName(String username) {
		QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<SysUserEntity>();
		queryWrapper.select("user_id","username").like("username",username);
		return baseMapper.selectList(queryWrapper);
	}

	@Override
	public SysUserEntity queryByCode(String code) {
		QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<SysUserEntity>()
				.eq(code != null,"code", code);
		return baseMapper.selectOne(queryWrapper);
	}

	@Override
	public SysUserEntity queryByEmailOrUserName(String email,String userName) {
		QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<SysUserEntity>()
				.eq(userName != null,"username", userName)
				.eq(email != null,"email", email);
		return baseMapper.selectOne(queryWrapper);
	}

	@Override
	@Transactional
	public void saveUser(SysUserEntity user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		this.save(user);

		//检查角色是否越权
		checkRole(user);

		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	public void activeUser(SysUserEntity user) {
		baseMapper.updateById(user);
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		this.updateById(user);

		//检查角色是否越权
		checkRole(user);

		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	public void deleteBatch(Long[] userId) {
		this.removeByIds(Arrays.asList(userId));
	}

	@Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
		SysUserEntity userEntity = new SysUserEntity();
		userEntity.setPassword(newPassword);
		return this.update(userEntity,
				new QueryWrapper<SysUserEntity>().eq("user_id", userId).eq("password", password));
	}

	/**
	 * 检查角色是否越权
	 */
	private void checkRole(SysUserEntity user){
		if(user.getRoleIdList() == null || user.getRoleIdList().size() == 0){
			return;
		}
		//如果不是超级管理员，则需要判断用户的角色是否自己创建
		/*if(user.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}*/

		//查询用户创建的角色列表
		//List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

		//判断是否越权
		/*if(!roleIdList.containsAll(user.getRoleIdList())){
			throw new RRException("新增用户所选角色，不是本人创建");
		}*/
	}

	@Override
	public void changeUserStatus(ChangeUserStatusDto changeUserStatusDto) {
		UpdateWrapper<SysUserEntity> updateWrapper = new UpdateWrapper<SysUserEntity>();
		updateWrapper.set("status", changeUserStatusDto.getStatus());
		updateWrapper.eq("username", changeUserStatusDto.getUsername());
		this.baseMapper.update(null, updateWrapper);
	}

	@Override
	public void changeUserInfo(UserInfoDto userInfoDto) {
		UpdateWrapper<SysUserEntity> updateWrapper = new UpdateWrapper<SysUserEntity>();
		updateWrapper.set("email", userInfoDto.getEmail());
		updateWrapper.set("mobile", userInfoDto.getMobile());
		updateWrapper.set("avatar", userInfoDto.getAvatar());
		updateWrapper.set("introduction", userInfoDto.getIntroduction());
		updateWrapper.eq("username", userInfoDto.getUsername());
		this.baseMapper.update(null, updateWrapper);
	}
}
