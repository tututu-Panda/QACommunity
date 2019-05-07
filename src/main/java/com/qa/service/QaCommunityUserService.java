package com.qa.service;

import com.qa.entity.QaFrontUser;

import java.util.List;
import java.util.Map;

/**
 * Created by InterlliJ IDEA.
 * User:3to
 * Date:17-12-23
 * Time:下午1:34
 */

public interface QaCommunityUserService {

    public Map getComUserList(String pages, String limit, String name, String[] rangeDate);

    public boolean banLog(List<Integer> ids,String comment);

    public boolean updateUser(int user,String password);

    QaFrontUser findById(int id);

    boolean cancelBanLog(List<Integer> ids);
}
