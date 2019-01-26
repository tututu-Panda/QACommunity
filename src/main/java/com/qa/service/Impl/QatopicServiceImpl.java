package com.qa.service.Impl;

import com.qa.dao.QaBackLabelDao;
import com.qa.dao.QaTopicDao;
import com.qa.entity.QaTopic;
import com.qa.service.QaTopicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("QaTopicService")
@Transactional
public class QatopicServiceImpl implements QaTopicService {
    @Resource
    private QaTopicDao qaTopicDao;

    @Resource
    private QaBackLabelDao qaBackLabelDao;

    @Override
    public List<QaTopic> findAllTopic(){

        List<QaTopic> list=qaTopicDao.findAll();
        return list;
    }

    public boolean DeleteTopic(int i){
        // 首先检查该话题下是否存在标签
        boolean b = qaBackLabelDao.checkTopic(i);
        if(b)
            return false;
        qaTopicDao.DeleteTopic(i);
        return true;

    }
    public void UpdateTopic(QaTopic qaTopic){
        System.out.println("nihao a"+qaTopic.getTopicName());
        qaTopicDao.UpdateTopic(qaTopic);
    }
    public void AddTopic(QaTopic qaTopic ){
        System.out.println("nihao a"+qaTopic.getTopicName());
        qaTopicDao.AddTopic(qaTopic);
    }

    public QaBackLabelDao getQaBackLabelDao() {
        return qaBackLabelDao;
    }

    public void setQaBackLabelDao(QaBackLabelDao qaBackLabelDao) {
        this.qaBackLabelDao = qaBackLabelDao;
    }
}
