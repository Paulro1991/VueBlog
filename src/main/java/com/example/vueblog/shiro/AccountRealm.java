package com.example.vueblog.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.example.vueblog.Utils.JwtUtils;
import com.example.vueblog.entity.User;
import com.example.vueblog.service.IUserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

// AuthorizingRealm - A top-level abstract implementation of the Realm interface that only implements authentication support (log-in) operations and leaves authorization (access control) behavior to subclasses.
// JwtFilter里shiro调用executeLogin方法 -> AuthorizingRealm

@Component
public class AccountRealm extends AuthorizingRealm {

    private final JwtUtils jwtUtils;
    private final IUserService userService;

    public AccountRealm(JwtUtils jwtUtils, IUserService userService) {
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    // 根据用户角色分配相应的操作权限，此项目不需要
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    // 实现根据token从MySQL取得suerId -> 取得User -> 转换成AccountProfile
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        JwtToken jwtToken = (JwtToken) authenticationToken;
        String token = (String) jwtToken.getPrincipal();

        Long userId = jwtUtils.getUserIdByToken(token);
        User user = userService.getById(userId);
        if (user == null) {
            throw new UnknownAccountException("账户不存在");
        }
        if (user.getStatus() == -1) {
            throw new LockedAccountException("账户已被锁定");
        }

        AccountProfile accountProfile = new AccountProfile();
        BeanUtil.copyProperties(user, accountProfile);

        return new SimpleAuthenticationInfo(accountProfile, jwtToken.getCredentials(), getName());
    }
}
