<%@ page import="com.qa.entity.QaBackUser" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: 3tu
  Date: 2019/5/24
  Time: 15:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

    QaBackUser backUser = (QaBackUser ) session.getAttribute("qaBackUser");
%>


<html>
<link type="text/css" href="<%=basePath %>/static/plugins/layui/css/layui.css" rel="stylesheet" />
<head>
</head>
<body>

</body>
<form class="layui-form" action="">

    <div class="layui-form-item">
        <label class="layui-form-label">话题管理</label>

        <div class="layui-input-block" style="width:70%;">
            <select name="topic" lay-verify="required">
<s:iterator value="list" status="st" var="b">

    <s:if test=" topicId == toId ">
        <option selected value="<s:property value="toId"/>"><s:property value="topicName"/></option>
    </s:if>

    <s:else>
        <option value="<s:property value="toId"/>" ><s:property value="topicName "/></option>
    </s:else>

</s:iterator>
            </select>
        </div>
    </div>


    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
</form>

<script src="<%=basePath %>/static/plugins/js/jquery-3.1.1.min.js" type="text/javascript"></script>
<script src="<%=basePath %>/static/plugins/layui/layui.all.js"></script>

<script>

    layui.use('form', function(){
        var form = layui.form;

        var userId = '<s:property value="userId" />';

        console.log(userId);

        //监听提交
        form.on('submit(formDemo)', function(data){
            $.ajax({
                url: '<%=path%>/admin/qaBackUser_updateBackUserTopic.action'
                , data: {"id": userId,"topic":data.field.topic}
                , dataType: 'json'
                // 返回成功的
                , success: function (data) {
                    if (data.status == "0") {
                        layer.msg("更新失败!!", {
                            icon: 2,
                            timeout: 2000
                        }, function () {
                            parent.layer.closeAll()
                        });
                    } else {
                        layer.msg("更新成功!", {
                            icon: 1,
                            timeout: 2000
                        }, function () {
                            parent.layer.closeAll();
                        });
                    }
                }
                // 超时
                , timeout: function () {
                    layer.msg("请求超时!", {
                        icon: 2,
                        timeout: 2000
                    }, function () {
                        parent.layer.closeAll()
                    });
                }
                // 错误
                , error: function () {
                    layer.msg("发生错误!请与管理员联系!", {
                        icon: 2,
                        timeout: 2000
                    }, function () {
                        parent.layer.closeAll()
                    });
                }
            });
            return false;
        });
    });
</script>

</html>

