package com.qa.action;

import com.opensymphony.xwork2.ActionContext;
import com.qa.entity.QaBackUser;
import com.qa.service.DashBoardService;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by 3tu on 2019/5/8.
 */

@Controller("DashBoardAction")
@Scope("prototype")
public class DashBoardAction extends BaseAction{

        @Resource
        private DashBoardService dashBoardService;

    private Map List = null;       // 结果集合

    /**
     * 获取用户信息
     * 1. 新增用户（昨日到现在新增）
     * 2. 总用户数
     */
    public String getUserInfo(){

        Map<String, Object> map;    // 定义map集合存如入返回json的集合

        map = dashBoardService.getUserInfo();

        List = JSONObject.fromObject(map);
        return "getAllUser";
    }


    /**
     * 获取文章信息
     * 1. 待审核文章数
     * 2. 总的文章个数
     */
    public String getArticleInfo(){
        Map<String, Object> map;    // 定义map集合存如入返回json的集合

        // 根据用户角色，获取对应话题相关问题
        Map session = ActionContext.getContext().getSession();
        QaBackUser backUser = (QaBackUser) session.get("qaBackUser");
        int role = backUser.getRole();
        int topic = 0;
        if(role == 1){
            topic = backUser.getTopic();
        }
        map = dashBoardService.getArticleInfo(topic);

        List = JSONObject.fromObject(map);
        return "getAllArticle";
    }

    /**
     * 获取最新文章信息
     */
    public String getLatestArticle(){
        Map<String, Object> map;    // 定义map集合存如入返回json的集合

        map = dashBoardService.getLatestArticle();

        List = JSONObject.fromObject(map);
        return "getLatestArticleList";
    }


    public Map getList() {
        return List;
    }

    public void setList(Map list) {
        this.List = list;
    }
}
