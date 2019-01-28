package com.qa.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import com.qa.entity.QaComment;
import com.qa.entity.QaQuestion;
import com.qa.service.FrontQuestionService;
import com.qa.service.QaBackLabelService;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.aspectj.util.FileUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 前台社区问题控制器
 */

@Controller("FrontQuestionAction")
@Scope("prototype")
public class FrontQuestionAction extends BaseAction {

    @Resource
    private FrontQuestionService frontQuestionService;
    @Resource
    private QaBackLabelService qaBackLabelService;

    private QaComment qaComment;
    private QaQuestion qaQuestion;
    private String status;      // 返回的json
    private List topicList;  //二级评论中转

    private File file;          // 上传的图片
    private String fileFileName;              //得到文件的名称，写法是固定的
    private String fileContentType;           // 上传文件的类型
    private Map<Object,Object> map = new HashMap<>();    // 返回的json信息

    private JSONObject labelList;



    /**
     * 发布二级评论视图
     * @return
     */
    public String addTwoCommentView() {
        String commId = request.getParameter("pid");
        String quesId = request.getParameter("quesId");
        ValueStack valueStack = ServletActionContext.getContext().getValueStack();
        valueStack.set("pid",commId);
        valueStack.set("quesId",quesId);
        return "addTwoCommentView";
    }

    /**
     *
     * 描述： 回复问题
    */
    public String questionReply(){

        Map<String, Object> map = new HashMap<String, Object>();    // 定义map集合存如入返回json的集合

        status = "0";

        // 添加创建时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();
        Timestamp timestamp = new Timestamp(currentTime.getTime());

        // 获取用户信息
        Map session = ActionContext.getContext().getSession();
        Map user = (Map) session.get("frontUser");//获取session
        int id = (int) user.get("id");

        if(qaComment !=null){
            qaComment.setCreateDate(timestamp);
            qaComment.setCreateUser(id);

            System.out.println(qaComment.getPid());

            boolean b = frontQuestionService.addReply(qaComment);
            if(b){
                status = "1";
            }

        }

        map.put("status",status);
        JSONObject.fromObject(map);

        return "questionReply";
    }

    /**
     * 图片接口
     * @return
     */
    public String uploadImage() throws IOException {
//        Map<Object,Object> imageInfo =new HashMap<>();

        String[] str = { ".jpg", ".gif","png" }; // 限制图片后缀


        String realPath = ServletActionContext.getServletContext().getRealPath("/static/images");   // 图片存储路径

        // 随机生成图片文件名
        Random random = new Random(99999);
        int tempInt = random.nextInt();
        Date datenew = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddhhmmss");
        int last = fileFileName.lastIndexOf(".");
        String head = fileFileName.substring(0,last);
        String type = fileFileName.substring(last);
        fileFileName = head+sd.format(datenew)+tempInt+type;

        // 创建父文件夹
        if(file != null){
            File saveFile = new File(realPath, fileFileName);
            if(!saveFile.getParentFile().exists()){     // 如果图片文件夹不存在则创建新的多级文件夹
                saveFile.getParentFile().mkdir();
            }

            System.out.println(fileFileName);

            // 保存文件
            FileUtil.copyFile(file, saveFile);
            Map<String, Object> data1 = new HashMap<>();
            data1.put("src", request.getContextPath()+"/static/images/" + fileFileName);

            map.put("data",data1);
            map.put("code",0);

        }
        else{
            map.put("code",1);
            map.put("msg","上传失败");
        }

        JSONObject.fromObject(map);
        return "uploadImage";
    }

    /**
     *发布问题视图
     * @return
     */
    public String addQuestion() {
        topicList = qaBackLabelService.getTopicList();
        return "addQuestionView";
    }

    /**
     * ajax获取话题下的标签
     * @return
     */
    public String ajaxForLabel() {
        String topicId= request.getParameter("topicId");
        int topicIdInt = Integer.parseInt(topicId);
        Map map = qaBackLabelService.getLabelToTopic(topicIdInt);
        map.put("status",0);
        labelList = JSONObject.fromObject(map);
        return "ajaxLabel";
    }

    /**
     * 添加问题的提交
     * @return
     */
    public String addQuesHandle() {

        Map<String, Object> map = new HashMap<String, Object>();    // 定义map集合存如入返回json的集合
        int b = 0;  // 返回的主键信息
        status = "0";

        // 添加创建时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();
        Timestamp timestamp = new Timestamp(currentTime.getTime());

        // 获取用户信息
        Map session = ActionContext.getContext().getSession();
        Map user = (Map) session.get("frontUser");//获取session
        int id = (int) user.get("id");

        if(qaQuestion !=null){
            qaQuestion.setCreateDate(timestamp);
            qaQuestion.setCreateUser(id);
            System.out.println(qaQuestion.getLabelIds() );

            b = frontQuestionService.addQues(qaQuestion);
            status = String.valueOf(b);

        }

        map.put("status",status);
        JSONObject.fromObject(map);
        return "addQuesResult";
    }


    /**
     * 编辑问题
     * @return
     */
    public String editQuestion(){

        // 获取用户信息
        Map session = ActionContext.getContext().getSession();
        Map user = (Map) session.get("frontUser");//获取session
        int id = (int) user.get("id");


        // 获取问题相关信息
        int ques_id = Integer.parseInt(request.getParameter("q_id"));


        // 检查用户id与问题id 是否匹配
        boolean b = frontQuestionService.checkQuesByUser(id,ques_id);
        if(b){
            // 获取问题详情
             List ques = (List) frontQuestionService.getQuestionById(ques_id);
             // 获取话题标签
            List topic_list = qaBackLabelService.getTopicList();
            ValueStack vs = ServletActionContext.getContext().getValueStack();
            vs.set("quesInfo",ques.get(0));
            vs.set("topicList",topic_list);
            return "editQuestionView";
        }
        return "error";

    }


    public String editQuesHandle(){
        Map<String, Object> map = new HashMap<>();    // 定义map集合存如入返回json的集合
        status = "0";

        // 获取用户信息
        Map session = ActionContext.getContext().getSession();
        Map user = (Map) session.get("frontUser");//获取session
        int id = (int) user.get("id");

        if(qaQuestion !=null){
            System.out.println(qaQuestion.getLabelIds() );
            // 前台传过的值为String,无法更新,因此需要手动修改
            int q_id = Integer.parseInt(request.getParameter("q_id"));
            qaQuestion.setqId(q_id);
            // 由于更新操作必须传入非空值,因此需要手动设置
            qaQuestion.setCreateUser(id);
            boolean b = frontQuestionService.editQues(qaQuestion);
            if(b){
                status = "1";
            }
        }

        map.put("status",status);
        JSONObject.fromObject(map);

        return "editQuesResult";
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public QaComment getQaComment() {
        return qaComment;
    }

    public void setQaComment(QaComment qaComment) {
        this.qaComment = qaComment;
    }

    public List getTopicList() {
        return topicList;
    }

    public void setTopicList(List topicList) {
        this.topicList = topicList;
    }
    public QaQuestion getQaQuestion() {
        return qaQuestion;
    }

    public void setQaQuestion(QaQuestion qaQuestion) {
        this.qaQuestion = qaQuestion;
    }

    public JSONObject getLabelList() {
        return labelList;
    }

    public void setLabelList(JSONObject labelList) {
        this.labelList = labelList;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Map<Object, Object> getMap() {
        return map;
    }

    public void setMap(Map<Object, Object> map) {
        this.map = map;
    }
}
