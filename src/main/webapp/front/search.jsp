<%@ page import="java.util.Map" %>
<%@ page import="com.opensymphony.xwork2.util.ValueStack" %>
<%@ page import="org.apache.struts2.ServletActionContext" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: 3tu
  Date: 2017/12/28
  Time: 22:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<%
    Map frontUser = (Map) session.getAttribute("frontUser");
    ValueStack vs=(ValueStack)request.getAttribute("struts.valueStack");
    int to_id = 0;
    if(vs.findValue("to_id") != null) {
        to_id = (int) vs.findValue("to_id");
    }


%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>QA问答社区</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="description" content="QA问答社区">
    <link rel="stylesheet" href="<%=basePath %>/static/plugins/layui/css/layui.css">
    <link rel="stylesheet" href="<%=basePath %>/static/css/global.css">
    <link rel="stylesheet" href="<%=basePath %>/static/css/front_index.css">
</head>
<body>
<%--header部分--%>
<s:include value="commom/comHeader.jsp"/>
<%--header结束--%>

<div class="layui-container">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md8">

            <div class="fly-panel" style="margin-bottom: 0;">

                <div class="fly-panel-title fly-filter ">
                    <i class="layui-icon">&#xe68e;</i>&nbsp;搜索：
                    <a href="<%=basePath %>/front/frontIndex_getQuestionIndex?page=1&orderType=1" >查看全部</a>

                    <span class="fly-filter-right layui-hide-xs">
                        <a href="javascript:;" class="" id="byNew">按最新</a>
                        <span class="fly-mid"></span>
                        <a href="javascript:;" id="byHot" class="">按热议</a>
                    </span>
                </div>
                <%--问题循环开始--%>
                <ul class="fly-list">
                    <%--问题循环--%>
                    <s:iterator value="quesList.quesLists" var="qq">
                        <li>
                            <a href="<%=basePath %>/frontUser/FrontUser_userHome.action?id=<s:property value="#qq.id"></s:property>" class="fly-avatar">
                                <img src="<%=basePath %>/<s:property value="#qq.headPhoto" />" alt="">
                            </a>
                            <h2>
                                <a class="layui-badge" href='<%=basePath %>/front/frontIndex_getQuestionIndex?page=1&orderType=1&to_id=<s:property value="#qq.toId"/>'><s:property value="#qq.topicName"></s:property></a>
                                <a href="javascript:;" class="quesDetail" data-id="<s:property value="#qq.quesId"></s:property>"><s:property value="#qq.quesTitle"></s:property></a>
                            </h2>
                            <div class="fly-list-info">
                                <a href="<%=basePath %>/frontUser/FrontUser_userHome.action?id=<s:property value="#qq.id"></s:property> " link>
                                    <cite><s:property value="#qq.accountName"></s:property></cite>
                                </a>
                                <span><s:date name="#qq.createDate" format="yyyy年MM月dd日 hh:mm:ss"></s:date></span>

                                <span class="fly-list-nums">
                                      <i class="iconfont" title="浏览">&#xe60b;</i><s:property value="#qq.browseCount"></s:property>
                                      <i class="iconfont icon-pinglun1" title="回复"></i><s:property value="#qq.commentCount"></s:property>
                                  </span>
                            </div>
                            <div class="fly-list-badge">
                                <!--<span class="layui-badge layui-bg-red">精帖</span>-->
                            </div>
                        </li>
                    </s:iterator>

                </ul>
                <div style="text-align: center">
                    <div class="" id="pages">

                    </div>
                </div>

            </div>

            <%--问题循环结束--%>
        </div>

        <%--右边公共部分--%>
        <s:include value="commom/comRight.jsp"/>
    </div>
</div>

<s:include value="commom/comFooter.jsp"/>
<script>
    layui.use(['laydate','laypage','laytpl','layer','element'], function() {
        var laypage = layui.laypage;
        var laydate = layui.laydate;
        var laytpl = layui.laytpl;
        var layer = layui.layer;
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块


        var orderCode = "<s:property value="quesList.orderType" />";   //定义排序代号

        var search = "<s:property value="quesList.search" />";   //定义搜索数据

        var topicId = "<%=to_id%>";

        if(orderCode == 1) {
            $("#byNew").addClass("layui-this");
        }else if(orderCode == 2) {
            $("#byHot").addClass("layui-this");
        }else {
            $("#byNew").addClass("layui-this");
        }

        $("#byNew").on('click', function() {
            orderCode = 1;
            window.location.href = "<%=basePath %>/front/frontIndex_getSearchQuestion?page=1&orderType="+orderCode+"&to_id="+topicId+"&search="+search;
        });
        $("#byHot").on('click', function() {
            orderCode = 2;
            window.location.href = "<%=basePath %>/front/frontIndex_getSearchQuestion?page=1&orderType="+orderCode+"&to_id="+topicId+"&search="+search;
        });
        var counts = "<s:property value="quesList.count" />";
        console.log(counts);
        var curr = "<s:property value="quesList.page" />";
        //执行一个laypage实例
        laypage.render({
            elem: 'pages' //注意，这里的是 ID，不用加 # 号
            ,count: counts //数据总数，从服务端得到
            ,limit: 9
            ,curr: curr
            ,group:6
            ,theme: '#1E9FFF'
            ,curr: curr
            ,jump: function(obj, first) {
                var curr = obj.curr;
                if(!first) {
                    window.location.href = "<%=basePath %>/front/frontIndex_getSearchQuestion?page="+curr+"&orderType="+orderCode+"&to_id="+topicId+"&search="+search;;
                }
            }
        });

        /**
         * 查看问题详情
         */
        $(".quesDetail").on('click', function() {
            var quesId = $(this).data("id");
            window.location.href = "<%=basePath %>/front/frontIndex_getTheQuestion?quesId="+quesId;
        });

    });
</script>


</body>
</html>
