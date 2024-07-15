package com.xunmeng.jccommerce.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.xunmeng.jccommerce.Logic.LoginLogic;
import com.xunmeng.jccommerce.annotation.AuthToken;
import com.xunmeng.jccommerce.base.Result;
import com.xunmeng.jccommerce.dto.auth.ChangePwdQo;
import com.xunmeng.jccommerce.dto.auth.LoginVo;
import com.xunmeng.jccommerce.dto.auth.RegisterQo;
import com.xunmeng.jccommerce.dto.auth.UserNameLoginQo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录接口
 *
 * @author ltm
 * @since 2024/7/4
 */

@Log4j2
@RestController
@RequiredArgsConstructor
@Api(tags = "01-登录相关接口", value = "登录相关接口")
@RequestMapping("/login")
public class LoginController {

    private final LoginLogic loginLogic;

    @ApiOperation(value = "用户名密码登录")
    @ApiOperationSupport(order = 1)
    @PostMapping("/loginByUserName")
    public Result<LoginVo> loginByUserName(@RequestBody @Validated UserNameLoginQo req) {
        LoginVo vo = loginLogic.uPassLogin(req.getUserName(), req.getPwd());
        return Result.newSuccessResponse(vo);
    }

    @ApiOperation(value = "用户注册")
    @ApiOperationSupport(order = 2)
    @PostMapping("/register")
    public Result<String> register(@RequestBody @Validated RegisterQo req) {
        loginLogic.register(req);
        return Result.newSuccessResponse("注册成功");
    }

    @AuthToken
    @ApiOperation(value = "修改密码")
    @ApiOperationSupport(order = 3)
    @PostMapping("/change")
    public Result<String> changePwd(@RequestBody @Validated ChangePwdQo req) {
        return loginLogic.changePwd(req.getPwd());

    }
}
