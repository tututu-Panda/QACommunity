package com.qa.dao;

import com.qa.entity.QaFrontUser;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface QaCommunityUserDao {

    public Map getCommunityUserList(String pages, String limit, String name, String[] rangeDate);

    boolean banComUser(List<Integer> ids,String comment);

    boolean updateUser(QaFrontUser user);

    QaFrontUser findById(int id);

    boolean banComUser(List<Integer> ids);
}
