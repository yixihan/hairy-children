package com.wq.config.shiro.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wq.common.pojo.Result;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt 过滤器
 * @author : yixihan
 * @create : 2022-02-05-12:16
 */
public class JwtFilter extends BasicHttpAuthenticationFilter {
    /**
     * 向后端发送请求时进行 JwtToken 的校验
     * @return true : JwtToken 正确 | false : JwtToken 错误
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        // 将 ServletRequest 转换为 HttpServletRequest
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // 在请求头中获取 token
        // 前端命名为 JwtToken
        String token = httpServletRequest.getHeader("Jwt-Token");

        // token 不存在
        if (StringUtils.isEmpty(token)) {
            out(response, Result.fail("无 token, 无权访问, 请先登录"));
            return false;
        }

        // token 存在
        JwtToken jwtToken = new JwtToken(token);

        try {
            SecurityUtils.getSubject().login(jwtToken);
            return true;
        } catch (ShiroException e) {
            // 其他情况抛出的异常统一处理，由于先前是登录进去的了，所以都可以看成是 token 被伪造造成的
            out(response,Result.fail(" 无效 token ~"));
            e.printStackTrace();
            return false;
        }
    }


    /**
     * json 形式返回结果 token 验证失败信息，无需转发
     */
    private void out(ServletResponse response, Result rs) throws IOException {
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        ObjectMapper mapper = new ObjectMapper();
        String jsonRes = mapper.writeValueAsString(rs);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        httpServletResponse.getOutputStream().write(jsonRes.getBytes());
    }


    /**
     * 过滤器拦截请求的入口方法
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        try {
            return executeLogin(request,response);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个 OPTIONS 请求,这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
