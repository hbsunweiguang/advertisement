package com.ruoyi.advertisement.service.impl;

import com.ruoyi.advertisement.mapper.LoginMapper;
import com.ruoyi.advertisement.service.ILoginService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * <p>ClassName : LoginServiceImpl</p>
 * <p>Description : </p>
 *
 * @author 孙伟光
 * @version 1.0
 * @date 2025/7/29 14:40
 */
@Service
public class LoginServiceImpl implements ILoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private SysPermissionService permissionService;

    @Override
    public LoginUser selectUserByUserNameAndRoleKey(String userName, String password, String roleKey) {
        SysUser user = loginMapper.selectUserByUserNameAndRoleKey(userName, roleKey);
        if (StringUtils.isNull(user))
        {
            log.info("登录用户：{} 不存在.", userName);
            throw new ServiceException(MessageUtils.message("user.not.exists"));
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", userName);
            throw new ServiceException(MessageUtils.message("user.password.delete"));
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", userName);
            throw new ServiceException(MessageUtils.message("user.blocked"));
        }

        if (! SecurityUtils.matchesPassword(password, user.getPassword())) {
            throw new UserPasswordNotMatchException();
        }

        LoginUser loginUser = new LoginUser(user.getUserId(), user.getDeptId(), user, permissionService.getMenuPermission(user));

        sysLoginService.recordLoginInfo(loginUser.getUserId());

        loginUser.setToken(tokenService.createToken(loginUser));

        return loginUser;
    }
}
