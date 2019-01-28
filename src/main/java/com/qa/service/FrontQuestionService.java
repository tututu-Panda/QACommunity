package com.qa.service;

import com.qa.entity.QaComment;
import com.qa.entity.QaQuestion;

/**
 * Created by InterlliJ IDEA.
 * User:3to
 * Date:18-1-2
 * Time:下午9:34
 */
public interface FrontQuestionService {
    Object getQuestionById(int id);

    Object getCommentById(int id);

    boolean addReply(QaComment qaComment);

    int addQues(QaQuestion qaQuestion);

    boolean editQues(QaQuestion qaQuestion);

    boolean checkQuesByUser(int id, int ques_id);
}
