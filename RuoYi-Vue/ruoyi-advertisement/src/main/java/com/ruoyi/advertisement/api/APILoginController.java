package com.ruoyi.advertisement.api;

import com.ruoyi.advertisement.domain.APILoginBody;
import com.ruoyi.advertisement.service.ILoginService;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>ClassName : APILoginController</p>
 * <p>Description : </p>
 *
 * @author 孙伟光
 * @version 1.0
 * @date 2025/7/29 14:14
 */
@RestController
@RequestMapping("/api")
public class APILoginController extends BaseController {

    @Autowired
    private ILoginService loginService;
    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody APILoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        LoginUser user = loginService.selectUserByUserNameAndRoleKey(loginBody.getUsername(), loginBody.getPassword(), loginBody.getRoleKey());
        user.getUser().setPassword("");
        ajax.put("data", user);
        return ajax;
    }
}
