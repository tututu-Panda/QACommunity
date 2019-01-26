package com.qa.action;

import com.opensymphony.xwork2.ModelDriven;
import com.qa.entity.QaBackUser;
import com.qa.entity.QaTopic;
import com.qa.service.QaTopicService;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("qaBackTopicAction")
@Scope("prototype")
public class qaBackTopicAction implements ModelDriven<QaTopic> {

    @Resource
    private QaTopicService QaTopicService;
    private QaTopic qaTopic;

    private JSONObject status;      // 返回的json

    Date date = new Date();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");





    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    private List list=null;
    @Override
    public QaTopic getModel() {
        this.qaTopic=new QaTopic();
        return  qaTopic;
    }



    public String FindAllTopic(){
        List<QaTopic> qaTopic = QaTopicService.findAllTopic();
        list = qaTopic;

        return "FindAllTopic";
    }


    public String DeleteTopic(){
        Map<String, Object> map = new HashMap<String, Object>();    // 定义map集合存如入返回json的集合

        boolean b= QaTopicService.DeleteTopic(id);
        if(b)
            map.put("status","1");
        else
            map.put("status","0");
        status = JSONObject.fromObject(map);
        return "DeleteSuccess";
    }


    public String UpdateTopic(){
        QaTopicService.UpdateTopic(qaTopic);
        return "UpdateSuccess";
    }


    public String AddTopic(){
        QaTopicService.AddTopic(qaTopic);
        return "AddSuccess";
    }

    public JSONObject getStatus() {
        return status;
    }

    public void setStatus(JSONObject status) {
        this.status = status;
    }
}
