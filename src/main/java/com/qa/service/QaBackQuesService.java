package com.qa.service;

import com.qa.entity.QaQuestion;

import java.util.List;
import java.util.Map;

/**
 *Create by 3tu on 2018/12/13
 */
public interface QaBackQuesService {
    public boolean addQues(QaQuestion qaQuestion);

    public boolean update(QaQuestion qaQuestion);

    public boolean deleteQues(List<Integer> c_ids);

    public QaQuestion getQaQuestion(int l_id);

    public Map getQuestionList(String pages, String limit, String[] rangeDate,int role);

    public Map getTheQuestion(int l_id);

    public Map getBrowseForDate(int qId, String[] time);

    public Map getTheComment(int q_id,String page);


    public Map getTheComment_two(int pq_id);

    public boolean deleteComm(int c_id);

    Map getCheckQuestionList(String pages, String limit, String[] rangeDate, int check,int topic);


    boolean checkQues(List<Integer> ids, int check);
}
