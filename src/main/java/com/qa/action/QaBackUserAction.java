package com.qa.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.ValueStack;
import com.qa.entity.QaBackUser;
import com.qa.entity.QaTopic;
import com.qa.service.QaBackUserService;
import com.qa.service.QaTopicService;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.xml.ws.Action;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;

/**
 * 后台用户登录
 */

@Controller("QaBackUserAction")
@Scope("prototype")
public class QaBackUserAction extends BaseAction {
    @Resource
    private QaBackUserService qaBackUserService;
    @Resource
    private com.qa.service.QaTopicService QaTopicService;
    private QaBackUser qaBackUser;
    private String msg;  //设置返回信息提示

    private Map data = null;          //map型值栈

    //检查登录
    public String baLogin()  throws NoSuchAlgorithmException, UnsupportedEncodingException{
        checkLogin();
        return SUCCESS;
    }


    //登录的具体实现
    private void checkLogin()  throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //定义map集合存取返回信息信息
        Map<String, Object> map = new HashMap<String, Object>();
        String status = null;       //状态标识代码
        String Ps = qaBackUser.getPassword();
        String md5Ps = EncoderByMd5(Ps);  //调用加密算法
        qaBackUser.setPassword(md5Ps);

        String account = qaBackUser.getAccount();

        //检查登录信息是否正确
        if(!qaBackUserService.checkStatus(account)){
            status = "2";       // 禁用状态码
        }
        else{
            qaBackUser = qaBackUserService.login(qaBackUser);
            if(qaBackUser != null) {
                Map session = ActionContext.getContext().getSession();

                session.put("qaBackUser", qaBackUser);//存入session
                session.put("user",qaBackUser.getAccount());
                status = "0";  //成功状态码

            }
            else {
                //return ERROR;
                status = "1";      //失败状态码

            }

        }
        map.put("status", status);
        msg = JSONObject.fromObject(map).toString();        //转化为json字符串
        //System.out.println(msg);
    }

    /**
     * 验证登录成功进入控制面板主页
     * @return
     */
    public String dashboard() {
        return "dashboard";
    }

    /**
     * 退出登录删除时session
     * @return
     */
    public String logout() {
        Map session = ActionContext.getContext().getSession();
        if(session.get("qaBackUser") != null) {
            session.remove("qaBackUser");
        }
        return "userLogin";
    }


    /**
     * 后台用户列表
     * @return
     */
    public String backUserList(){
        return "backUserList";
    }


    /**
     * 获取后台用户信息
     * @return
     */
    public String getBackUserList(){
        String pages;     // 当前页数
        String limit;     // 分页数量
        String name = "";     // 用户名称
        Map userMap;
        Map<String, Object> map = new HashMap<>();

        Map<String, Object> params = (Map) ActionContext.getContext().getParameters();
        pages = ((String []) params.get("page"))[0];
        limit = ((String[]) params.get("limit"))[0];

        // 如果包含名字
        if(params.containsKey("name")){
            name = ((String[]) params.get("name"))[0];
        }

        // 如果参数错误,则返回
        if (pages.equals("") || limit.equals("")){
            map.put("code","1");
            map.put("msg","参数传入错误!");
        }else{
            userMap = qaBackUserService.getBackUserList(pages,limit,name);
            map.put("code","0");
            map.put("msg","");
            map.put("count",userMap.get("count"));
            map.put("data",userMap.get("list"));
        }


        data = JSONObject.fromObject(map);
        return "getBackUserList";
    }


    /**
     * 禁用管理员
     * @return
     */
    public String banBackUser(){

        Map<String, Object> map = new HashMap<String, Object>();    // 定义map集合存如入返回json的集合
        String[] list;                 // 请求的id集合
        boolean istrue=false;

        // 获得请求参数
        Map<String, Object> params = (Map) ActionContext.getContext().getParameters();
        list = ((String []) params.get("id"));
        String[] status = (String[]) params.get("status");


        int id = Integer.parseInt(list[0]);

        istrue = qaBackUserService.banBackUser(id,status[0]);

        if(istrue){
            map.put("status","1");
        }else{
            map.put("status","0");
        }
        data = JSONObject.fromObject(map);

        return "banBackUser";
    }


    /**
     * 重置用户密码
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public String resetPassWord() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Map<String, Object> map = new HashMap<String, Object>();    // 定义map集合存如入返回json的集合
        boolean istrue=false;

        // 获得请求参数
        Map<String, Object> params = (Map) ActionContext.getContext().getParameters();

        String[] list = ((String []) params.get("id"));
        int id = Integer.parseInt(list[0]);

        // 重置密码
        String password = super.EncoderByMd5("123456");
        istrue = qaBackUserService.resetPassWord(id,password);

        if(istrue){
            map.put("status","1");
        }else{
            map.put("status","0");
        }
        data = JSONObject.fromObject(map);


        return "resetPassWord";
    }


    /**
     * 获取话题列表
     * @return
     */
    public String getTopicList(){

        // 获得请求参数
        Map<String, Object> params = (Map) ActionContext.getContext().getParameters();

        String[] userList = ((String []) params.get("id"));
        String[] topicList = ((String []) params.get("topic"));


        List<QaTopic> qaTopic = QaTopicService.findAllTopic();
        ValueStack s = ServletActionContext.getContext().getValueStack();
        s.set("list",qaTopic);
        s.set("userId",userList[0]);
        s.set("topicId",topicList[0]);
        return "getTopicList";
    }

    /**
     * 更新用户权限
     * @return
     */
    public String updateBackUserTopic(){
        Map<String, Object> map = new HashMap<String, Object>();    // 定义map集合存如入返回json的集合
        // 获得请求参数
        Map<String, Object> params = (Map) ActionContext.getContext().getParameters();

        String[] userList = ((String []) params.get("id"));
        String[] topicList = ((String []) params.get("topic"));

        int userId = Integer.parseInt(userList[0]);
        int topicId = Integer.parseInt(topicList[0]);

        boolean b = qaBackUserService.updateBackUserTopic(userId, topicId);
        if(b){
            map.put("status","1");
        }else{
            map.put("status","0");
        }
        data = JSONObject.fromObject(map);
        return "updateBackUserTopic";
    }


    /**
     * 新增后台用户管理员
     * @return
     */
    public String addBackUser(){
        List<QaTopic> qaTopic = QaTopicService.findAllTopic();
        ValueStack s = ServletActionContext.getContext().getValueStack();
        s.set("list",qaTopic);
        return  "addBackUser";
    }


    /**
     * 检查用户是否存在
     * @return
     */
    public String checkAccount(){

        Map<String, Object> map = new HashMap<String, Object>();    // 定义map集合存如入返回json的集合
        // 获得请求参数
        Map<String, Object> params = (Map) ActionContext.getContext().getParameters();

        String[] accounts = ((String []) params.get("account"));

        boolean b = qaBackUserService.checkAccount(accounts[0]);

        if(b){
            map.put("status","1");
        }else{
            map.put("status","0");
        }
        data = JSONObject.fromObject(map);

        return "checkAccount";
    }


    public String addBackUserForm() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Map<String, Object> map = new HashMap<String, Object>();    // 定义map集合存如入返回json的集合
        // 获得请求参数
        Map<String, Object> params = (Map) ActionContext.getContext().getParameters();

        String defaultPhoto = "static/images/default-avatar.png";

        qaBackUser.setPhoto(defaultPhoto);
        qaBackUser.setRole(1);
        qaBackUser.setStatus(1);

        // 密码加密
        qaBackUser.setPassword(super.EncoderByMd5(qaBackUser.getPassword()));

        boolean b = qaBackUserService.addQbUser(qaBackUser);

        if(b){
            map.put("status","1");
        }else{
            map.put("status","0");
        }
        data = JSONObject.fromObject(map);

        return "addBackUserForm";
    }


    public QaBackUser getQaBackUser() {
        return qaBackUser;
    }

    public void setQaBackUser(QaBackUser qaBackUser) {
        this.qaBackUser = qaBackUser;
    }

    //对应json获取msg
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
