package com.qa.service;

import com.qa.entity.QaBackUser;

import java.util.List;
import java.util.Map;

/**
 *Create by 3tu on 2018/11/30
 */
public interface QaBackUserService {

    public boolean addQbUser(QaBackUser qbUser);

    public QaBackUser login(QaBackUser qbUser);

    public List getAllQbUser();

    public QaBackUser getQbUserById(int id);

    public boolean updateQbUser(QaBackUser qbUser);

    public boolean deleteQbUser(int id);

    Map getBackUserList(String pages, String limit, String name);

    boolean saveUserPhoto(int id, String s);

    boolean banBackUser(int id, String status);

    boolean resetPassWord(int id, String password);

    boolean updateBackUserTopic(int userId, int topicId);

    boolean checkAccount(String account);

    boolean checkStatus(String account);
}
