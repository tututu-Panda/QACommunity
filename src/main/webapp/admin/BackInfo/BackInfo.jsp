<%--
  Created by IntelliJ IDEA.
  User: 3tu
  Date: 2018/12/22
  Time: 17:19
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<html>
<head>
    <link rel="stylesheet" href="<%=basePath %>/static/plugins/layui/css/layui.css">
    <link rel="stylesheet" href="<%=basePath %>static/css/global.css">
    <link rel="stylesheet" href="<%=basePath %>static/css/front_index.css">
    <title>Title</title>


</head>
<style>
    .BackInfo{
        margin-left: 100px;
        margin-top: 60px;
    }
    .label{
        float: left;
        display: block;
        padding: 9px 15px;
        width: 80px;
        font-weight: 400;
        text-align: right;
    }
    .input-inline{
        float: left;
        width: 190px;
        margin-right: 10px;
    }
    .input-text{
        height: 38px;
        line-height: 38px;
        line-height: 36px\9;
        border: 1px solid #e6e6e6;
        background-color: #fff;
        border-radius: 2px;
    }
    .form-info{
        margin-bottom: 20px;
        clear: both;
        float:left;
    }
    .XGXX{
        display: inline-block;
        height: 38px;
        line-height: 38px;
        padding: 0 18px;
        background-color: #009688;
        color: #fff;
        white-space: nowrap;
        text-align: center;
        border: none;
        border-radius: 2px;
        cursor: pointer;
        opacity: .9;
        filter: alpha(opacity=90);
    }
    .form-info2{
        margin-bottom: 20px;
        clear: both;
        float:left;
        margin-left: 12px;
    }
    a{
        text-decoration: none;
        color: inherit;
    }
    .PasswordUpdate{
        width:400px;
        height:300px;
        background-color: white;
        position: absolute;
        margin-left:200px;
        margin-top: -680px;
        border-width: 3px;
        border-color: #ccc;
        border-style: solid;
        z-index: 1;

    }
</style>

<body>
<div style="z-index:-1;">
<s:form id="updateBackInfo" action="BackInfo_updateInfo" method="list">
<div class="BackInfo" style="width: 700px; height:700px; ">

        <div>
            <form class="layui-from">
            <div class="layui-form-item">
                <div class="avatar-add">
                    <p>建议尺寸168*168，支持jpg、png、gif，最大不能超过50KB</p>
                    <button type="button" id="upload" class="layui-btn upload-img">
                        <i class="layui-icon">&#xe67c;</i>上传头像
                    </button>
                    <img src="<%=basePath%>${list[0].photo}">
                    <span class="loading"></span>
                </div>

            </div>
            </form>
        </div>
    <div class="form-info">

        <label class="label ">账号:</label>
        <div class="input-inline">
            <input name="account" type="text" placeholder="请输入内容" class="input-text layui-word-aux "  readonly="readonly" value="${list[0].account}">
            <input name="photo" type="text" placeholder="请输入内容" class="input-text"  readonly="readonly" style="display: none" value="${list[0].photo}">
        </div>
        <label class="label">名字:</label>
        <div class="input-inline">
            <input name="name" type="text" placeholder="请输入内容" class="input-text"  value="${list[0].name}">
        </div>
    </div>
    <div class="form-info">
        <label class="label">邮箱:</label>
        <div class="input-inline">
            <input name="email" type="text" placeholder="请输入内容" class="input-text"  value="${list[0].email}">
        </div>
        <label class="label">角色:</label>
        <div class="input-inline ">
            <s:if test="list[0].status==1">
                <input name="type" type="text" placeholder="请输入内容" class="input-text layui-word-aux" disabled="" value="超级管理员">
            </s:if>
            <s:elseif test="list[0].status==2">
                <input name="type" type="text" placeholder="请输入内容" class="input-text layui-word-aux" disabled value="普通会员">
            </s:elseif>

        </div>

    </div>

    <div class="form-info" >
        <label class="label">性别</label>
        <div class="input-inline">
            <script>
                sex= ${list[0].sex};
            </script>
            <label class="label"><input name="sex" id="f" type="radio" value="0" <s:if test="list[0].sex == 0">checked</s:if>/>男 </label>
            <label class="label"><input name="sex" id="m" type="radio" value="1"<s:if test="list[0].sex == 1">checked</s:if>  />女 </label>
        </div>
    </div>

    <div style="float:left;margin-top:80px;clear: both;margin-left: 60px;">

        <div class="XGXX"> <a  href="javascript:document.getElementById('updateBackInfo').submit()" onClick="return confirm('确定修改?')">修改信息</a></div>
        <div class="XGXX" style="margin-left: 200px;" onclick="updatepassword()"> 修改密码</div>
        <script type="text/javascript">
            function  updatepassword(){

                document.getElementById('PasswordUpdate').style.display="block";
            }
            function updatepassword1() {
                document.getElementById('PasswordUpdate').style.display="none";

            }

        </script>
    </div>




</s:form>
    <div id="PasswordUpdate" style="display: none">
    <div class="PasswordUpdate" >
        <s:form action="BackInfo_updatePassword" id="updatePassword"  method="list"  style="margin-top:70px;">
            <input name="account" type="text" placeholder="请输入内容" class="input-text"  style="display: none;" value="${list[0].account}">
            <div style="float:left">
            <label class="label">原密码:</label>
            <div class="input-inline">
                <input name="pastpassword" type="password" placeholder="请输入内容" class="input-text"  value="${list[0].password}">
            </div>
            </div>
            <div style="float:left;margin-top:30px;">
            <label class="label">新密码:</label>
            <div class="input-inline">
                <input name="password" type="password" placeholder="请输入内容" class="input-text"  value="">
            </div>
            </div>
            <div style="    float:left;margin-left: 100px;margin-top: 30px;">
            <div class="XGXX"> <a  href="javascript:document.getElementById('updatePassword').submit()" onClick="return confirm('确定修改?')" >提交</a></div>
            <div class="XGXX" style="margin-left: 30px;" onclick="updatepassword1()"> 取消</div>
        </s:form>
    </div>


</div>

    </div>
</body>
</html>
<script src="<%=basePath %>/static/plugins/js/jquery-3.1.1.min.js" type="text/javascript"></script>
<script src="<%=basePath %>/static/plugins/layui/layui.js"></script>
<script>
    layui.use(['element','form','upload'], function() {
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
        form = layui.form;
        upload = layui.upload;

        upload.render({
            elem: '#upload' //绑定元素
            ,url: '<%=path%>/admin/BackInfo_uploadPhoto.action' //上传接口
            ,accept:"images"
            ,exts:'jpg|png|gif'
            ,size:51200
            ,drag:true
            ,done: function(data){
                console.log(data);
                if(data == "0"){
                    layer.msg("上传成功!", {time: 1000, icon: 1},function () {
                        parent.location.reload();
                    });
                }else{
                    layer.msg("上传失败!", {time: 1000, icon: 2});
                }
            }
            ,error: function(){
                layer.msg("请求异常,请联系管理员!", {time: 1000, icon: 2});
            }
        });
    });
</script>
