<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: 3tu
  Date: 2019/5/25
  Time: 22:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<html>
<head>
    <title></title>
    <link rel="stylesheet" href="<%=basePath %>/static/plugins/layui/css/layui.css">
    <link rel="stylesheet" href="<%=basePath %>static/css/global.css">
</head>
<body>

<form class="layui-form" action="">


    <div class="layui-form-item">
        <label class="layui-form-label">账户</label>
        <div class="layui-input-inline">
            <input type="text" name="qaBackUser.account" required  lay-verify="account" placeholder="请输入账户" autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">姓名</label>
        <div class="layui-input-inline">
            <input type="text" name="qaBackUser.name" required  placeholder="请输入标题" autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label for="email" class="layui-form-label">邮箱</label>
        <div class="layui-input-inline">
            <input type="text" id="email" name="qaBackUser.email" required lay-verify="email" autocomplete="off" class="layui-input">
        </div>
    </div>

    <div class="layui-form-item">
        <label for="pass" class="layui-form-label">密码</label>
        <div class="layui-input-inline">
            <input type="password" id="pass" name="qaBackUser.password" required lay-verify="pass" autocomplete="off" class="layui-input">
        </div>
        <div class="layui-form-mid layui-word-aux">6到16个字符</div>
    </div>

    <div class="layui-form-item">
        <label class="layui-form-label">性别</label>
        <div class="layui-input-inline">
            <input type="radio" name="qaBackUser.sex" value="0" title="男" checked>
            <input type="radio" name="qaBackUser.sex" value="1" title="女" >
        </div>
    </div>


    <div class="layui-form-item">
        <label class="layui-form-label">话题管理</label>

        <div class="layui-input-inline" style="width:70%;">
            <select name="qaBackUser.topic" lay-verify="required">
                <s:iterator value="list" status="st" var="b">
                        <option value="<s:property value="toId"/>" ><s:property value="topicName "/></option>
                </s:iterator>
            </select>
        </div>
    </div>


    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="addBackUser">立即提交</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
</form>
</body>

<script src="<%=basePath %>/static/plugins/js/jquery-3.1.1.min.js" type="text/javascript"></script>
<script src="<%=basePath %>/static/plugins/layui/layui.all.js"></script>

<script>

    layui.use('form', function() {
        var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery;

        // 表单验证
        form.verify({
            // 用户名验证
            account: function (value, item) { //value：表单的值、item：表单的DOM对象
                if (!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)) {
                    return '用户名不能有特殊字符';
                }
                if (/(^•••\_)|(\__)|(\_+$)/.test(value)) {
                    return '用户名首尾不能出现下划线\'_\'';
                }
                if (/^\d+\d+\d$/.test(value)) {
                    return '用户名不能全为数字';
                }
                if(!/^[\S]{4,10}$/.test(value)){
                    return '用户名为4到10位';
                }

                // 检查账户是否已经存在
                var flag = false;
                // 必须使用方法,否则执行顺序为先判断flag,后执行ajax,即flag始终为false
                function checkU(){
                    $.ajax({
                        url: "<%=path%>/admin/qaBackUser_checkAccount.action",
                        type: "POST",
                        async:false,
                        data: {"account":value},
                        success: function (data2) {
                            if(data2.status == "0"){
                                flag = true;
                            }
                        }
                    });
                }

                checkU();
                if(flag){
                    return "账户已经存在!";
                }
            }


            , pass: [
                /^[\S]{6,12}$/
                , '密码必须6到12位，且不能出现空格'
            ]

        });

        // 提交表单
        form.on('submit(addBackUser)', function (data) {

//            console.log(data.field);
            layer.confirm('确认提交？', {
                btn: ['确认', '取消'] //按钮
            }, function () {
                layer.msg('已提交！', {icon: 1});
                $.ajax({
                    url: "<%=path%>/admin/qaBackUser_addBackUserForm.action",
                    type: "POST",
                    data: data.field,
                    beforeSend: function () {
                        //
                    },
                    success: function (data2) {
                        if (data2.status == "1") {
                            layer.msg("创建成功!", {time: 1000, icon: 1}, function () {parent.layer.closeAll();});
                        } else {
                            layer.msg("创建失败!", {time: 1000, icon: 2},function () {parent.layer.closeAll();});
                        }
                    },
                    error: function () {
                        layer.msg('请求服务器超时', {time: 1000, icon: 2},function () {parent.layer.closeAll();});
                    }
                });
                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            });

            return false;

        });

    });

</script>

</html>
