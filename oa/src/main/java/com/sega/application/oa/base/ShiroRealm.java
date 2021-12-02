package com.sega.application.oa.base;

import com.alibaba.fastjson.JSONObject;
import com.sega.application.oa.constant.JwtConstant;
import com.sega.application.oa.constant.RedisConstant;
import com.sega.application.oa.entity.system.RoleModelEntity;
import com.sega.application.oa.entity.system.UserEntity;
import com.sega.application.oa.service.system.IRoleModelService;
import com.sega.application.oa.service.system.impl.UserServiceImpl;
import com.sega.application.oa.utils.JwtUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;

import java.util.List;

public class ShiroRealm extends AuthorizingRealm {

    private final static Logger logger = LoggerFactory.getLogger(ShiroRealm.class);
    /**
     * 延迟加载bean,解决缓存Cache不能正常使用;事务Transaction注解不能正常运行
     */
    @Autowired
    @Lazy
    private UserServiceImpl userService;

    @Autowired
    private IRoleModelService roleModelBiz;

    @Autowired
    private RedisClient redis;

    @Value("${refreshTokenExpireTime}")
    protected String refreshTokenExpireTime;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 认证.登录
     * doGetAuthenticationInfo这个方法是在用户登录的时候调用的
     * 也就是执行SecurityUtils.getSubject().login()的时候调用；(即:登录验证)
     * 验证通过后会用户保存在缓存中的
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken paramAuthenticationToken) throws AuthenticationException {
        logger.info("##################执行Shiro登录认证##################");
        //获取用户输入的token
        String token = (String) paramAuthenticationToken.getCredentials();

        String loginName = JwtUtil.getClaim(token, JwtConstant.ACCOUNT);
        //查询数据库
        UserEntity user = userService.getByAccount(loginName);

        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (redis.hasKey(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + loginName)) {
            // 获取RefreshToken的时间戳
            String currentTimeMillisRedis = redis.get(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN + loginName).toString();
            // 获取AccessToken时间戳，与RefreshToken的时间戳对比
            redis.set(RedisConstant.PREFIX_SHIRO_REFRESH_TOKEN +  user.getUserLoginName(), currentTimeMillisRedis, Integer.parseInt(refreshTokenExpireTime));
            redis.set(token, JSONObject.toJSONString(user) , Integer.parseInt(refreshTokenExpireTime));
            return new SimpleAuthenticationInfo(token, token, this.getClass().getName());
        }
        throw new AuthenticationException("Token已过期(Token expired or incorrect.)");
    }

    /**
     * 授权
     * doGetAuthorizationInfo方法是在我们调用
     * SecurityUtils.getSubject().isPermitted（）这个方法，
     * 授权后用户角色及权限会保存在缓存中的
     *
     * @param principal
     * @return
     * @RequiresPermissions这个注解起始就是在执行SecurityUtils.getSubject().isPermitted（）
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String loginName = JwtUtil.getClaim(principal.toString(), JwtConstant.ACCOUNT);
        System.out.println(loginName);
        UserEntity userEntity = userService.getByAccount(loginName);
        List<RoleModelEntity> rmEntityList = roleModelBiz.query(new RoleModelEntity(userEntity.getUserRoleId()));
        for (RoleModelEntity rmEntity : rmEntityList) {
            info.addStringPermission(rmEntity.getModel().getModelCode());
        }
        return info;
    }
}
