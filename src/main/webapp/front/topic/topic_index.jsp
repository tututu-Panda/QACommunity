<%@ page import="java.util.Map" %><%--
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
%>
<%--<s:iterator value="quesList.quesLists" var="qq">--%>
<%--<s:property value="#qq.quesTitle"></s:property>--%>
<%--<br>--%>
<%--</s:iterator>--%>
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
    <link rel="stylesheet" href="<%=basePath %>/static/css/topic_index.css">
</head>
<body>
<%--header部分--%>
<s:include value="../commom/comHeader.jsp"/>
<%--header结束--%>

<div class="layui-container">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md8">

            <div class="fly-panel" style="margin-bottom: 0;">

                <div class="fly-panel-title fly-filter">
                    <i class="iconfont icon-iconmingxinganli"></i>&nbsp;话题


                </div>


                <%--话题循环开始--%>
                <div class="ui cards" style="margin:10px 20px 10px 20px;    ">
                    <s:iterator value="topicList.topicLists" var="tt">
                        <div class="card">
                            <div class="content">
                                <div class="header">
                                    <a href="<%=basePath %>/front/frontIndex_getQuestionIndex?page=1&orderType=1&to_id=<s:property value="#tt.toId"/>" class="layui-btn layui-btn-xs topic-style">
                                        <s:property value="#tt.topicName"></s:property>
                                    </a>
                                </div>
                                <%--<div class="meta">好友</div>--%>
                                <div class="description"><s:property value="#tt.remarks"></s:property> </div>
                            </div>
                            <div class="extra content">
                                <span class="right floated"><s:date name="#tt.createDate" format="yyyy年MM月dd日"></s:date>&nbsp;添加</span>
                            </div>
                        </div>
                    </s:iterator>
                </div>

            </div>

            <%--问题循环结束--%>
        </div>

        <%--以下做demo数据--%>
        <s:include value="../commom/comRight.jsp"/>
    </div>
</div>

<s:include value="../commom/comFooter.jsp"/>
<script>
    layui.use(['laydate','table','laytpl','layer','element'], function() {
        var table = layui.table;
        var laydate = layui.laydate;
        var laytpl = layui.laytpl;
        var layer = layui.layer;
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块

        $("#logout").on("click",function(){
            layer.confirm('确认退出？', {
                btn: ['确认', '取消'] //按钮
            }, function () {
                $.ajax({
                    url: "<%=path%>/frontUser/FrontUser_userLogout.action",
                    type: "POST",
                    success: function (data2) {
                        if (data2 === "1") {
                            layer.msg("退出成功!", {time: 1000, icon: 1}, function () {location.reload();});
                        } else {
                            layer.msg("退出失败!", {time: 1000, icon: 2},function () {location.reload();});
                        }
                    },
                    error: function () {
                        layer.msg('请求服务器超时', {time: 1000, icon: 2},function () {location.reload();});
                    }
                });
            });
        });
    });
</script>

<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_30088308'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "w.cnzz.com/c.php%3Fid%3D30088308' type='text/javascript'%3E%3C/script%3E"));</script>

</body>
</html>
