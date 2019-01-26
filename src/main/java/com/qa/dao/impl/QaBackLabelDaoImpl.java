package com.qa.dao.impl;

import com.qa.dao.QaBackLabelDao;
import com.qa.entity.QaLabel;
import com.qa.entity.QaTopic;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qa.entity.BaTopicToLabel;

/**
 *
 */
@Transactional(rollbackFor = Exception.class)//出现Exception异常回滚
@Repository("QaBackLabelDao") //注入
public class QaBackLabelDaoImpl implements QaBackLabelDao{

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    /**
     *
     * 添加标签信息
     * @param qaLabel
     * @return boolean
     */
    @Override
    public boolean addLabel(QaLabel qaLabel) {
        // 添加的名称唯一(首先查找更改的标签是否存在)
        String hql = "FROM QaLabel where labelName = ? and topicId = ? ";
        Query query1 =  sessionFactory.getCurrentSession().createQuery(hql);
        query1.setString(0,qaLabel.getLabelName());
        query1.setInteger(1,qaLabel.getTopicId());
        if(query1.list().size() != 0)
            return false;

        //接受返回值
        Serializable result = sessionFactory.getCurrentSession().save(qaLabel);
        int integer = (Integer)result;      //添加时，成功返回主键值，否则错误
        if(integer != 0) {
            return true;
        }else {
            return false;
        }

    }

    /**
     *
     * 更新标签
     * @param qaLabel
     * @return
     */
    @Override
    public boolean updateLabel(QaLabel qaLabel) {
        String hql = null;
        int result = 0;
            // 更改的名称唯一(首先查找更改的标签是否存在)
            hql = "FROM QaLabel where labelName = ? and topicId = ? and lId != ?";
            Query query1 =  sessionFactory.getCurrentSession().createQuery(hql);
            query1.setString(0,qaLabel.getLabelName());
            query1.setInteger(1,qaLabel.getTopicId());
            query1.setInteger(2,qaLabel.getlId());
            if(query1.list().size() != 0)
                return false;

            // 更改的信息不存在时,便可以更新
            hql = "update QaLabel ql set ql.labelName=?, ql.remarks=?,  ql.topicId=?, ql.sorted = ? where ql.lId=?";
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            query.setString(0,qaLabel.getLabelName());
            query.setString(1,qaLabel.getRemarks());
            query.setInteger(2,qaLabel.getTopicId());
            query.setInteger(3,qaLabel.getSorted());
            query.setInteger(4,qaLabel.getlId());
            result = query.executeUpdate();
            return result != 0;

    }

    /**
     *
     * 删除标签
     * @param l_id  标签id
     * @return
     */
    @Override
    public boolean deleteLabel(int l_id) {
        try{
            //先获取在删除
            QaLabel ql = (QaLabel) sessionFactory.getCurrentSession().get(QaLabel.class, l_id);
            sessionFactory.getCurrentSession().delete(ql);
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *获取该标签的信息
     * @param l_id
     * @return
     */
    @Override
    public QaLabel getQaLabel(int l_id) {
//        return (QaLabel) sessionFactory.getCurrentSession().get(QaLabel.class, l_id);
        List list  =  sessionFactory.getCurrentSession().createQuery("FROM QaLabel where lId  = "+l_id).list();
        return (QaLabel) list.get(0);
    }

    /**
     * 获取标签列表集合
     * @return
     */
    @Override
    public List getQaLabelList() {
        //hql获取标签列表并按sorted字段排序
        return sessionFactory.getCurrentSession().createQuery("FROM QaLabel order by sorted asc").list();
    }

    /**
     * 查找隶属的话题信息  ，即该标签的所属话题
     * @param to_id
     * @return
     * PS：最开始写的，暂时先这样  :) :)
     */
    public QaTopic getLabelForTopic(int to_id) {
        List list  =  sessionFactory.getCurrentSession().createQuery("FROM QaTopic where toId = "+to_id).list();
        System.out.println(list.get(0));
        return (QaTopic) list.get(0);
    }

    /**
     * 返回所有的话题列表
     * 主要针对添加或更新话题是使用
     * @return
     */
    public List getTopicList() {
        return sessionFactory.getCurrentSession().createQuery("FROM QaTopic order by sorted asc").list();
    }

    public Map getLabelToTopic(int topicId) {
        Map map = new HashMap();
        String hql = "FROM QaLabel where topicId = ?";
        Query query =   sessionFactory.getCurrentSession().createQuery(hql);
        query.setInteger(0,topicId);
        List list = query.list();
        map.put("labelList", list);
        return map;

    }

    @Override
    public boolean checkTopic(int topicId) {
        String hql = "from QaLabel where topicId = ?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setInteger(0,topicId);
        return query.list().size() != 0;
    }
}
