<%--
  Created by IntelliJ IDEA.
  User: 3tu
  Date: 2018/12/29
  Time: 15:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="<%=basePath %>/static/plugins/layui/css/layui.css">
    <link rel="stylesheet" href="<%=basePath %>static/css/global.css">
    <style>
        html body {
            margin-top: 0px;
            background: #fff;
        }
        /*.twoComm-style {*/
        /*margin-top: 5px;*/
        /*margin-left: 10px;*/
        /*margin-right: 10px;*/
        /*}*/
        body .mytwolayer {
            border-radius:10px;
        }
        body .mytwolayer .layui-layer-title{border-radius:10px 10px 0 0;}
    </style>
</head>
<body>
<div class="twoComm-style">
    <div class="commenTwoLayer">
        <div class="layui-form layui-form-pane">
            <form action="" method="post">
                <input type="hidden" class="pid" value="<s:property value="pid"/>">
                <input type="hidden" class="quesId" value="<s:property value="quesId"/>">
                <div class="layui-form-item layui-form-text">
                    <a name="comment"></a>
                    <div class="layui-input-block">
                        <textarea id="layerContent" ></textarea>
                    </div>
                </div>
                <div class="layui-form-item">
                    <button class="layui-btn" lay-filter="reply_two" lay-submit>提交回复</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
<script src="<%=basePath %>/static/plugins/js/jquery-3.1.1.min.js" type="text/javascript"></script>
<script src="<%=basePath%>/static/plugins/layui/layui.js"></script>
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
    }).use(['form','layedit'], function() {
        form = layui.form;
        var layedit = layui.layedit;

        // 初始化编辑器
        var edit = layedit.build('layerContent',{
            height:150
            ,uploadImage: {
                url:"<%=path%>/front/frontQuestion_uploadImage.action"
            }
        });



        form.on('submit(reply_two)',function(){
        var pid = $(".pid").val();
        var quesId = $(".quesId").val();
            layer.confirm('确认回复？', {
                btn: ['确认', '取消'] //按钮
                }, function () {
                $.ajax({
                    url: "<%=path%>/front/frontQuestion_questionReply.action",
                    type: "POST",
                    data: {"qaComment.content":layedit.getContent(edit), "qaComment.questionId":quesId,"qaComment.pid":parseInt(pid)},
                    success: function (data2) {
                    if (data2 == "1") {
                        layer.msg("回复成功!", {time: 1000, icon: 1}, function () {parent.location.reload()});
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


    });

</script>
</html>

