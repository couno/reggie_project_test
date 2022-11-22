package com.couno.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.couno.reggie.common.BaseContext;
import com.couno.reggie.common.Rmg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: LoginCheckFilter
 * date: 2022/11/10 18:31
 *
 * @author Couno
 * ProjectName: reggie
 */

@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;


        // 1.获取本次请求的URI
        String requestURI = request.getRequestURI();

        // 2.判断本次请求是否需要处理(定义不需要处理的路径)
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/login",
                "/user/sendMsg"
        };

        boolean check = check(urls, requestURI);

        // 3.如果不需要处理，则直接放行
        if (check){
            log.info("本次请求不需要处理:{}",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

        // 4.判断登录状态，如果已登录，则直接放行
        if (request.getSession().getAttribute("employee") != null){
            log.info("后端用户已经登录，用户的id为:{}",request.getSession().getAttribute("employee"));

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);

            filterChain.doFilter(request,response);
            return;
        }

        if (request.getSession().getAttribute("user") != null){
            log.info("移动端用户已经登录，用户的id为:{}",request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);

            filterChain.doFilter(request,response);
            return;
        }

        // 5.如果未登录则返回未登录结果
        log.info("用户未登录！{}",requestURI);
        response.getWriter().write(JSON.toJSONString(Rmg.error("NOTLOGIN")));
    }

    // 检查路径是否需要过滤
    public boolean check(String[] urls,String requestURI){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url,requestURI);
            if (match){
                return true;
            }
        }
        return false;
    };
}
