package com.example.vueblog.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vueblog.Utils.JwtUtils;
import com.example.vueblog.common.dto.LoginDTO;
import com.example.vueblog.common.lang.Result;
import com.example.vueblog.entity.User;
import com.example.vueblog.service.IUserService;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class AccountController {

    public static final String TOKEN = "Authorization";

    private final IUserService userService;
    private final JwtUtils jwtUtils;

    public AccountController(IUserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDTO loginDTO, HttpServletResponse response) {

        // 验证用户名和密码
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDTO.getUsername()));
        Assert.notNull(user, "用户不存在");
        if (!user.getPassword().equals(SecureUtil.md5(loginDTO.getPassword()))) {
            return Result.fail("密码不正确");
        }

        // 登陆成功为其生成TOKEN
        String jwt = jwtUtils.generateToken(user.getId());
        response.setHeader(TOKEN, jwt);
        response.setHeader("Access-Control-Expose-Headers", TOKEN);

        return Result.succ(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .put("email", user.getEmail())
                .map()
        );
    }

    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }
}
