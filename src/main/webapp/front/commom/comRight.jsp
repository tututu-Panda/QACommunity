<%--
  Created by IntelliJ IDEA.
  User: 3tu
  Date: 2019/1/26
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<div class="layui-col-md4">

    <div class="fly-panel">
        <h3 class="fly-panel-title">随便看看</h3>
        <ul class="fly-panel-main fly-list-static" id="ranQues">
            <%--js添加随机帖子--%>
        </ul>
    </div>


    <div class="fly-panel fly-rank fly-rank-reply" id="LAY_replyRank">
        <h3 class="fly-panel-title">回贴周榜</h3>
        <dl id="rank">
            <%--js接口添加用户--%>
        </dl>
    </div>
    <div class="fly-panel">
        <div class="fly-panel-title">
            推荐
        </div>
        <div class="fly-panel-main">
            <a href="http://layim.layui.com/?from=fly" target="_blank" class="fly-zanzhu" time-limit="2017.09.25-2099.01.01" style="background-color: #5FB878;">QAIM 3.0 - QA 旗舰之作</a>
        </div>
    </div>

    <div class="fly-panel fly-link">
        <h3 class="fly-panel-title">友情链接</h3>
        <dl class="fly-panel-main">
            <dd><a href="http://www.layui.com/" target="_blank">layui</a><dd>
            <dd><a href="http://layim.layui.com/" target="_blank">WebIM</a><dd>
            <dd><a href="http://layer.layui.com/" target="_blank">layer</a><dd>
            <dd><a href="http://www.layui.com/laydate/" target="_blank">layDate</a><dd>
        </dl>
    </div>

</div>

<script src="<%=basePath %>/static/plugins/js/jquery-3.1.1.min.js" type="text/javascript"></script>
<script>
    window.onload=function(){

        // 回帖榜单
        $.ajax({
            url: "<%=path%>/front/frontIndex_weekRank.action",
            type: "POST",
            success: function (data2) {
                if (data2 != null) {
                    var html ;
                   // 拼接html
                    $.each(data2, function(index, item) {
//                        console.log(item[0]);
                            var user = "<%=path%>/frontUser/FrontUser_userHome.action?id="+item[0];
                            var name = item[1];
                            var image = "<%=path%>/"+item[2];
                            var num = item[3];
//                        <dd>
//                        <a href="">
//                            <img src="https://tva1.sinaimg.cn/crop.0.0.118.118.180/5db11ff4gw1e77d3nqrv8j203b03cweg.jpg"><cite>贤心</cite><i>106次回答</i>
//                        </a>
//                        </dd>
                            html = '<dd>'+
                                    '<a href="'+user+'" >'+
                                    '<image src = "'+image+'" >' +
                                '<cite> "'+name+'" </cite>' +
                                '<i> "'+num+'"次回答 </i> '+
                                '</a></dd>';
                            $("#rank").append(html);
                    });
                } else {
                    layer.msg("周榜数据接口出错!", {time: 1000, icon: 2});
                }
            },
            error: function () {
                layer.msg('请求服务器超时', {time: 1000, icon: 2},function () {location.reload();});
            }
        });

        // 随机获取帖子
        $.ajax({
            url: "<%=path%>/front/frontIndex_randomQuestion.action",
            type: "POST",
            success: function (data2) {
                if (data2 != null) {
                    var html ;
                    // 拼接html
                    $.each(data2, function(index, item) {
//                        /front/frontIndex_getTheQuestion?quesId=40
                        var ques = "<%=path%>/front/frontIndex_getTheQuestion?quesId="+item[0];
                        var title = item[1];
//                        <li>
//                        <a href="http://fly.layui.com/jie/5366/" target="_blank">
//                            Laravel社区，中文网官方社区
//                        </a>
//                        </li>
                        html = '<li>'+
                            '<a href="'+ques+'"  target="_blank">'+title+
                            '</a></li>';
                        $("#ranQues").append(html);
                    });
                } else {
                    layer.msg("随机帖子数据接口出错!", {time: 1000, icon: 2});
                }
            },
            error: function () {
                layer.msg('请求服务器超时', {time: 1000, icon: 2},function () {location.reload();});
            }
        });
    }
</script>