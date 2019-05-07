package com.qa.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.qa.entity.BackQuestion;
import com.qa.entity.QaQuestion;
import com.qa.service.QaBackQuesService;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *Create by 3tu on 2018/5/4
 */
@Controller("QaBackQuesAction")
@Scope("prototype") //作用域，prototype指每次请求都创建一个实例
public class QaBackQuesAction extends BaseAction implements ModelDriven<QaQuestion>{
    @Resource
    private QaBackQuesService qaBackQuesService;
    private QaQuestion qaQuestion = new QaQuestion();
    private List qaQuesList = null;  //设置问题列表值栈
    private JSONObject quesList = null;   //设置返回的json类型的问题列表
    private JSONObject status = null;    //设置返回的json类型的标识
    private JSONObject quesComment = null;      //问题评论json
    private Map singleInfo = null;          //map型值栈
    private Map qaTwoComment = null;  //设置二级评论的值栈




    /**
     * 问题列表值栈的get方法
     * @return
     */
    public List getQaQuesList() {
        return qaQuesList;
    }


    /**
     * 实现ModelDriven需要实现的方法
     * @return
     */
    @Override
    public QaQuestion getModel() {
        return this.qaQuestion;
    }


    /**
     * 获取问题列表
     */
    public String allQuestionView() {
        return "ques_list_view";
    }
    public String getAllQuestion() {
        //初始化map集合
        Map<String, Object> map = new HashMap<String, Object>();
        Map receiveMap = new HashMap<String, Object>();
        String pages;                     // 请求页数
        String limit;                     //每页限制
        String[] rangeDate = new String[4];                         // 请求时间范围

        // 获得请求参数
        Map<String, Object> params = (Map) ActionContext.getContext().getParameters();
        pages = ((String []) params.get("page"))[0];
        limit = ((String[]) params.get("limit"))[0];
        rangeDate[0] = ((String[]) params.get("startDate"))[0];       // 请求时间范围

        // 如果包含结尾时间
        if(params.containsKey("endDate")){
            rangeDate[1] = ((String[]) params.get("endDate"))[0];
        }

        if(pages.equals("") || limit.equals("")) {
            map.put("code","1");
            map.put("msg","参数返回错误!");
        }else {
            receiveMap = qaBackQuesService.getQuestionList(pages,limit, rangeDate);
            map.put("code","0");  //成功
            map.put("msg","");
            map.put("count",receiveMap.get("count"));
            map.put("data",receiveMap.get("quesLists"));
        }
//        List ll = (List) map.get("data");
//        for(Object bt: ll) {
//            System.out.println(bt);
//        }
        //转化json形式数据
        quesList = JSONObject.fromObject(map);
        return "question_list";

    }

    /**
     * 根据问题id获取某个问题详细信息，返回值栈形式
     * @return  值栈
     */
    public String getTheQues() {

        // 获得请求参数:问题id
        String qIdTemp = request.getParameter("qId");
        int qId = Integer.parseInt(qIdTemp);        //转化为int形
        singleInfo = qaBackQuesService.getTheQuestion(qId);
        return "single_question";
    }



    /**
     *获取一级评论：即该问题下的直接评论
     * @return
     */
    public String getComment() {
        String qId_temp = request.getParameter("qId");
        String page = request.getParameter("page");     //页码
        int qId = Integer.parseInt(qId_temp);
        Map map = qaBackQuesService.getTheComment(qId,page);  //接受返回map集合
        map.put("status","0");
        quesComment = JSONObject.fromObject(map);
        return "comment_one";
    }

    /**
     * 获取某个评论下的二级评论
     * @return
     */
    public String getTwoComment() {
        String pqId_temp = request.getParameter("pqId");
        int pqId = Integer.parseInt(pqId_temp);
        qaTwoComment = qaBackQuesService.getTheComment_two(pqId);  //接受返回map集合

        return "comment_two";
    }

    /**
     * 删除问题
     * @return
     */
    public String deleteQues() {
        Map<String, Object> map = new HashMap<>();
        String[] qIds_temps;
        Map<String, Object> params = (Map) ActionContext.getContext().getParameters();
        qIds_temps = ((String []) params.get("qId"));
        // 转换为List集合
        List<Integer> ids = new ArrayList<Integer>();
        for (int j = 0; j < qIds_temps.length; j++) {
            ids.add(Integer.parseInt(qIds_temps[j]));
        }
        if(qaBackQuesService.deleteQues(ids)) {
            map.put("status", "0");
        }else {
            map.put("status", "1");
        }
        status = JSONObject.fromObject(map);
        return "deleteQues";
    }

    /**
     * 删除评论（包括一级和二级）
     * @return
     */
    public String deleteComment() {
        Map<String, Object> map = new HashMap<>();
        String cId_temp = request.getParameter("cId");
        int cId = Integer.parseInt(cId_temp);
        if(qaBackQuesService.deleteComm(cId)) {
            map.put("status", "0");
        }else {
            map.put("status", "1");
        }
        status = JSONObject.fromObject(map);
        return "deleteComm";
    }


    /**
     * 内容审核页面
     * @return
     */
    public String checkContent(){
        return "checkContent";
    }


    /**
     * 审核内容列表
     * @return
     */
    public String checkContentList(){
            //初始化map集合
            Map<String, Object> map = new HashMap<String, Object>();
            Map receiveMap = new HashMap<String, Object>();
            String pages;                     // 请求页数
            String limit;                     //每页限制
            int check = 1;                  // 默认为查找待审核问题
            String[] rangeDate = new String[4];                         // 请求时间范围

            // 获得请求参数
            Map<String, Object> params = (Map) ActionContext.getContext().getParameters();
            pages = ((String []) params.get("page"))[0];
            limit = ((String[]) params.get("limit"))[0];
            rangeDate[0] = ((String[]) params.get("startDate"))[0];       // 请求时间范围

            // 如果包含结尾时间
            if(params.containsKey("endDate")){
                rangeDate[1] = ((String[]) params.get("endDate"))[0];
            }

            // 包含筛选信息
            if(params.containsKey("check")){
                String c= ((String[]) params.get("check"))[0];
                check = Integer.valueOf(c);
            }

            if(pages.equals("") || limit.equals("")) {
                map.put("code","1");
                map.put("msg","参数返回错误!");
            }else {
                receiveMap = qaBackQuesService.getCheckQuestionList(pages,limit, rangeDate,check);
                map.put("code","0");  //成功
                map.put("msg","");
                map.put("count",receiveMap.get("count"));
                map.put("data",receiveMap.get("quesLists"));
            }

            //转化json形式数据
            quesList = JSONObject.fromObject(map);
            return "check_question_list";

    }


    /**
     * 不通过所选取的内容
     * @return
     */
    public String checkQues(){
        Map<String, Object> map = new HashMap<>();
        String[] qIds_temps;
        Map<String, Object> params = (Map) ActionContext.getContext().getParameters();

        qIds_temps = ((String []) params.get("qId"));
        // 转换为List集合
        List<Integer> ids = new ArrayList<Integer>();
        int check = Integer.valueOf(((String[]) params.get("check"))[0]);

        for (int j = 0; j < qIds_temps.length; j++) {
            ids.add(Integer.parseInt(qIds_temps[j]));
        }
        if(qaBackQuesService.checkQues(ids,check)) {
            map.put("status", "0");
        }else {
            map.put("status", "1");
        }
        status = JSONObject.fromObject(map);

        return "check";
    }

//    /**
//     * 通过所选取的内容
//     * @return
//     */
//    public String passQues(){
//        Map<String, Object> map = new HashMap<>();
//        String[] qIds_temps;
//        Map<String, Object> params = (Map) ActionContext.getContext().getParameters();
//        qIds_temps = ((String []) params.get("qId"));
//        // 转换为List集合
//        List<Integer> ids = new ArrayList<Integer>();
//        for (int j = 0; j < qIds_temps.length; j++) {
//            ids.add(Integer.parseInt(qIds_temps[j]));
//        }
//        if(qaBackQuesService.passQues(ids)) {
//            map.put("status", "0");
//        }else {
//            map.put("status", "1");
//        }
//        status = JSONObject.fromObject(map);
//        return "pass";
//    }





    /**
     * 以下是一些变量的实现方法：如值栈或json数据
     * @return
     */
    public JSONObject getQuesList() {
        return quesList;
    }

    public void setQuesList(JSONObject quesList) {
        this.quesList = quesList;
    }
    public JSONObject getStatus() {
        return status;
    }

    public void setStatus(JSONObject status) {
        this.status = status;
    }
    public Map getSingleInfo() {
        return singleInfo;
    }

    public void setSingleInfo(Map singleInfo) {
        this.singleInfo = singleInfo;
    }
    public JSONObject getQuesComment() {
        return quesComment;
    }

    public void setQuesComment(JSONObject quesComment) {
        this.quesComment = quesComment;
    }
    public Map getQaTwoComment() {
        return qaTwoComment;
    }

    public void setQaTwoComment(Map qaTwoComment) {
        this.qaTwoComment = qaTwoComment;
    }
}
