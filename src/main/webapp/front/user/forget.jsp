<%@ taglib prefix="s" uri="/struts-tags" %>

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
                <input type="text" name="account" required  lay-verify="account" placeholder="请输入账户" autocomplete="off" class="layui-input account">
            </div>
        </div>


        <div class="layui-form-item">
            <label for="email" class="layui-form-label">邮箱</label>
            <div class="layui-input-inline">
                <input type="text" id="email" name="email" required lay-verify="email|checkEmail" placeholder="请输入邮箱" autocomplete="off" class="layui-input">
            </div>
        </div>




        <div class="layui-form-item">
            <div class="layui-input-block">
                <button class="layui-btn" lay-submit lay-filter="addBackUser">立即提交</button>
                <button type="reset" class="layui-btn layui-btn-primary">重置</button>
            </div>
        </div>
    </form>

<style>
    .layui-form-label{
        text-align: center;
    }
    .layui-input{
        width: 80%;
    }
</style>
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

            },

            // 邮箱与账户是否对应
            checkEmail:function (value, item) {
                var account = $(".account").val();
                var flag = false;

                $.ajax({
                    url: "<%=path%>/frontUser/FrontUser_checkEmail.action",
                    type: "POST",
                    async:false,
                    data: {"account":account,"email":value},
                    success: function (data2) {
                        if(data2 == "0"){
                            flag = true;
                        }
                    }
                });

                if(flag){
                    return "账户与邮箱不对应!";
                }
            }

        });

        // 提交表单
        form.on('submit(addBackUser)', function (data) {

//            console.log(data.field);
            layer.confirm('确认重置密码？', {
                btn: ['确认', '取消'] //按钮
            }, function () {
                $.ajax({
                    url: "<%=path%>/frontUser/FrontUser_resetPassword.action",
                    type: "POST",
                    data: data.field,
                    beforeSend: function () {
                        //
                    },
                    success: function (data2) {
                        if (data2 == "1") {
                            layer.msg("重置密码成功!", {time: 1000, icon: 1}, function () {parent.layer.closeAll();});
                        } else {
                            layer.msg("重置密码失败!", {time: 1000, icon: 2},function () {parent.layer.closeAll();});
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
