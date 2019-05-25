<%--
  Created by IntelliJ IDEA.
  User: 3tu
  Date: 2019/5/24
  Time: 12:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<html>
<link type="text/css" href="<%=basePath %>/static/plugins/layui/css/layui.css" rel="stylesheet" />

<style>


    .log{
        border-radius: 4px;
        background-color: #fff;
    }
    th {
        text-align: center;
    }

</style>

<head>
    <title>后台用户</title>
</head>
<body>
<div class="admin-main">
    <blockquote class="layui-elem-quote ">
        <form class="layui-form" style="display: inline-block;margin-left: 10px; min-height: inherit; vertical-align: bottom;">

            <div class="layui-input-block" style="display: inline-block;margin-left: 20px; min-height: inherit; vertical-align: bottom;">
                <div class="layui-form-pane">
                    <input class="layui-input keywords" placeholder="姓名关键字" style="width: 300px;height:30px; line-height:30px;"  value="" >
                </div>
            </div>

            <a href="javascript:;" class="layui-btn layui-btn-sm" style="margin-left: 20px;" id="com_search">
                <i class="layui-icon">&#xe615;</i> 搜索
            </a>

            <a href="javascript:;" class="layui-btn layui-btn-sm" id="com_searchAll">
                <i class="layui-icon">&#xe615;</i> 查看全部
            </a>
        </form>
    </blockquote>
    <div class="log">
        <fieldset class="layui-elem-field">
            <legend>用户列表</legend>
            <div class="layui-field-box">
                <div class="layui-btn-group">
                    <button class="layui-btn layui-btn-sm " id="addBackUser">添加管理员</button>
                </div>
                <table id="demo1" lay-filter="community"></table>
            </div>
        </fieldset>
    </div>
</div>


<%--性别模板--%>
<script type="text/html" id="sexTpl">
    {{#  if(d[3] == 0){ }}
    <span>男士</span>
    {{#  } else  if(d[3] == 1) { }}
    <span>女士</span>
    {{#  } }}
</script>

</body>
<script src="<%=basePath %>/static/plugins/js/jquery-3.1.1.min.js" type="text/javascript"></script>
<script src="<%=basePath %>/static/plugins/layui/layui.all.js"></script>
<script type="text/html" id="barDemo">
    <a class="layui-btn layui-bheighttn-warm layui-btn-sm" lay-event="alter">重置密码</a>
    {{#  if(d[6] == 0){ }}
    <a class="layui-btn layui-btn-sm layui-btn-warm"  lay-event="cancelDel">
        使用
        {{#  } else  if(d[6] == 1) { }}

    <a class="layui-btn layui-btn-sm layui-btn-danger"  lay-event="del">
        禁用
        {{#  } }}
    </a>
        <a class="layui-btn layui-btn-sm layui-btn-danger"  lay-event="changeTopic">
            修改权限
        </a>
    </a>

</script>
<script>
    layui.use(['table','laytpl'], function(){
        var table = layui.table;
        var laytpl = layui.laytpl;



        table.render({
            elem: '#demo1'
            ,id:'table_community'
            ,height: 700
            ,url: '<%=path%>/admin/qaBackUser_getBackUserList.action' //数据接口
            ,page: true //开启分页
            ,align:'center'
            ,loading:true
            ,cols: [[ //表头
                {field: '0', title: 'ID',  sort: true,width:"10%"}
                ,{field: '1', title: '账户',  sort: true,width:"12.5%"}
                ,{field: '2', title: '姓名', sort: true,width:"18%"}
                ,{field: '3', title: '性别', sort: true,width:"10%", templet:"#sexTpl"}
                ,{field: '4', title: '邮箱',width:"15%"}
                ,{field: '5', title: '管理话题',width:"14%"}
                ,{toolbar: '#barDemo',width:"20%"}
            ]]

        });


        // 查看全部
        $("#com_searchAll").on('click',function () {

            table.reload('table_community',{
                url: '<%=path%>/admin/qaBackUser_getBackUserList.action' //数据接口
                ,where:{
                    name:""
                }
            });
        });

        // 搜索
        $("#com_search").on('click',function () {
            var name = $(".keywords").val();

            table.reload('table_community',{
                url: '<%=path%>/admin/qaBackUser_getBackUserList.action' //数据接口
                ,where:{
                    "name":name
                }
            });
        });


        // 添加管理员
        $("#addBackUser").on("click",function () {
            layer.open({
                type: 2,
                title: ['添加管理员', 'text-align:center;'],
                content: '<%=path%>/admin/qaBackUser_addBackUser.action',
                area:['500px', '550px'],  //宽高
                resize: false,		//是否允许拉伸
                scrollbar: false,
                end: function(){
                    location.reload();
                }
            });
        });


        // 监听工具条
        table.on('tool(community)',function(obj){
            var data = obj.data;
            var layEvent = obj.event;


            // 禁用事件
            if(layEvent === "del") {
                layer.confirm("确定禁用吗?", function (index) {
                        $.ajax({
                            url: '<%=path%>/admin/qaBackUser_banBackUser.action'
                            , data: {"id": data[0],'status':0}
                            , dataType: 'json'
                            // 返回成功的
                            , success: function (data) {
                                if (data.status == "0") {
                                    layer.msg("禁用失败!!", {
                                        icon: 2,
                                        timeout: 2000
                                    }, function () {
                                        location.reload();
                                    });
                                } else {
                                    layer.msg("禁用成功!", {
                                        icon: 1,
                                        timeout: 2000
                                    }, function () {
                                        location.reload();
                                    });
                                }
                            }
                            // 超时
                            , timeout: function () {
                                layer.msg("请求超时!", {
                                    icon: 2,
                                    timeout: 2000
                                }, function () {
                                    location.reload();
                                });
                            }
                            // 错误
                            , error: function () {
                                layer.msg("发生错误!请与管理员联系!", {
                                    icon: 2,
                                    timeout: 2000
                                }, function () {
                                    location.reload();
                                });
                            }
                        });
                });
            }

            // 解禁事件
            if(layEvent == 'cancelDel'){
                layer.confirm("确定解除禁用吗?", function (index) {
                    $.ajax({
                        url: '<%=path%>/admin/qaBackUser_banBackUser.action'
                        , data: {"id": data[0],'status':1}
                        , dataType: 'json'
                        // 返回成功的
                        , success: function (data) {
                            if (data.status == "0") {
                                layer.msg("解除禁用失败!!", {
                                    icon: 2,
                                    timeout: 2000
                                }, function () {
                                    location.reload();
                                });
                            } else {
                                layer.msg("解除禁用成功!", {
                                    icon: 1,
                                    timeout: 2000
                                }, function () {
                                    location.reload();
                                });
                            }
                        }
                        // 超时
                        , timeout: function () {
                            layer.msg("请求超时!", {
                                icon: 2,
                                timeout: 2000
                            }, function () {
                                location.reload();
                            });
                        }
                        // 错误
                        , error: function () {
                            layer.msg("发生错误!请与管理员联系!", {
                                icon: 2,
                                timeout: 2000
                            }, function () {
                                location.reload();
                            });
                        }
                    });
                });
            }

            // 重置密码事件
            else if (layEvent === "alter"){
                layer.confirm("确定重置密码吗?", function () {
                    $.ajax({
                        url: '<%=path%>/admin/qaBackUser_resetPassWord.action'
                        , data: {"id": data[0]}
                        , dataType: 'json'
                        // 返回成功的
                        , success: function (data) {
                            if (data.status == "0") {
                                layer.msg("重置密码失败!!", {
                                    icon: 2,
                                    timeout: 2000
                                }, function () {
                                    location.reload();
                                });
                            } else {
                                layer.msg("重置密码成功!", {
                                    icon: 1,
                                    timeout: 2000
                                }, function () {
                                    location.reload();
                                });
                            }
                        }
                        // 超时
                        , timeout: function () {
                            layer.msg("请求超时!", {
                                icon: 2,
                                timeout: 2000
                            }, function () {
                                location.reload();
                            });
                        }
                        // 错误
                        , error: function () {
                            layer.msg("发生错误!请与管理员联系!", {
                                icon: 2,
                                timeout: 2000
                            }, function () {
                                location.reload();
                            });
                        }
                    });
                });
            }

            // 修改权限
            else if (layEvent === "changeTopic"){

                layer.open({
                    type: 2,
                    title: ['修改用户权限', 'text-align:center;'],
                    content: '<%=path%>/admin/qaBackUser_getTopicList.action?id='+data[0]+"&topic="+data[7],
                    area:['500px', '350px'],  //宽高
                    resize: false,		//是否允许拉伸
                    scrollbar: false,
                    end: function(){
                        location.reload();
                    }
                });
            }
        });



    });


</script>
</html>
