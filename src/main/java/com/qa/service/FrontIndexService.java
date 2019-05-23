package com.qa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *Create by 3tu on 2017/12/28
 */
public interface FrontIndexService {

    public Map getQuesIndex(String page, String orderType, int topic);

    public Map getTopicIndex();

    public Map getTheQuesInfo(int quesId);

    public Map getTheOneComm(int q_id);

    public Map getTheTwoComm(int pq_id);

    List getReplyRank();

    List getRandomQues(ArrayList<Integer>... list);

    Map getSearchQues(String page, String orderType, int topic, String searchValue);
}
