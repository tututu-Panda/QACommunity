package com.qa.filter;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * 前台用户的验证
 */
public class FrontUserFilter extends HttpServlet implements Filter {

    private String excludedPages;
    private String[] excludedPageArray;

    /**
     * 初始化函数，获取需要排除的URL
     * @param fConfig
     * @throws ServletException
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // 从web.xml配置文件中得到
        excludedPages = fConfig.getInitParameter("excludedPages");
        //获取配置中的路径信息
        if (StringUtils.isNotEmpty(excludedPages)) {
            excludedPageArray = excludedPages.split(",");
        }
        return;
    }


    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(true);
        boolean isExcludedPage = false;
        for (String page : excludedPageArray) {//判断是否在过滤url之外
            if((request).getServletPath().equals(page)){
                isExcludedPage = true;
                break;
            }
        }
        if(isExcludedPage) {        //在过滤的url之外
            chain.doFilter(request, response);
        }else {
            //不在url之外则进行判断session
            if(request.getSession().getAttribute("frontUser") == null) {
                //保存当前客户想要去的url地址
                String goURL = request.getServletPath();//获得用户想要去的地址
                String param = request.getQueryString(); //获得地址中携带的参数
                if(param != null) {
                    goURL = goURL + "?" + param; //重新拼好请求地址+参数
                }
                //把当前用户想要访问的地址，存储到session中
                request.getSession().setAttribute("goURL", goURL);

                //非法请求，跳转到登陆页面
                request.getSession().setAttribute("error", "非法请求，请登录！");
                response.sendRedirect(request.getContextPath() + "/front/user/login.jsp");

            }else {
                //如果有下一个过滤器则跳转，否则直接到目标页面
                chain.doFilter(request, response);
            }
        }

    }

}
