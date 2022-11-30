package io.train.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.train.common.utils.PageUtils;
import io.train.modules.sys.dto.ChangeUserStatusDto;
import io.train.modules.sys.dto.UserInfoDto;
import io.train.modules.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;


/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
public interface SysUserService extends IService<SysUserEntity> {

	PageUtils queryPage(Map<String, Object> params);

	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	List<String> queryAllPerms(Long userId);

	/**
	 * 查询用户的所有菜单ID
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 */
	SysUserEntity queryByUserName(String username);

	/**
	 * 根据帐户，查询系统用户
	 */
	SysUserEntity queryByAccount(String account);

	/**
	 * 根据用户名模糊查询用户
	 * @author: liyajie
	 * @date: 2021/7/18 21:13
	 * @param username
	 * @return java.util.List<SysUserEntity>
	 * @exception:
	 * @update:
	 * @updatePerson:
	 */
	List<SysUserEntity> getUserLikeName(String username);

	/**
	 * 根据code，查询系统用户
	 */
	SysUserEntity queryByCode(String code);

	/**
	 * 根据email和用户名，查询系统用户
	 */
	SysUserEntity queryByEmailOrUserName(String email,String userName);

	/**
	 * 保存用户
	 */
	void saveUser(SysUserEntity user);

	/**
	 * 激活用户
	 */
	void activeUser(SysUserEntity user);

	/**
	 * 修改用户
	 */
	void update(SysUserEntity user);

	/**
	 * 删除用户
	 */
	void deleteBatch(Long[] userIds);

	/**
	 * 修改密码
	 * @param userId       用户ID
	 * @param password     原密码
	 * @param newPassword  新密码
	 */
	boolean updatePassword(Long userId, String password, String newPassword);

	/**
	 * 修改用户状态
	 */
	void changeUserStatus(ChangeUserStatusDto changeUserStatusDto);

	/**
	 * 修改用户个人信息
	 */
	void changeUserInfo(UserInfoDto userInfoDto);
}
