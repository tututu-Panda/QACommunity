package com.qa.filter;

import com.qa.recommend.algorithms.ContentRecommender;
import com.qa.recommend.reWrite.Keyword;
import com.qa.recommend.userLog.userLog;
import com.qa.recommend.userProfile.userProfile;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by 3tu on 2019/5/9.
 */

/**
 * 记录用户浏览记录
 */
public class  userLogFilter  extends HttpServlet implements Filter {

    private userLog userLog;
    private userProfile userProfile;
    private ContentRecommender contentRecommender;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession(true);




            //判断用户是否登录, 记录登录用户的浏览记录
            if (request.getSession().getAttribute("frontUser") != null) {
                        // 获取相关信息
                        final int quesId = Integer.valueOf(request.getParameter("quesId"));
                        Map user = (Map) session.getAttribute("frontUser");
                        final int userId = (int) user.get("id");
                        final ServletContext sc = session.getServletContext();



                // 执行异步操作,避免占用用户时间
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                Future<Void> future = executorService.submit(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {

                        // （非注解无法直接注入）从容器中获取component
                        XmlWebApplicationContext cxt = (XmlWebApplicationContext) WebApplicationContextUtils.getWebApplicationContext(sc);

                        if(cxt != null && cxt.getBean("userLog") != null && userLog == null)
                            userLog = (userLog) cxt.getBean("userLog");

                        if(cxt != null && cxt.getBean("userProfile") != null && userProfile == null)
                            userProfile = (userProfile)cxt.getBean("userProfile");

                        if(cxt != null && cxt.getBean("ContentRecommender") != null && contentRecommender == null)
                            contentRecommender = (ContentRecommender)cxt.getBean("ContentRecommender");

                        // 加入Redis缓存
                        userLog.addUserLog(userId, quesId);


                        // 更新用户画像
                        Map<Integer, ArrayList<com.qa.recommend.reWrite.Keyword>> words = userProfile.refreshUserProfile(userId);

                        // 生成用户画像
                        Map<Integer, ArrayList<Keyword>> profile = userProfile.productProfile(words);


                        // 将画像加入redis
                       userProfile.addProfile(userId, profile);

                        contentRecommender.recommend(userId);

                        return null;
                    }
                });



            }



        chain.doFilter(request, response);
    }


}
