package com.example.vueblog.shiro;

import cn.hutool.json.JSONUtil;
import com.auth0.jwt.interfaces.Claim;
import com.example.vueblog.Utils.JwtUtils;
import com.example.vueblog.common.lang.Result;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class JwtFilter extends BasicHttpAuthenticationFilter {

    public static final String TOKEN = "Authorization";
    JwtUtils jwtUtils;

    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    // executeLogin(...)会调用createToken(...)来取得token
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader(TOKEN);
        if (ObjectUtils.isEmpty(jwt)) {
            return null;
        }
        return new JwtToken(jwt);
    }

    // Processes requests where the subject was denied access as determined by the isAccessAllowed method
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = WebUtils.toHttp(servletRequest);
        String jwt = request.getHeader(TOKEN);
        // 不带TOKEN的request直接跳转至对应的controller，访问不需要授权的页面
        if (ObjectUtils.isEmpty(jwt)) {
            return true;
        } else {
            // 校验TOKEN
            Map<String, Claim> claims = jwtUtils.getClaimsByToken(jwt);
            if (claims == null || jwtUtils.isTokenExpired(claims.get("exp").asDate())) {
                throw new ExpiredCredentialsException("token已失效，请重新登录");
            }
            // Shiro用Token执行登录
            return executeLogin(servletRequest, servletResponse);
        }
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        Result result = Result.fail(throwable.getMessage());
        String json = JSONUtil.toJsonStr(result);

        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ioException) {
        }
        return false;
    }

    // 跨域支持
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }

        return super.preHandle(request, response);
    }
}
