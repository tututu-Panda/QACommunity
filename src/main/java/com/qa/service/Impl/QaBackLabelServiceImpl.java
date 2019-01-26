package com.qa.service.Impl;

import com.qa.dao.QaBackLabelDao;
import com.qa.dao.QaBackQuesDao;
import com.qa.entity.BaTopicToLabel;
import com.qa.entity.QaLabel;
import com.qa.entity.QaTopic;
import com.qa.service.QaBackLabelService;
import com.sun.jmx.remote.internal.ArrayQueue;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *Create by 3tu on 2017/12/10
 */
//注入服务
@Service("QaBackLabelService")
public class QaBackLabelServiceImpl implements QaBackLabelService{

    @Resource
    private QaBackLabelDao qaBackLabelDao;
    @Resource
    private QaBackQuesDao qaBackQuesDao;


    /**
     * 作者：3tu   下同
     * 添加标签
     * @param qaLabel
     * @return
     */
    @Override
    public boolean addQaLabel(QaLabel qaLabel) {
        //调用DAO层方法
        return this.qaBackLabelDao.addLabel(qaLabel);

    }

    /**
     * 更新标签
     * @param qaLabel
     * @return
     */
    @Override
    public boolean updateQaLabelById(QaLabel qaLabel) {

        return this.qaBackLabelDao.updateLabel(qaLabel);
    }

    /**
     * 删除标签
     * @param l_id
     * @return
     */
    @Override
    public boolean deleteQaLabel(int l_id) {
        // 首先查找该标签是否存在下级问题,如果没有则删除,否则无法删除
        boolean b = qaBackQuesDao.checkLabel(l_id);
        return !b && this.qaBackLabelDao.deleteLabel(l_id);
    }

    /**
     * 根据ID取出单个标签
     * @param l_id
     * @return
     */
    @Override
    public List getQaLabelByid(int l_id) {
        //接受全部的原生标签列表
       QaLabel ql = this.qaBackLabelDao.getQaLabel(l_id);  //返回的是对象，所以用对象来接收
       List<QaLabel> list = new ArrayList<QaLabel>();
       list.add(ql);
       return list;
    }

    /**
     * 获取标签列表集合
     * @return
     */
    @Override
    public List getQaLabelList() {
        //接受全部的原生标签列表
        List<QaLabel> list = this.qaBackLabelDao.getQaLabelList();
        //组合对象，封装需要的结果list
        List<BaTopicToLabel> resultList = new ArrayList<BaTopicToLabel>();
        //循环list
        /*因为查询出来的结果需要一些组合字段，即同时需要两张不同表中某些字段，所以新建了类来生成对象，对结果进行
           封装
        */
        for (QaLabel ql : list){
            BaTopicToLabel bt = new BaTopicToLabel();
            QaTopic qt = null;
            qt = qaBackLabelDao.getLabelForTopic(ql.getTopicId());  //查找所隶属的话题对象

            //封装值
            bt.setLId(ql.getlId());
            bt.setLabelName(ql.getLabelName());
            bt.setCreateDate(ql.getCreateDate());
            bt.setRemarks(ql.getRemarks());
            bt.setTopicId(ql.getTopicId());
            bt.setToId(qt.getToId());
            bt.setTopicName(qt.getTopicName());
            resultList.add(bt);  //添加进列表
        }
        return resultList;
    }

    /**
     * 获取话题列表list
     * @return
     */
    public List getTopicList() {
        return this.qaBackLabelDao.getTopicList();
    }

    public Map getLabelToTopic(int topicId) {
        return this.qaBackLabelDao.getLabelToTopic(topicId);
    }

    public QaBackQuesDao getQaBackQuesDao() {
        return qaBackQuesDao;
    }

    public void setQaBackQuesDao(QaBackQuesDao qaBackQuesDao) {
        this.qaBackQuesDao = qaBackQuesDao;
    }
}
