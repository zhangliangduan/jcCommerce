package com.xunmeng.jccommerce.Logic;

import cn.hutool.core.lang.id.NanoId;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.jwt.JWTUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xunmeng.jccommerce.base.BusinessException;
import com.xunmeng.jccommerce.base.Result;
import com.xunmeng.jccommerce.constant.CacheConstant;
import com.xunmeng.jccommerce.constant.LoginConstant;
import com.xunmeng.jccommerce.domain.JcOrdersUser;
import com.xunmeng.jccommerce.dto.auth.LoginVo;
import com.xunmeng.jccommerce.dto.auth.RegisterQo;
import com.xunmeng.jccommerce.dto.auth.UserCacheInfo;
import com.xunmeng.jccommerce.enums.ErrorCodeEnum;
import com.xunmeng.jccommerce.service.IJcOrdersUserService;
import com.xunmeng.jccommerce.util.AuthUtil;
import com.xunmeng.jccommerce.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 登录逻辑
 *
 * @author ltm
 * @since 2024/7/4
 */

@Service
@Log4j2
@RequiredArgsConstructor
public class LoginLogic {

    private final IJcOrdersUserService jcOrdersUserService;

    private final RedisUtil redisUtil;


    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$";

    /**
     * 用户名密码登录
     *
     * @param userName 用户名
     * @param pwd      密码
     * @return 登录信息
     */
    public LoginVo uPassLogin(String userName, String pwd) {
        JcOrdersUser user = jcOrdersUserService.getByName(userName);
        if (Objects.isNull(user)) {
            throw new BusinessException(ErrorCodeEnum.PERMISSION_DENIED, "用户名或密码错误");
        }
        String salt = user.getSalt();
        pwd = pwd + salt;
        boolean checked = DigestUtil.bcryptCheck(pwd, user.getPwd());
        if (!checked) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "用户名或密码错误");
        }
        return this.getLoginVo(user);
    }

    /**
     * 获取登录返回信息
     *
     * @param user 用户
     * @return 登录信息
     */
    private LoginVo getLoginVo(JcOrdersUser user) {
        String userKey = CacheConstant.USER_INFO + user.getId();
        String userInfoStr = redisUtil.get(userKey);
        UserCacheInfo userCacheInfo;
        if (Objects.isNull(userInfoStr)) {
            userCacheInfo = new UserCacheInfo();
            userCacheInfo.setUserId(user.getId());
            userCacheInfo.setUserName(user.getName());
            userCacheInfo.setPhone(user.getPhone());
            String token = IdUtil.fastSimpleUUID();
            userCacheInfo.setTokenWEB(token);
            redisUtil.setEx(userKey, JSON.toJSONString(userCacheInfo), 1, TimeUnit.DAYS);
        } else {
            userCacheInfo = JSON.parseObject(userInfoStr, UserCacheInfo.class);
        }
        Map<String, Object> jwtMap = new HashMap<>(2);
        jwtMap.put("token", userCacheInfo.getTokenWEB());
        jwtMap.put("userId", user.getId());
        String jwtToken = JWTUtil.createToken(jwtMap, LoginConstant.JWT_KEY);
        LoginVo vo = new LoginVo();
        vo.setUserId(user.getId());
        vo.setName(user.getName());
        vo.setToken(jwtToken);
        return vo;
    }

    /**
     * 用户注册
     *
     * @param req 注册请求
     */
    public void register(RegisterQo req) {
        // 密码限制
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        if (!pattern.matcher(req.getPwd()).matches()) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "密码最少8位且必须包含字母和数字");
        }
        String name = req.getName();
        JcOrdersUser user = jcOrdersUserService.getByName(name);
        if (Objects.nonNull(user)) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "用户名已存在");
        }
        JcOrdersUser xdSave = new JcOrdersUser();
        String salt = NanoId.randomNanoId(8);
        xdSave.setName(name);
        xdSave.setSalt(salt);
        xdSave.setPwd(DigestUtil.bcrypt(req.getPwd() + salt));
        boolean saved = jcOrdersUserService.save(xdSave);
        if (!saved) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "注册失败");
        }
    }

    /**
     * 修改密码
     *
     * @param password 新密码
     * @return 修改结果
     */
    public Result<String> changePwd(String password) {
        String userName = AuthUtil.getCurrentUserInfo().getUserName();
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        if (!pattern.matcher(password).matches()) {
           return Result.newFailedResponse(ErrorCodeEnum.FAIL, "密码最少8位");
        }
        String salt = NanoId.randomNanoId(8);
        LambdaUpdateWrapper<JcOrdersUser> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(JcOrdersUser::getName, userName)
                .set(JcOrdersUser::getSalt, salt)
                .set(JcOrdersUser::getPwd,(DigestUtil.bcrypt(password + salt)));
        boolean update = jcOrdersUserService.update(wrapper);
        if (!update) {
            throw new BusinessException(ErrorCodeEnum.FAIL, "修改密码失败或与原密码相同");
        }
        return Result.newSuccessResponse("修改密码成功");
    }
}
