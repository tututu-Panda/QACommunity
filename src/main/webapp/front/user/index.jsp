<%@ page import="com.qa.entity.QaFrontUser" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<%--<%--%>
  <%--Map frontUser = (Map) session.getAttribute("frontUser");--%>
<%--%>--%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>用户中心</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <meta name="description" content="QA问答社区">
  <link rel="stylesheet" href="<%=basePath %>/static/plugins/layui/css/layui.css">
  <link rel="stylesheet" href="<%=basePath %>static/css/global.css">
  <link rel="stylesheet" href="<%=basePath %>static/css/front_index.css">
</head>
<body>

<s:include value="../commom/comHeader.jsp"/>

<div class="layui-container fly-marginTop fly-user-main">
  <ul class="layui-nav layui-nav-tree layui-inline" lay-filter="user">
    <li class="layui-nav-item">
      <a href="<%=basePath %>/frontUser/FrontUser_userHome.action">
        <i class="layui-icon">&#xe609;</i>
        我的主页
      </a>
    </li>
    <li class="layui-nav-item layui-this">
      <a href="<%=basePath %>/frontUser/FrontUser_userIndex.action">
        <i class="layui-icon">&#xe612;</i>
        用户中心
      </a>
    </li>
    <li class="layui-nav-item">
      <a href="<%=basePath %>/frontUser/FrontUser_userSet.action">
        <i class="layui-icon">&#xe620;</i>
        基本设置
      </a>
    </li>
  </ul>

  <div class="site-tree-mobile layui-hide">
    <i class="layui-icon">&#xe602;</i>
  </div>
  <div class="site-mobile-shade"></div>

  <div class="site-tree-mobile layui-hide">
    <i class="layui-icon">&#xe602;</i>
  </div>
  <div class="site-mobile-shade"></div>


  <div class="fly-panel fly-panel-user" pad20>

    <div class="layui-tab layui-tab-brief" lay-filter="user">

      <ul class="layui-tab-title" id="LAY_mine">
        <li data-type="mine-jie" lay-id="index" class="layui-this">我发的帖（<span><s:property value="count"/></span>）</li>
        <li data-type="mine-jie" lay-id="index" class="layui ">正在审核的帖（<span><s:property value="count2"/></span>）</li>
      </ul>

      <div class="layui-tab-content" style="padding: 20px 0;">

        <div class="layui-tab-item layui-show">
          <ul class="mine-view jie-row">
            <s:iterator value="list" id="question" status="st">
              <li>
                <a class="jie-title" href="<%=basePath %>/front/frontIndex_getTheQuestion?quesId=<s:property value="list[#st.index][0]"/>"><s:property value="list[#st.index][1]"/></a>
                <i><s:date name="list[#st.index][2]"/></i>
                <a class="mine-edit" href="<%=basePath %>/front/frontIndex_getTheQuestion?quesId=<s:property value="list[#st.index][0]"/>">查看</a>
                <a class="mine-edit" href="<%=basePath %>/front/frontQuestion_editQuestion?q_id=<s:property value="list[#st.index][0]"/>">编辑</a>
              </li>
            </s:iterator>


          </ul>
          <div id="pages"></div>
        </div>
        <div class="layui-tab-item">
            <ul class="mine-view jie-row">
                <s:iterator value="list2" id="question" status="st2">
                    <li>
                        <span class="jie-title" ><s:property value="list2[#st2.index][1]"/></span>
                        <i><s:date name="list2[#st2.index][2]"/></i>
                        <a class="mine-edit" href="<%=basePath %>/front/frontQuestion_editQuestion?q_id=<s:property value="list2[#st2.index][0]"/>">编辑</a>
                    </li>
                </s:iterator>

            </ul>

          <div id="pages2"></div>
        </div>

      </div>
    </div>
  </div>
</div>

<s:include value="../commom/comFooter.jsp"/>


<script>
    layui.use(['element','laypage'], function() {
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
        var laypage = layui.laypage;

        <%--console.log(<s:property value="count" />);--%>
        var curr = <s:property value="page" />;
        var count = <s:property value="count" />;
        var fir = laypage.render({
            elem: 'pages' //注意，这里的是 ID，不用加 # 号
            ,count: count //数据总数，从服务端得到
            ,limit: 10
            ,curr: curr
            ,group:6
            ,theme: '#1E9FFF'
            ,jump: function(obj, first) {
                var curr = obj.curr;
                if(!first) {
                    window.location.href = "<%=basePath %>/frontUser/FrontUser_userIndex.action?page="+curr;
                }
            }
        });
        var two = laypage.render({
            elem: 'pages2' //注意，这里的是 ID，不用加 # 号
            ,count: count //数据总数，从服务端得到
            ,limit: 10
            ,curr: curr
            ,group:6
            ,theme: '#1E9FFF'
            ,jump: function(obj, first) {
                var curr = obj.curr;
                if(!first) {
                    window.location.href = "<%=basePath %>/frontUser/FrontUser_userIndex.action?page="+curr;
                }
            }
        });




    });



</script>

</body>
</html>
