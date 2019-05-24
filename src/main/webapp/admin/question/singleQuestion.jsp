<%--
  Created by IntelliJ IDEA.
  User: 3tu
  Date: 2018/12/17
  Time: 14:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <title>问题详情</title>

    <link type="text/css" href="<%=basePath %>/static/plugins/layui/css/layui.css" rel="stylesheet" />
    <link type="text/css" href="<%=basePath %>/static/plugins/css/semantic.min.css" rel="stylesheet" />
    <link type="text/css" href="<%=basePath %>/static/plugins/css/font-awesome.min.css" rel="stylesheet" />
    <style>
        body {
            background: #f5f5f5;
        }
        .layui-btn-sm{
            background-color: #11bfe3;
        }
        .layui-btn-warm{
            background-color: #FFB800;
        }
        .quote1{
            border-left: 5px solid #11bfe3;
            border-radius: 4px;
            background-color: #fff;
        }
        .log{
            border-radius: 4px;
            background-color: #fff;
        }
        .layui-table-view .layui-form-checkbox, .layui-table-view .layui-form-radio, .layui-table-view .layui-form-switch {
            top: 0;
            margin: 4px;
            box-sizing: content-box;
        }
        .ct-chart {
            margin: 50px 0 10px;
            height: 260px;
        }
        .quote2 {
            border-left: 5px solid #fb760699;
            border-radius: 4px;
            background-color: #ebf3f24d;
            margin-left: 5px;margin-top: 10px;
        }
        .quote3 {
            border-left: 5px solid #365ce399;
            border-radius: 4px;
            background-color: #ebf3f24d;
            margin-left: 5px;margin-top: 10px;
        }
        .brow-comm {
            margin: 10px 20px 0 20px;
        }
        .comment_box {
            margin-left: 20px;
        }
        .text_style {
            margin-top: 10px;
        }
        .tool_comment {
            margin-top: 20px;
            font-size: 1.0em;
        }
        .comment_html_style {
            margin-left:30px;
            margin-right: 30px;
        }
        .ui.comments {
            max-width: 100%;
        }
        .ui.comments .comment .metadata .date {
            margin-left: 15px;
        }
        .ui.comments .comment .text {
            margin: 1.05em 0.92em 1.05em;
        }
        .ui.comments .comment .metadata .rating {
            margin: 1.05em 0.92em .05em;
            font-size: 1.20em;
        }
        .ui.comments .comment .metadata .reply {
            margin-left:10px;
            color: rgba(0,0,0,.4);
            cursor: pointer;
        }
        .ui.comments .comment .metadata .reply:hover {

            color: rgba(0,0,0,.8);
            cursor: pointer;
        }
        .ui.section.divider {
            margin-top: 13px;
        }
        .sign-style {
            font-family: Lato,'Helvetica Neue',Arial,Helvetica,sans-serif;
            font-weight: 400;
            line-height: 1em;
            color: #1B1C1D;
            text-transform: uppercase;
            text-align: center;
        }
        .title-style {
            margin-left: 50px;
            font-size: 16px;
            font-family: Lato,'Helvetica Neue',Arial,Helvetica,sans-serif;
            font-weight: 500;
            line-height: 1em;
            color: #1B1C1D;
            text-transform: uppercase;
            text-align: center;
        }
        .returnlast {
            margin-right: 7px;
            float: right;
        }

        .detail-about {
            position: relative;
            line-height: 20px;
            padding: 15px 15px 15px 75px;
            font-size: 13px;
            /*background-color: #f8f8f8;*/
            color: #999;
        }
        .fly-avatar {
            position: absolute;
            left: 11px;
            top: 3px;
        }
        .fly-avatar img {
            display: block;
            width: 45px;
            height: 45px;
            margin: 0;
            border-radius: 2px;
        }
    </style>


</head>
<body style="overflow-y:scroll;">
    <div class="">
            <blockquote class="layui-elem-quote quote1">

                <span class="title-style">问题标题：<s:property value="singleInfo.list[0]"/></span>
                <a href="javascript:;" class="layui-btn layui-btn-sm returnlast">
                <i class="layui-icon">&#xe615;</i> 返回
                </a>
            </blockquote>

        <%--问题的浏览日期图表--%>
        <div class="log">
            <div class="brow-comm">
                <fieldset class="layui-elem-field">
                    <legend>问题浏览信息</legend>
                    <blockquote class="layui-elem-quote quote2">
                        <form class="layui-form" style="display: inline-block;margin-left: 10px; min-height: inherit; vertical-align: bottom;">
                            <span class="sign-style">问题总的浏览量： <s:property value="singleInfo.list[7]"/> 人次</span>
                            <div class="layui-input-block" style="display: inline-block;margin-left: 20px; min-height: inherit; vertical-align: bottom;">
                                    <input type="hidden" value="<s:property value="singleInfo.q_id"/>" name="quesId" id="quesId">
                            </div>
                        </form>
                    </blockquote>
                </fieldset>
            </div>


        </div>

        <%--问题内容--%>
        <div class="log" >
            <fieldset class="brow-comm">
                <fieldset class="layui-elem-field">
                    <legend>问题详情</legend>
                        <div class="fly-panel detail-box">
                            <%--标题--%>
                            <h1 align="center"><s:property value="singleInfo.list[0]"></s:property></h1>
                                <%--所属标签--%>
                            <div class="fly-detail-info" style="text-align: center; padding-bottom: 2rem">
                                <span class="layui-badge fly-detail-column" style="background-color: #999;margin: 10px 10px;  ">所属话题:<s:property value="singleInfo.list[3]"></s:property></span>
                                    <div class="fly-admin-box" data-id="123" style="padding: 10px;">
                                        所属标签:
                                    <s:iterator value="singleInfo.labels" var="ql">
                                        <span class="layui-btn layui-btn-xs jie-admin"><s:property value="#ql"></s:property></span>
                                    </s:iterator>
                                </div>
                                <span class="fly-list-nums">
                     </span>
                            </div>
                                <%--作者信息--%>
                                <div class="detail-about" style="margin:0 auto; width: 30%;">
                                    <a class="fly-avatar" >
                                        <img  src="<%=basePath %>/<s:property value="singleInfo.list[5]" />" alt="">
                                    </a>
                                    <div class="fly-detail-user">
                                            <cite><s:property value="singleInfo.list[4]"></s:property></cite>
                                        <span><s:date name="singleInfo.list[2]" format="yyyy年MM月dd日 hh:mm:ss"></s:date></span>
                                    </div>
                                </div>
                                <%--编辑器编辑格式内容--%>
                            <fieldset class="layui-elem-field" style="padding: 30px; margin: 30px;">
                                <legend>内容详情</legend>
                                <div class="detail-body photos" style="    margin: 30px;">
                                    <s:property value="singleInfo.list[1]" escape="false"></s:property>
                                </div>
                            </fieldset>
                    </div>
                </fieldset>
            </div>
        </div>

        <%--问题的评论--%>
        <div class="log">
            <div class="brow-comm">
                <fieldset class="layui-elem-field">
                    <legend>评论信息</legend>
                    <blockquote class="layui-elem-quote quote3">
                        <form class="layui-form" style="display: inline-block;margin-left: 10px; min-height: inherit; vertical-align: bottom;">
                            <span class="sign-style">问题总的评论量： <s:property value="singleInfo.list[6]"/> 条</span>
                            <div class="layui-input-block" style="display: inline-block;margin-left: 20px; min-height: inherit; vertical-align: bottom;">

                            </div>
                        </form>
                    </blockquote>
                    <div class="comment_html_style">
                        <div class="comment_box ui comments " id="comment_html"><h3 class="ui dividing header">评论</h3>
                        </div>
                        <div style="text-align: center">
                            <div class="" id="pages">
                            </div>
                        </div>
                    </div>
                </fieldset>
            </div>
        </div>

    </div>

</body>
<script src="<%=basePath %>/static/plugins/js/jquery-3.1.1.min.js" type="text/javascript"></script>
<script src="<%=basePath %>/static/plugins/layui/layui.js"></script>
<%--<script src="<%=basePath %>/static/plugins/js/echarts.min.js"></script>--%>
<%--<script src="<%=basePath %>/static/js/basigleques.js"></script>--%>

<script type="text/javascript">
    layui.use(['table','laytpl','layer','laypage'], function() {
        var layer = layui.layer;
        var laypage = layui.laypage;

        var count=5;
        var curr=1;



        var quesId = $('#quesId').val();        //问题id

        InitComment();

        function InitComment(page){
            $.ajax({
                url: '<%=path%>/admin/qaBackQues_getComment.action'
                , data: {'qId':quesId,'page':page}
                , dataType: 'json'
                // 返回成功的
                , success: function (data) {
                    if (data.status == "0") {
                        getComment_one(data,page);
                        initPage();
                    } else {

                    }
                }
                ,complete:function() {

                }
            });
        }



        function initPage() {
            laypage.render({
                elem: 'pages' //注意，这里的是 ID，不用加 # 号
                ,count: count //    数据总数，从服务端得到
                ,limit: 5
                ,theme: '#1E9FFF'
                ,curr: curr
                ,jump: function(obj, first) {
                    var curr = obj.curr;
                    if(!first) {
                        InitComment(curr);
                    }
                }
            });
        }

        /**
         *获取到json数据对其进行组装，嵌入评论页
         * */
        function getComment_one(data,page) {
            $("#comment_html").html("");
            var commList = data.commList;
            count = data.count;
            curr =data.page;

            var html;
            $.each(commList, function(index, item) {
                var date = new Date(item.createDate['time']).toLocaleDateString();
                var time = new Date(item.createDate['time']).toLocaleTimeString();
                var dateTime = date + " " + time;
                var photo = "<%=basePath %>/"+item.headPhoto;

                html = '<div class="comment">'+
                            '<a class="avatar">'+
                                '<img src="'+photo+'"></a>'+
                                    '<div class="content">'+
                                        '<a class="author">'+item.accountName+'</a>'+
                                            '<div class="metadata">'+
                                                '<span class="date">'+dateTime+'</span>'+
                                            '</div>'+
                                            '<div class="text text_style">'+item.content+'</div>'+
                                            '<div class="metadata tool_comment">'+
                                                '<a class="reply getTwo" data-id="'+item.commId+'" href="javascript:;"><i class="fa fa-comments"></i>查看回复</a>'+
                                                '<a class="reply deleteOne" data-id="'+item.commId+'" href="javascript:;"><i class="fa fa-trash-o"></i>删除</a>'+
                                            '</div>'+
                                    '</div>'+
                        '</div>'+
                    '<div class="ui section divider"></div>';

                $("#comment_html").append(html);
            });

        }


        $(".returnlast").on('click', function() {
            window.location.href = "<%=basePath %>/admin/qaBackQues_allQuestionView.action";
        });
        /**
         *将二级评论显示在弹窗页面
         */

        $(document).on('click', '.getTwo', function() {
            var commId = $(this).data("id");
            var two_commentUrl = "<%=path%>/admin/qaBackQues_getTwoComment.action?pqId="+commId;
            layer.open({
                title: '二级评论',
                type: 2,
                shadeClose: true,
                shade: 0.8,
                fix:true,
                shift: 2,
                maxmin: true,
                area: ['700px', '550px'],
                content: two_commentUrl,
                scrollbar: false,
            });
        });


        $(document).on('click', '.deleteOne', function() {
            var commId = $(this).data("id");
            layer.confirm('这样会将该评论的下级一起删除！确定删除吗？', {
                icon:3,
                btn: ['确定删除', '我在想想']
            },function() {
                $.ajax({
                    url: "<%=path%>/admin/qaBackQues_deleteComment.action",
                    type: 'POST',
                    dataType: 'json',
                    data: {'cId': commId},
                    error: function(request){
                        layer.msg("请求服务器超时", {time: 1000, icon: 5});
                    },
                    success: function(data){
                        if (data.status = "0"){
                            layer.msg("删除成功！",{time: 1000,icon: 1}, function(){
                                location.reload();
                            });
                        }else{
                            layer.msg('删除失败！', {time: 1000,icon: 2});
                        }
                    }
                });
            });
        });


        //ajax加载完数据后重新修改iframe高度
        // 修改iframe的高度值
        function ChangeIfmHeight() {
//            if ($(window.parent.document).find("#iframepage")) {
//                var iframeObj = $(window.parent.document).find("#iframepage");
//                var thisheight = $(document).height();
//                iframeObj .height(thisheight);
//            }
        }

    });



</script>
</html>
