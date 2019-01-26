<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: 3tu
  Date: 2017/12/29
  Time: 12:35
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
    <style>
        body .mytwolayer {
            border-radius:10px;
        }
        body .mytwolayer .layui-layer-title{
            border-radius:10px 10px 0 0;
            background: #7c8c96;
            color: #fff;
        }
        .login-style {
            color: #0d71bb;
        }
    </style>
</head>
<body>

<%--隐藏域，判断session--%>
<input type="hidden" id="sessionjudge" value="<%=frontUser%>">
<%--header部分--%>
<s:include value="../commom/comHeader.jsp"/>
<%--header结束--%>

<%--主体部分--%>
<div class="layui-container">
    <div class="layui-row layui-col-space15">
        <div class="layui-col-md8 content detail">
            <div class="fly-panel detail-box">

                <s:iterator value="theQues.question" var="tq">
                    <input type="hidden" class="quesIdc" value="<s:property value="#tq.quesId"></s:property>">
                <h1><s:property value="#tq.quesTitle"></s:property></h1>
                <div class="fly-detail-info">
                    <span class="layui-badge fly-detail-column" style="background-color: #999;"><s:property value="#tq.topicName"></s:property></span>

                    <%--<span class="layui-badge" style="background-color: #999;">未结</span>--%>

                    <%--<span class="layui-badge layui-bg-black">置顶</span>--%>
                    <%--<span class="layui-badge layui-bg-red">精帖</span>--%>


                    <div class="fly-admin-box" data-id="123">
                        <s:iterator value="#tq.labels" var="ql">
                            <span class="layui-btn layui-btn-xs jie-admin"><s:property value="#ql"></s:property></span>
                        </s:iterator>
                        <%--<span class="layui-btn layui-btn-xs jie-admin" type="del">删除</span>--%>

                        <%--<span class="layui-btn layui-btn-xs jie-admin" type="set" field="stick" rank="1">置顶</span>--%>

                        <%--<span class="layui-btn layui-btn-xs jie-admin" type="set" field="status" rank="1">加精</span>--%>
                    </div>
                    <span class="fly-list-nums">
                         <i class="iconfont" title="浏览">&#xe60b;</i><s:property value="#tq.browseCount"></s:property>
                         <i class="iconfont icon-pinglun1" title="回复"></i><s:property value="#tq.commentCount"></s:property>
                     </span>
                </div>

                <div class="detail-about">
                    <a class="fly-avatar" href="<%=basePath %>/frontUser/FrontUser_userHome.action?id=<s:property value="#tq.Id" />">
                        <img src="<%=basePath %>/<s:property value="#tq.headPhoto" />" alt="">
                    </a>
                    <div class="fly-detail-user">
                        <a href="<%=basePath %>/frontUser/FrontUser_userHome.action?id=<s:property value="#tq.Id" />" class="fly-link">
                            <cite><s:property value="#tq.accountName"></s:property></cite>
                            <%--<i class="iconfont icon-renzheng" title="认证信息：{{ rows.user.approve }}"></i>--%>
                            <%--<i class="layui-badge fly-badge-vip">VIP3</i>--%>
                        </a>
                        <span><s:date name="#tq.createDate" format="yyyy年MM月dd日 hh:mm:ss"></s:date></span>
                    </div>
                    <div class="detail-hits" id="LAY_jieAdmin" data-id="123">
                        <span style="padding-right: 10px; color: #FF7200">悬赏：60声望</span>
                        <span class="layui-btn layui-btn-xs jie-admin" type="edit"><a href="">关注此贴</a></span>
                    </div>
                </div>
                <div class="detail-body photos">
                    <%--编辑器编辑格式内容--%>
                    <s:property value="#tq.quesDetail" escape="false"></s:property>

                </div>
                </s:iterator>
            </div>

            <div class="fly-panel detail-box" id="flyReply">
                <fieldset class="layui-elem-field layui-field-title" style="text-align: center;">
                    <legend>回帖</legend>
                </fieldset>

                <% if(frontUser != null){ %>
                <%--回复框--%>
                <div class="layui-form layui-form-pane">
                    <form action="" method="post">
                        <div class="layui-form-item layui-form-text">
                            <a name="comment"></a>
                            <div class="layui-input-block">
                                <textarea id="L_content" name="content" ></textarea>
                            </div>
                        </div>
                        <div class="layui-form-item">
                            <input type="hidden" name="jid" value="123">
                            <button class="layui-btn" lay-filter="reply_one" lay-submit>提交回复</button>
                        </div>
                    </form>
                </div>
                <%--回复框--%>
                <% }else {%>
                <p class="" style="margin-left:37%">若要回复，请先 <a href="<%=path %>/frontUser/FrontUser_UserLogin.action" class="login-style">登录</a>
                    或者 <a href="<%=path %>/frontUser/FrontUser_registerUser.action" class="login-style">注册</a> </p>
                <% }%>
                <hr class="layui-bg-gray">


                <%--评论列表--%>
                <ul class="jieda" id="jieda">

                </ul>
            </div>
        </div>
        <s:include value="../commom/comRight.jsp"/>
    </div>
</div>

<s:include value="../commom/comFooter.jsp"/>
<script>

    layui.cache.page = 'jie';
    layui.cache.user = {
        username: '游客'
        ,uid: -1
        ,avatar: '../../res/images/avatar/00.jpg'
        ,experience: 83
        ,sex: '男'
    };
    layui.config({
        version: "2.0.0"
        ,base: '<%=basePath %>/static/plugins/layui/resmods/'
    }).extend({
        fly: 'index'
    }).use(['fly','laydate','table','laytpl','layer','element','form','layedit'], function() {
        var fly = layui.fly;        //回复框，判断登录session
        var table = layui.table;
        var laydate = layui.laydate;
        var laytpl = layui.laytpl;
        var layer = layui.layer;
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
        var form = layui.form;
        var layedit = layui.layedit;

        // 初始化编辑器
        var edit = layedit.build('L_content',{
            height:150
            ,uploadImage: {
                url:"<%=path%>/front/frontQuestion_uploadImage.action"
            }
        });


        var quesId = $(".quesIdc").val();
        //初始化ajax加载评论
        $.ajax({
            url: '<%=path%>/front/frontIndex_getTheOneComment.action'
            , data: {'quesId':quesId}
            , dataType: 'json'
            // 返回成功的
            , success: function (data) {
                if (data.status == "0") {
                    getComment_one(data);
                } else {

                }
            }
            ,complete:function() {

            }
        });


        /**
         *获取到json数据对其进行组装，嵌入评论页
         * */
        function getComment_one(data) {
            var commList = data.commList;
            var html;
            if(commList.length != 0) {

                $.each(commList, function(index, item) {
                    var date = new Date(item.createDate['time']).toLocaleDateString();
                    var time = new Date(item.createDate['time']).toLocaleTimeString();
//                    console.log(item.commentCount)  ;
                    if(item.commentCount != 0)
                        var count = '<i class="iconfont icon-svgmoban53"></i>' + item.commentCount + ' 查看回复';
                    else
                            count = '<i class="iconfont icon-svgmoban53"></i>暂无回复';
                    var dateTime = date + " " + time;
                    var headphoto = '<%=basePath %>/'+item.headPhoto+'';
                    html = '<li>'+
                        '<div class="detail-about detail-about-reply">'+
                        '<a class="fly-avatar" href="">'+
                        '<img src="'+headphoto+'" alt=" ">'+
                        '</a>'+
                        '<div class="fly-detail-user">'+
                        '<a href="#" class="fly-link">'+
                        '<cite>'+item.accountName+'</cite>'+
                        '</a>'+
                        '</div>'+
                        '<div class="detail-hits">'+
                        '<span>'+dateTime+'</span>'+
                        '</div>'+
                        '</div>'+
                        '<div class="detail-body jieda-body photos">'+
                        '<p>'+item.content+'</p>'+
                        '</div>'+
                        ' <div class="jieda-reply">'+
                        '<span class="jieda-zan" type="zan">'+
                        '<i class="iconfont icon-zan"></i>'+
                        '<em>'+item.likes+'</em>'+
                        '</span>'+
                        '<span type="reply" class="getTwoComm" data-id="'+item.commId+'">'+
                       count+
                        '</span>'+
                        '<div class="jieda-admin">'+
                        '<span class="jieda-accept addTheComm" data-id="'+item.commId+'">添加回复</span>'+
                        '</div>'+
                        '</div>'+
                        '</li>';

                    $("#jieda").append(html);
                });
            }else {
                    html = '<p style="margin-top:30px;margin-left:37%">:( 暂无评论喔~~~,  &nbsp;快来抢沙发吧！</p>';
                    $("#jieda").append(html);
            }


        }


        /**
         * 查看单个评论下的二级评论
         */
        $(document).on('click', '.getTwoComm', function() {
            var commId = $(this).data("id");
            var two_commentUrl = "<%=path%>/front/frontIndex_getTheTwoComment.action?pqId="+commId;
            layer.open({
                title: '二级评论',
                type: 2,
                shadeClose: true,
                shade: 0.2,
                fix:true,
                shift: 2,
                maxmin: true,
                area: ['700px', '550px'],
                content: two_commentUrl,
                scrollbar: false,
                skin:'mytwolayer'
            });
        });

        /**
         * 对单个评论进行回复添加
         */
        $(document).on('click', '.addTheComm', function() {
            var front = $('#sessionjudge').val();  //获取隐藏域的session
            //判断是否已经登录
            if(front != "") {
                var commId = $(this).data("id");
                var addtwo_commentUrl = "<%=path%>/front/frontQuestion_addTwoCommentView.action?pid="+commId+"&quesId="+quesId;
                layer.open({
                    type: 2,
                    content: addtwo_commentUrl,
                    title: '添加回复',
                    type: 2,
                    shadeClose: true,
                    shade: 0.2,
                    fix:true,
                    shift: 2,
                    maxmin: true,
                    area: ['700px', '550px'],
                    scrollbar: false,
//                    success: function(layero, index) {
//                        var body = layer.getChildFrame('.commenTwoLayer', index);
//                        body.find(".cPid").text(commId);
////                        console.log(layero);
////                        console.log(index);
//                    },

                });
            }else {
                layer.msg("请先登录哦！", {time: 1000,icon: 3});
            }
        });

        /**
         * 回复问题主评论，即一级评论
         */
        form.on('submit(reply_one)',function(){
            layer.confirm('确认回复？', {
                btn: ['确认', '取消'] //按钮
            }, function () {
//                console.log(layedit.getContent(edit));
                $.ajax({
                    url: "<%=path%>/front/frontQuestion_questionReply.action",
                    type: "POST",
                    data: {"qaComment.content":layedit.getContent(edit), "qaComment.questionId":quesId},
                    success: function (data2) {
                        if (data2 == "1") {
                            layer.msg("回复成功!", {time: 1000, icon: 1}, function () {location.reload()});
                        } else {
                            layer.msg("回复失败!", {time: 1000, icon: 2});
                        }
                    },
                    error: function () {
                        layer.msg('请求服务器超时', {time: 1000, icon: 2},function () {location.reload();});
                    }
                });
                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            }, function () {

            });
            return false;
        });

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

</body>
</html>