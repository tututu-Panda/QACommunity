<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: pjy
  Date: 19-1-2
  Time: 下午11:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<%
    Map frontUser = (Map) session.getAttribute("frontUser");
%>

<div class="fly-header layui-bg-cyan">
    <div class="layui-container">
        <a class="fly-logo" href="<%=basePath %>">
           <%--<h1 style="color:#fff">问答系统社区</h1>--%>
            <img src="<%=basePath %>/static/images/logo.png" alt="layui">
        </a>
        <ul class="layui-nav fly-nav layui-hide-xs">
            <li class="layui-nav-item">
                <a href="<%=basePath %>/index.jsp"><i class="iconfont icon-shouye"></i>首页</a>
            </li>
            <li class="layui-nav-item  layui-this">
                <a href="<%=basePath %>/front/frontIndex_getQuestionIndex?page=1&orderType=1"><i class="iconfont icon-wenda"></i>问题</a>
            </li>
            <li class="layui-nav-item">
                <a href="<%=basePath %>/front/frontIndex_topicIndex"><i class="iconfont icon-iconmingxinganli"></i>话题</a>
            </li>

            <li class="layui-nav-item">
                <input type="text" id="serachValue"  class="layui-input " placeholder=" 搜索">
                <button id="search" class="layui-btn layui-btn-primary layui-btn-sm" style=" border:unset; right: 0;top: 5px;position: absolute;">
                    <i class="layui-icon" >&#xe615;</i>
                </button>
            </li>

        </ul>


        <ul class="layui-nav fly-nav-user">

            <% if(frontUser != null){ %>
            <!-- 登入的状态 -->
            <li class="layui-nav-item">
                <a class="fly-nav-avatar" href="javascript:;">
                    <cite class="layui-hide-xs"><%=frontUser.get("name")%></cite>
                    <img src="<%=basePath%>/<%=frontUser.get("photo")%>">
                </a>
                <dl class="layui-nav-child">
                    <dd><a href="<%=basePath %>/frontUser/FrontUser_userIndex.action"><i class="layui-icon">&#xe620;</i>用户中心</a></dd>
                    <dd><a href="<%=basePath %>/frontUser/FrontUser_userHome.action"><i class="layui-icon" style="margin-left: 2px; font-size: 22px;">&#xe68e;</i>我的主页</a></dd>
                    <%
                        int st = (int) frontUser.get("status");
                        if (st == 1){
                    %>
                        <dd><a href="<%=basePath %>/front/frontQuestion_addQuestion.action"><i class="layui-icon">&#xe609;</i>发布问题</a></dd>
                    <%
                        }
                    %>
                    <hr style="margin: 5px 0;">
                    <dd id="logout"><a style="text-align: center;">退出</a></dd>
                </dl>
            </li>
            <% }else {%>
            <!-- 未登入的状态 -->
            <li class="layui-nav-item">
                <a class="iconfont icon-touxiang layui-hide-xs" href="/front/user/login.html"></a>
            </li>
            <li class="layui-nav-item">
                <a href="<%=basePath %>/front/user/login.jsp" class="layui-btn layui-btn-sm login-btn" style="background: #5dade2; color:#fff;">登录</a>
            </li>
            <li class="layui-nav-item">
                <a href="<%=basePath %>/front/user/register.jsp" class="layui-btn  layui-btn-sm regist-btn" style="margin-left: 9px;background: #1abc9c;color:#fff;">注册</a>
            </li>
            <%--<li class="layui-nav-item layui-hide-xs">--%>
            <%--<a href="/app/qq/" onclick="layer.msg('正在通过QQ登入', {icon:16, shade: 0.1, time:0})" title="QQ登入" class="iconfont icon-qq"></a>--%>
            <%--</li>--%>
            <%--<li class="layui-nav-item layui-hide-xs">--%>
            <%--<a href="/app/weibo/" onclick="layer.msg('正在通过微博登入', {icon:16, shade: 0.1, time:0})" title="微博登入" class="iconfont icon-weibo"></a>--%>
            </li>
            <% }%>
        </ul>
    </div>
</div>
