package com.qa.service.Impl;

import com.qa.dao.QaBackQuesDao;
import com.qa.dao.QaCommunityUserDao;
import com.qa.service.DashBoardService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by 3tu on 2019/5/8.
 */
@Service
public class DashBoardServiceImpl implements DashBoardService {

    @Resource
    private QaCommunityUserDao qaCommunityUserDao;

    @Resource
    private QaBackQuesDao qaBackQuesDao;

    /**
     * 获取用户相关数量
     * @return
     */
    @Override
    public Map getUserInfo() {
        return qaCommunityUserDao.getLatestUserAndAllUser();
    }


    /**
     * 获取文章相关数量
     * @return
     */
    @Override
    public Map getArticleInfo(int topic) {
        return qaBackQuesDao.getCheckQuesAndAllQues(topic);
    }

    /**
     *获取最新文章信息
     * @return
     */
    @Override
    public Map getLatestArticle() {
        return qaBackQuesDao.getLatestArticle();
    }
}
