package com.qa.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;



@Entity
@Table(name = "qa_question", schema = "qacommunity", catalog = "")
public class QaQuestion {
    private int qId;
    private String title;
    private String detail;
    private int topicId;
    private String labelIds;
    private Date createDate;
    private int createUser;
    private int views;
    // 问题与话题是多对一的关系
    private QaTopic topic;
    // 问题与用户是多对一的关系
    private QaFrontUser frontUser;


    @Id
    @Column(name = "q_id", nullable = false)
    public int getqId() {
        return qId;
    }

    public void setqId(int qId) {
        this.qId = qId;
    }

    @Basic
    @Column(name = "title", nullable = false, length = 255)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "detail", nullable = false, length = -1)
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Basic
    @Column(name = "topic_id", nullable = false)
    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    @Basic
    @Column(name = "label_ids", nullable = true, length = 128)
    public String getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(String labelIds) {
        this.labelIds = labelIds;
    }

    @Basic
    @Column(name = "create_date", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
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

    @Basic
    @Column(name = "views", nullable = false, length = 11)
    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QaQuestion that = (QaQuestion) o;

        if (qId != that.qId) return false;
        if (topicId != that.topicId) return false;
        if (createUser != that.createUser) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (detail != null ? !detail.equals(that.detail) : that.detail != null) return false;
        if (labelIds != null ? !labelIds.equals(that.labelIds) : that.labelIds != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = qId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (detail != null ? detail.hashCode() : 0);
        result = 31 * result + topicId;
        result = 31 * result + (labelIds != null ? labelIds.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + createUser;
        return result;
    }

    @ManyToOne (targetEntity = QaTopic.class)
    @JoinColumn(name = "top_id",referencedColumnName = "to_id")
    public QaTopic getTopic() {
        return topic;
    }

    public void setTopic(QaTopic topic) {
        this.topic = topic;
    }

    @ManyToOne (targetEntity = QaFrontUser.class)
    @JoinColumn(name = "cre_user",referencedColumnName = "id")
    public QaFrontUser getFrontUser() {
        return frontUser;
    }

    public void setFrontUser(QaFrontUser frontUser) {
        this.frontUser = frontUser;
    }
}
