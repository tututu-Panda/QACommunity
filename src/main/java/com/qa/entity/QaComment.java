package com.qa.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by 3tu on 2019/1/20.
 */
@Entity
@Table(name = "qa_comment", schema = "qacommunity", catalog = "")
public class QaComment {
    private int cId;
    private String content;
    private int questionId;
    private Integer pid;
    private Timestamp createDate;
    private int createUser;

    @Id
    @Column(name = "c_id", nullable = false)
    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    @Basic
    @Column(name = "content", nullable = false, length = -1)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "question_id", nullable = false)
    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    @Basic
    @Column(name = "pid", nullable = true)
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "create_date", nullable = false)
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "create_user", nullable = false)
    public int getCreateUser() {
        return createUser;
    }

    public void setCreateUser(int createUser) {
        this.createUser = createUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QaComment qaComment = (QaComment) o;

        if (cId != qaComment.cId) return false;
        if (questionId != qaComment.questionId) return false;
        if (createUser != qaComment.createUser) return false;
        if (content != null ? !content.equals(qaComment.content) : qaComment.content != null) return false;
        if (pid != null ? !pid.equals(qaComment.pid) : qaComment.pid != null) return false;
        if (createDate != null ? !createDate.equals(qaComment.createDate) : qaComment.createDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = cId;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + questionId;
        result = 31 * result + (pid != null ? pid.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + createUser;
        return result;
    }
}
