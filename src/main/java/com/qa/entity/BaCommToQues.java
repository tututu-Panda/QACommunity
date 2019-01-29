package com.qa.entity;

import java.math.BigInteger;
import java.util.Date;

/**
 *Create by 3tu on 2017/12/19
 */
public class BaCommToQues {

    int commId;     // 评论id
    String content; // 评论内容
    Date createDate;    // 创建时间
    String accountName; // 姓名
    String headPhoto;   // 头像
    int commentCount;   // 二级评论总数


    public int getCommId() {
        return commId;
    }

    public void setCommId(int commId) {
        this.commId = commId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getHeadPhoto() {
        return headPhoto;
    }

    public void setHeadPhoto(String headPhoto) {
        this.headPhoto = headPhoto;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

}
