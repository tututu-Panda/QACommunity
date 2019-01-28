package com.qa.dao;

import com.qa.entity.QaComment;
import com.qa.entity.QaQuestion;

/**
 *
 */
public interface FrontQuestionDao {
    Object getQuestionById(int id);

    Object getCommentById(int id);

    boolean addReply(QaComment qaComment);
    boolean addQues(QaQuestion qaQuestion);

    boolean checkQuesByUser(int id, int ques_id);

    boolean editQues(QaQuestion qaQuestion);
}
