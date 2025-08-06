package com.ruoyi.advertisement.mapper;

import com.ruoyi.common.core.domain.entity.SysUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>InterfaceName : LoginMapper</p>
 * <p>Description : 小程序登录</p>
 *
 * @author 孙伟光
 * @version 1.0
 * @date 2025/7/29 14:32
 */
public interface LoginMapper {

    /**
     * 通过用户名查询用户
     * @param userName 用户名
     * @param roleKey 角色
     * @return 用户对象信息
     */
    public SysUser selectUserByUserNameAndRoleKey(@Param("userName") String userName,@Param("roleKey")  String roleKey);
}
