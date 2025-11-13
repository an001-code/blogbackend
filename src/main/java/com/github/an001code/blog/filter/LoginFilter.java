package com.github.an001code.blog.filter;

import com.alibaba.fastjson.JSONObject;
import com.github.an001code.blog.pojo.Result;
import com.github.an001code.blog.utils.JwtUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * 过滤器
 */

//@Slf4j
//@Component
//public class LoginFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        /*
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        HttpServletResponse resp = (HttpServletResponse) servletResponse;
//        //获取请求url
//        String url = req.getRequestURL().toString();
//        //判断url中是否包含url,如果有，说明是登录请求，放行。
//        if(url.contains("login")){
//            log.info("登录操作，放行");
//            filterChain.doFilter(servletRequest,servletResponse);
//            return;
//        }
//
//        //获取请求头中的jwt令牌
//        String jwt = req.getHeader("token");
//        //判断令牌是否存在，如果不存在，返回错误结果
//        if (!StringUtils.hasLength(jwt)) {
//            log.info("令牌不存在");
//            Result error = Result.error("NOT_LOGIN");
//            String notLogin = JSONObject.toJSONString(error);
//            resp.getWriter().write(notLogin);
//            return;
//        }
//
//        //解析token,如果解析错误，返回错误信息
//        try{
//            JwtUtils.parseJwt(jwt);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.info("解析令牌失败");
//            Result error = Result.error("NOT LOGIN");
//            String notLogin = JSONObject.toJSONString(error);
//            resp.getWriter().write(notLogin);
//            return;
//        }
//
//        //放行
//        log.info("令牌合法，放行");
//        filterChain.doFilter(servletRequest,servletResponse);
//
//         */
//    }
//
//
//
//}
