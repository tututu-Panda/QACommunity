package com.qa.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.qa.entity.QaBackUser;
import com.qa.service.BackInfoService;
import com.qa.service.QaBackUserService;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;
import org.aspectj.util.FileUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 后台用户基本信息
 */
@Controller("BackInfoAction")
@Scope("prototype")
public class BackInfoAction extends BaseAction implements ModelDriven<QaBackUser>{




    @Resource
    private BackInfoService BackInfoService;
    @Resource
    private QaBackUserService BackUserService;
    private JSONObject status;      // 返回的json

    private List list=null;
    private QaBackUser qaBackUser;
    private String username;
    private System account;

    private File file;
    private String fileFileName;              //得到文件的名称，写法是固定的
    private String fileContentType;           // 上传文件的类型


    public List getList() {
        return list;
    }

    @Override
    public QaBackUser getModel() {
        this.qaBackUser=new QaBackUser();
        return  qaBackUser;
    }


    public String FindBackInfo(){

    List<QaBackUser> QaBackUser = BackInfoService.findByname(username);
    list = QaBackUser;

    return "FindBackInfo";

    }

    /**
     * 更新用户基本信息
     * @return
     */
    public String updateInfo()  {
    BackInfoService.updateInfo(qaBackUser);
        return "updateInfoSuccess";


    }

    /**
     * 更新用户密码
     * @return
     */
    public String updatePassword() throws NoSuchAlgorithmException, UnsupportedEncodingException{
        String Ps = qaBackUser.getPassword();
        String md5Ps = EncoderByMd5(Ps);  //调用加密算法
        qaBackUser.setPassword(md5Ps);
        BackInfoService.updatePsInfo(qaBackUser);
        return "updateInfoPssuccess";
    }

    /**
     * 更新用户头像
     * @return
     */
    public String uploadPhoto() throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();    // 定义map集合存如入返回json的集合
        int id;

        // 获取id
        Map session = ActionContext.getContext().getSession();
       QaBackUser user = (QaBackUser) session.get("qaBackUser");//获取session
        id = user.getId();
//        System.out.println(id);

        String[] str = { ".jpg", ".gif","png" }; // 限制图片后缀
        if(file == null || file.length()>51200){        // 图片大小不能超过50kb
            map.put("status","0");
        }else{
            // 遍历后缀
            for(String s:str) {
                if (fileFileName.endsWith(s)) {
                    String realPath = ServletActionContext.getServletContext().getRealPath("/static/images");   // 图片存储路径

                    // 随机生成图片文件名
                    Random random = new Random(99999);
                    int tempInt = random.nextInt();
                    Date datenew = new Date();
                    SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddhhmmss");
                    int last = fileFileName.lastIndexOf(".");
                    String head = fileFileName.substring(0, last);
                    String type = fileFileName.substring(last);
                    fileFileName = head + sd.format(datenew) + tempInt + type;

                    // 创建父文件夹
                    File saveFile = new File(realPath, fileFileName);
                    if (!saveFile.getParentFile().exists()) {     // 如果图片文件夹不存在则创建新的多级文件夹
                        saveFile.getParentFile().mkdir();
                    }

                    System.out.println(fileFileName);

                    // 保存文件
                    FileUtil.copyFile(file, saveFile);
                    boolean b = BackUserService.saveUserPhoto(id, "/static/images/" + fileFileName);
                    user.setPhoto("/static/images/" + fileFileName);
                    if (b) {
                        map.put("status","1");
                    } else {
                        map.put("status","0");
                    }

                }
            }
        }

//        System.out.println("status::"+status);

        status = JSONObject.fromObject(map);
        return "uploadPhoto";
    }


    public System getAccount() {
        return account;
    }

    public void setAccount(System account) {
        this.account = account;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public JSONObject getStatus() {
        return status;
    }

    public void setStatus(JSONObject status) {
        this.status = status;
    }


    public QaBackUserService getBackUserService() {
        return BackUserService;
    }

    public void setBackUserService(QaBackUserService backUserService) {
        BackUserService = backUserService;
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
}
