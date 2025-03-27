package com.example.delicious_take_out.filter;

import com.alibaba.druid.support.json.JSONUtils;
import com.example.delicious_take_out.common.BaseContext;
import com.example.delicious_take_out.common.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * 检查用户是否已经完成登录的过滤器
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路径匹配器，匹配路径时还支持通配符**
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//    ServletRequest是HttpServletRequest的父接口，用实现类需要强转
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1、获取本次请求的url
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}",requestURI);
//        定义不需要处理的请求路径
        String[] urls =new String[]{
                "/employee/login",
                "/employee/logout",
                //页面中的图片等要放行
                "/backend/**",
                "/front/**",
                "/user/login"
        };

        //判断管理端登录状态，如果已经登陆，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用户已登录,用户id为：{}",request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request, response);
            return;
        }

        //判断用户端登录状态，如果已经登陆，则直接放行
        if(request.getSession().getAttribute("user") != null){
            log.info("用户已登录,用户id为：{}",request.getSession().getAttribute("user"));

            Integer userId =(Integer)request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }



        //判断本次请求是否需要处理，封装一个方法check
        boolean check = check(urls, requestURI);

        //如果不需要处理，直接放行
        if (check) {
            log.info("拦截到请求：{}",requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        //如果未登录则返回登录结果,通过输出流的方式向客户端页面响应数据
        // 前端已经写了拦截器，当判断为未登录状态，前端会自动跳转到login页面
        log.info("用户未登录");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(R.error("NOTLOGIN"));
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonResponse);
    }
/**
 * 路径匹配，检查本次请求是否需要放行
 * 检查请求的url是否和urls数组中的url有相同的
 * urls
 * requestURL
 */
    public boolean check(String[] urls, String requestURI) {
        for(String url : urls){
            boolean match = PATH_MATCHER.match(url, requestURI);
            if(match){
                return true;
            }
        }
        return false;
    }
}
