package com.qa.service;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by 3tu on 2019/5/8.
 */

public interface DashBoardService {

    public Map getUserInfo();
    public Map getArticleInfo();
    public Map getLatestArticle();

}
