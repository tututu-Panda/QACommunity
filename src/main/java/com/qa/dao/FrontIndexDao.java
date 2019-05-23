package com.qa.dao;

/**
 *
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 前台Dao，未登录时
 */
public interface FrontIndexDao {

    /**
     * 登录首页，获取问题列表
     * @return
     */
    public Map getQuesIndex(int page, int orderType, int topic);

    /**
     * 获取话题列表
     * @return
     */
    public Map getTopicIndex();

    /**
     * 获取该问题的详情
     * @return
     */
    public Map getTheQuesInfo(int quesId);

    public String[] LabelList(String labelStr);


    public Map getTopicList();

    List getReplyRank();

    List gerRandomQues(ArrayList<Integer>... list);

    int getViews(int ques_id);

    void updateViews(int ques_id, int views);

    Map getSearchQues(int page, int orderType, int topic, String search);
}
