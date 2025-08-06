package com.ruoyi.advertisement.service;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>InterfaceName : ILoginService</p>
 * <p>Description : </p>
 *
 * @author 孙伟光
 * @version 1.0
 * @date 2025/7/29 14:39
 */
public interface ILoginService {

    /**
     * 通过用户名查询用户
     * @param userName 用户名
     * @param password 角色
     * @param roleKey 角色
     * @return 用户对象token
     */
    public LoginUser selectUserByUserNameAndRoleKey(String userName, String password, String roleKey);
}
