package com.qa.dao;


import com.qa.entity.QaBackUser;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface QaBackUserDao {

    /**
     * 添加并保存后台用户
     * @param qbUser
     */
    public void add(QaBackUser qbUser);

    /**
     * 根据用户id获取用户的信息
     * @param id
     * @return
     */
    public QaBackUser getQaUser(int id);

    /**
     * 更新用户的信息
     * @param qaUser
     */
    public void update(QaBackUser qaUser);

    /**
     * 根据用户的id删除用户的信息
     * @param id
     */
    public void delete(int id);

    /**
     * 后台用户的登录操作
     * @param qaUser
     * @return
     */
    public QaBackUser login(QaBackUser qaUser);

    /**
     * 获取所有的后台用户列表
     * @return
     */
    public List getQaUserList();

    boolean saveUserPhoto(int id, String realPath);

    Map getBackUserList(String pages, String limit, String name);

    boolean banBackUser(int id, String status);

    boolean resetPassWord(int id, String password);

    boolean updateBackUserTopic(int userId, int topicId);

    boolean checkAccount(String account);

    boolean checkStatus(String status);
}
