<%--
  Created by IntelliJ IDEA.
  User: 3tu
  Date: 2019/1/27
  Time: 20:47
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>编辑问题</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="keywords" content="fly,layui,前端社区">
    <link rel="stylesheet" href="<%=basePath %>/static/plugins/layui/css/layui.css">
    <link rel="stylesheet" href="<%=basePath %>/static/css/global.css">
    <link rel="stylesheet" href="<%=basePath %>/static/css/front_index.css">
</head>

<style>

    .fly-avatar {
        position: absolute;
        left: 10px;
        top: 5px;
    }
    .layui-form-pane .layui-form-checkbox {
        margin-right: 5px;
    }

</style>
<body>

<s:include value="../commom/comHeader.jsp"/>

<div class="layui-container fly-marginTop">
    <div class="fly-panel" pad20 style="padding-top: 5px;">
        <div class="layui-form layui-form-pane">
            <div class="layui-tab layui-tab-brief" lay-filter="user">
                <ul class="layui-tab-title">
                    <li class="layui-this">发表新问题<!-- 编辑帖子 --></li>
                </ul>
                <div class="layui-form layui-tab-content" id="LAY_ucm" style="padding: 20px 0;">
                    <div class="layui-tab-item layui-show">
                        <form action="" method="post">
                            <div class="layui-row layui-col-space15 layui-form-item">
                                <div class="layui-col-md3">
                                    <label class="layui-form-label">选择话题</label>
                                    <div class="layui-input-block">
                                        <select name="qaQuestion.topicId" id="topicId" class="selectpicker" lay-filter="topicS" data-title="选择话题" required="required" data-style="btn-default btn-block" data-menu-style="dropdown-blue">
                                            <s:iterator value="topicList" var="topic">
                                                <s:if test="#topic.toId == quesInfo[7]">
                                                    <option selected value="<s:property value="#topic.toId"/>"><s:property value="#topic.topicName"  /></option>
                                                </s:if>
                                                <s:else>
                                                    <option value="<s:property value="#topic.toId"/>"><s:property value="#topic.topicName"/></option>
                                                </s:else>
                                            </s:iterator>
                                        </select>
                                    </div>
                                </div>
                                <input type="hidden" name="q_id" value= <s:property value= 'quesInfo[9]' />  />
                                <input type="hidden" name="qaQuestion.createDate" value= "<s:property value= 'quesInfo[1]' />"  />
                                <div class="layui-col-md9">
                                    <label for="L_title" class="layui-form-label">标题</label>
                                    <div class="layui-input-block">
                                        <input type="text" id="L_title" name="qaQuestion.title" required lay-verify="required" autocomplete="off" class="layui-input" value=<s:property value="quesInfo[0]" /> >
                                    </div>
                                </div>
                            </div>
                            <div class="layui-row layui-col-space15 layui-form-item" id="">
                                <div class="layui-col-md12">
                                    <label class="layui-form-label">选择标签</label>
                                    <div class="layui-input-block" id="ajaxAfter">

                                    </div>
                                </div>

                            </div>
                            <div class="layui-form-item layui-form-text">
                                <div class="layui-input-block">
                                    <%--<textarea id="L_content" name="qaQuestion.detail" required lay-verify="required" placeholder="详细描述" class="layui-textarea fly-editor" style="height: 260px;"></textarea>--%>
                                    <textarea id="L_content" name="qaQuestion.detail" ></textarea>
                                </div>
                            </div>
                            <div class="layui-form-item">
                                <button class="layui-btn" lay-filter="addQues" lay-submit>立即发布</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<s:include value="../commom/comFooter.jsp"/>


</body>
</html>

<script>

    layui.use(['element','laypage','form','layedit'], function() {
        var layedit = layui.layedit;
        var element = layui.element; //导航的hover效果、二级菜单等功能，需要依赖element模块
        laypage = layui.laypage;
        form = layui.form;

        // 初始化编辑器
        var edit = layedit.build('L_content',{
            height:550
            ,uploadImage: {
                url:"<%=path%>/front/frontQuestion_uploadImage.action"
            }
        });
        <%--layedit.setContent(edit,'<s:property value="quesInfo[0]" />',false);--%>
        $("#L_content").text('<s:property value="quesInfo[2]" escape="false" />');

        var ques_id = '<s:property value="quesInfo[9]" escape="false" />';

        var nowTop = '<s:property value="quesInfo[7]" escape="false" />';

        var nowLables = '<s:property value="quesInfo[8]" escape="false" />';
        var labels = nowLables.split(",");
        labels = $.trim(labels);
        console.log(labels);

        form.on('select(topicS)',function() {
            var topicId = $('#topicId').val();
            if(topicId != "") {
                $.ajax({
                    url: "<%=path%>/front/frontQuestion_ajaxForLabel.action",
                    type: 'POST',
                    data: {'topicId': topicId},
                    dataTpye: 'json',
                    success: function (data) {
                        if (data.status == 0) {
                            ajaxLabel(data);
                            form.render();
                        } else {
                            layer.msg("数据错误！");
                        }
                    }
                });
            }
        });

        $(function() {
            $.ajax({
                url: "<%=path%>/front/frontQuestion_ajaxForLabel.action",
                type: 'POST',
                data: {'topicId': nowTop},
                dataTpye: 'json',
                success: function (data) {
                    if (data.status == 0) {
                        ajaxLabel(data);
                        form.render();
                    } else {
                        layer.msg("数据错误！");
                    }
                }
            })
        });

        function ajaxLabel(data) {
            var list = data.labelList;
            var html;
            $('#ajaxAfter').empty();
            if(list.length != 0) {
                $.each(list, function(index, item) {
                    if(labels.indexOf(item.lId) != -1){
                        console.log("in  ");
                        html = '<input type="checkbox" checked name="qaQuestion.labelIds" value="'+item.lId+'">'+   item.labelName+'';
                    }
                    else{
                        console.log(labels.indexOf("2"));
                        console.log(item.lId);
                        html = '<input type="checkbox" name="qaQuestion.labelIds" value="'+item.lId+'">'+   item.labelName+'';
                    }

                    $("#ajaxAfter").append(html);
                });
            }else {
                html = '<p style="margin-top:30px;margin-left:37%">:( 暂无标签！</p>';
                $("#ajaxAfter").append(html);
            }
        }

        form.on('submit(addQues)',function(){
            $("#L_content").val(layedit.getContent(edit));
            layer.confirm('确认修改,问题一经修改，将不可删除？', {
                btn: ['确认', '取消'] //按钮
            }, function () {
                $.ajax({
                    url: "<%=path%>/front/frontQuestion_editQuesHandle.action",
                    type: "POST",
                    data: $("form").serialize(),
                    success: function (data2) {
                        console.log(data2);
                        if (data2 != "0") {
                            layer.msg("修改成功!", {time: 1000, icon: 1}, function () { window.location.href = "<%=basePath %>/front/frontIndex_getTheQuestion?quesId="+ques_id });
                        } else {
                            layer.msg("修改失败!", {time: 1000, icon: 2});
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