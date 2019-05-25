<%@ page import="java.util.Map" %>
<%@ page import="com.qa.entity.QaBackUser" %>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

    QaBackUser backUser = (QaBackUser ) session.getAttribute("qaBackUser");
    int role =  backUser.getRole();

%>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="<%=basePath %>/static/plugins/layui/css/layui.css">
    <link rel="stylesheet" href="<%=basePath %>static/css/global.css">
    <link rel="stylesheet" href="<%=basePath %>static/css/dashboard.css">
    <link rel="stylesheet" href="<%=basePath %>static/css/font.css" media="all" />

</head>

<body>
<div class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-header">快捷入口</div>
        <div class="layui-card-body">
            <div class="layui-row" id="shortcutEntry">

                <div class="layui-col-xs6 layui-col-sm4 layui-col-md2 layui-col-lg2">
                    <a href="<%=basePath%>/admin/qaBackQues_checkContent.action" title="问题审核" class="featured-app"
                       data-icon="&#xe63f;">
                        <div class="featured-app-logo-wrapper">
                            <i class="layui-icon featured-app-logo">&#xe63f;</i>
                        </div>
                        <div class="featured-app-name">问题审核</div>
                    </a>
                </div>

                <%if(role == 0){%>
                <div class="layui-col-xs6 layui-col-sm4 layui-col-md2 layui-col-lg2">
                    <a href="<%=basePath %>/admin/qaBackTopic_FindAllTopic.action" title="话题管理" class="featured-app"
                       data-icon="&#xe638;">
                        <div class="featured-app-logo-wrapper">
                            <i class="layui-icon featured-app-logo">&#xe638;</i>
                        </div>
                        <div class="featured-app-name">话题管理</div>
                    </a>
                </div>
                <%}%>

                <div class="layui-col-xs6 layui-col-sm4 layui-col-md2 layui-col-lg2">
                    <a href="<%=basePath%>/admin/qaCommunity_communityList.action" title="用户管理" class="featured-app"
                       data-icon="&#xe612;">
                        <div class="featured-app-logo-wrapper">
                            <i class="layui-icon featured-app-logo">&#xe612;</i>
                        </div>
                        <div class="featured-app-name">用户管理</div>
                    </a>
                </div>

                <%if(role == 0){%>
                <div class="layui-col-xs6 layui-col-sm4 layui-col-md2 layui-col-lg2">
                    <a href="<%=basePath%>/admin/qaLog_logList.action" title="日志查看" class="featured-app"
                       data-icon="&#xe60a;">
                        <div class="featured-app-logo-wrapper">
                            <i class="layui-icon featured-app-logo">&#xe60a;</i>
                        </div>
                        <div class="featured-app-name">日志查看</div>
                    </a>
                </div>
                <%}%>
                <div class="layui-col-xs6 layui-col-sm4 layui-col-md2 layui-col-lg2">
                    <a href="<%=basePath %>/admin/qaBackQues_allQuestionView.action" title="问题管理" class="featured-app"
                       data-icon="&#xe613;">
                        <div class="featured-app-logo-wrapper">
                            <i class="layui-icon layui-icon-form">&#xe63c;</i>
                        </div>
                        <div class="featured-app-name">问题管理</div>
                    </a>
                </div>

            </div>
        </div>

    </div>

    <div class="layui-card">
        <div class="layui-card-header">系统一栏</div>
        <div class="layui-card-body">
            <div class="panel_box row  layui-col-space10">
                <div class="panel col layui-col-md4">
                    <a href="javascript:;">
                        <div class="panel_icon" style="background-color:#FF5722;">
                            <i class="iconfont icon-dongtaifensishu" data-icon="icon-dongtaifensishu"></i>
                        </div>
                        <div class="panel_word ">
                            <span class="userAddAll"></span>
                            <cite>新增人数</cite>
                        </div>
                    </a>
                </div>
                <div class="panel col layui-col-md4">
                    <a href="javascript:;" data-url="">
                        <div class="panel_icon" style="background-color:#009688;">
                            <i class="layui-icon" data-icon="&#xe613;">&#xe613;</i>
                        </div>
                        <div class="panel_word">
                            <span class="userAll"></span>
                            <cite>用户总数</cite>
                        </div>
                    </a>
                </div>
                <div class="panel col layui-col-md4">
                    <a href="javascript:;" >
                        <div class="panel_icon" style="background-color:#F7B824;">
                            <i class="iconfont icon-wenben" data-icon="icon-wenben"></i>
                        </div>
                        <div class="panel_word waitNews">
                            <span class="checkArticle"></span>
                            <cite>待审核文章</cite>
                        </div>
                    </a>
                </div>
                <div class="panel col  layui-col-md4">
                    <a href="javascript:;">
                        <div class="panel_icon" style="background-color:#2F4056;">
                            <i class="iconfont icon-text" data-icon="icon-text"></i>
                        </div>
                        <div class="panel_word allNews ">
                            <span class="allArticle"></span>
                            <cite>文章总数</cite>
                        </div>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <div class="layui-card userInfo">
        <div class="layui-card-header">用户相关</div>
        <div class="layui-card-body">
                <div class="layui-row">

                    <%--排行榜--%>
                    <div class="layui-col-md6">
                        <div class="layui-col-md8">
                            <div class="layui-row grid-demo">
                                <div class="layui-col-md5">
                                    <span>排行榜</span>
                                </div>
                                <div class="layui-col-md12">
                                    <table class="layui-table" lay-skin="line">
                                        <colgroup>
                                            <col>
                                            <col width="110">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th>用户名称</th>
                                            <th>回帖数量</th>
                                        </tr>
                                        </thead>
                                        <tbody class="userList"></tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>

                        <%--最新文章--%>
                    <div class="layui-col-md6 title">
                        <div class="layui-col-md10">
                            <div class="layui-row grid-demo">
                                <div class="layui-col-md5">
                                    最新文章<i class="iconfont icon-new1"></i>
                                </div>
                                <div class="layui-col-md12">
                                    <table class="layui-table" lay-skin="line">
                                        <colgroup>
                                            <col>
                                            <col width="110">
                                        </colgroup>
                                        <thead>
                                        <tr>
                                            <th>标题</th>
                                            <th>创建时间</th>
                                        </tr>
                                        </thead>
                                        <tbody class="hot_news"></tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
        </div>
    </div>


</div>










</body>
<script src="<%=basePath %>/static/plugins/js/jquery-3.1.1.min.js" type="text/javascript"></script>
<script src="<%=basePath %>/static/plugins/layui/layui.js"></script>
<script type="text/javascript">
    layui.use(['jquery', 'element'], function() {
        var element = layui.element,
            layer =  layui.layer,
            $ = layui.jquery;


        InitData();


        function InitData() {
            // 获取一系列数据

            $.ajax({
                url: "<%=path%>/admin/qaDashBoard_getUserInfo.action",
                type: "GET",
                success: function (data) {
                    $(".userAddAll").html(data.latest);
                    $(".userAll").html(data.count);
                },
                error: function () {
                    layer.msg('请求服务器超时', {time: 1000, icon: 2});
                }
            });

            $.ajax({
                url: "<%=path%>/admin/qaDashBoard_getArticleInfo.action",
                type: "GET",
                success: function (data) {
                    $(".checkArticle").html(data.checkQues);
                    $(".allArticle  ").html(data.allQues);
                },
                error: function () {
                    layer.msg('请求服务器超时', {time: 1000, icon: 2});
                }
            });

            $.ajax({
                url: "<%=path%>/admin/qaDashBoard_getLatestArticle.action",
                type: "GET",
                success: function (data) {
                    var list = data.list;
                    var newsHtml = '';
                    for(var i=0;i<list.length;i++){
                        var time = new Date(list[i][1]['time']).toLocaleDateString();
//                        console.log(new Date(list[i][1]['time']).toLocaleDateString());
                        newsHtml += '<tr>'
                            +'<td align="left">'+list[i][0]+'</td>'
                            +'<td>'+time+'</td>'
                            +'</tr>';
                    }
                    $(".hot_news").html(newsHtml);
                },
                error: function () {
                    layer.msg('请求服务器超时', {time: 1000, icon: 2});
                }
            });

            $.ajax({
                url: "<%=path%>/front/frontIndex_weekRank.action",
                type: "POST",
                success: function (data2) {
                    if (data2 != null) {
                        var html='' ;
                        // 拼接html
                        $.each(data2, function(index, item) {
//                        console.log(item[0]);
                            console.log(item);
                            var name = item[1];
                            var times = item[3];
                            html += '<tr>'
                                +'<td align="left">'+name+'</td>'
                                +'<td>'+times+'</td>'
                                +'</tr>';
                        });
                        $(".userList").append(html);
                    } else {
                        layer.msg("周榜数据接口出错!", {time: 1000, icon: 2});
                    }
                },
                error: function () {
                    layer.msg('请求服务器超时', {time: 1000, icon: 2},function () {location.reload();});
                }
            });
        }

    });
</script>

</html>