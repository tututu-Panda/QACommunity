package com.qa.service;

import com.qa.entity.QaBackUser;

import java.util.List;

/**
 *Create by 3tu on 2017/11/30
 */
public interface QaBackUserService {

    public boolean addQbUser(QaBackUser qbUser);

    public QaBackUser login(QaBackUser qbUser);

    public List getAllQbUser();

    public QaBackUser getQbUserById(int id);

    public boolean updateQbUser(QaBackUser qbUser);

    public boolean deleteQbUser(int id);

    boolean saveUserPhoto(int id, String s);
}
