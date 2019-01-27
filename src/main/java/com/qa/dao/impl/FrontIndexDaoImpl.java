package com.qa.dao.impl;

import com.qa.dao.FrontIndexDao;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 前台数据库交互层
 */
@Transactional(rollbackFor = Exception.class)//出现Exception异常回滚
@Repository("FrontIndexDao") //注入
public class FrontIndexDaoImpl implements FrontIndexDao {
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    /**
     * 获取首页问题列表
     * @param page  页码
     * @param orderType 排序类型：1，最新（按时间） 2.最热（评论数最多）
     * @param topic
     * @return
     */
    @Override
    public Map getQuesIndex(int page, int orderType, int topic) {
        int firstRe = 0;            // 查询的第一个结果
        int count = 0;              // 查询到总数
        int pages;           //页码,int型变量，默认为空
        int limit = 9;         //每页的显示数
        String orderString;     //排序

        int[] labels;       // 话题对应的标签

        if(page == 0) {
            pages = 0;
        }else {
            pages = page - 1;
        }
        if(orderType == 1) {
            orderString = "createDate";
        }else{
            orderString = "commNum";
        }

        Map map = new HashMap();

        String sql = null;

        // 如果传入话题,查询话题对应的标签
        System.out.println("----"+topic);
        if(topic != 0){

            // 根据子标签查询问题详情
            sql =  "select t1.q_id as qId,    t1.title as qTitle,     t1.create_date as createDate,       " +
                    "t2.to_id as toId,      t2.topic_name as topicName,     t3.id as id,  " +
                    "t3.name as accountName,    t3.photo as userPhoto,      count(DISTINCT t4.c_id) commNum,    " +
                    "t1.views as browNum from qa_question as t1 "  +
                    " left join qa_topic t2 on t1.topic_id=t2.to_id "+
                    " left join qa_front_user t3 on t1.create_user=t3.id" +
                    " left join qa_comment t4 on t1.q_id=t4.question_id" +
                    " group by t1.q_id order by "+orderString+" desc";
        }


        else{
            sql = "select t1.q_id as qId,    t1.title as qTitle,     t1.create_date as createDate,       " +
                    "t2.to_id as toId,      t2.topic_name as topicName,     t3.id as id,  " +
                    "t3.name as accountName,    t3.photo as userPhoto,      count(DISTINCT t4.c_id) commNum,    " +
                    "t1.views as browNum from qa_question as t1" +
                    " left join qa_topic t2 on t1.topic_id=t2.to_id" +
                    " left join qa_front_user t3 on t1.create_user=t3.id" +
                    " left join qa_comment t4 on t1.q_id=t4.question_id" +
                    " group by t1.q_id order by "+orderString+" desc";
        }

        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);

        // 获取总数
        List list = query.list();
        count = list.size();
//        System.out.println("count:"+count);

        // 获取该分页的列表数据
        firstRe = pages * limit;   //当前该显示的记录开始点
        query.setFirstResult(firstRe);
        query.setMaxResults(limit);
        list = query.list();


        map.put("count", count);
        map.put("page",page);
        map.put("orderType", orderType);
        map.put("list", list);
        return map;
    }

    /**
     * 获取话题列表
     * @return
     */
    @Override
    public Map getTopicIndex() {

        Map map = new HashMap();
        List list = sessionFactory.getCurrentSession().createQuery("from QaTopic order by sorted asc").list();
        map.put("topicLists", list);
        return map;
    }


    /**
     * 获取问题详情
     * @return
     */
    public Map getTheQuesInfo(int quesId) {
        Map map = new HashMap();
        String sql = "select t1.q_id as qId, t1.title as qTitle,t1.detail as quesDetail,t1.label_ids as labels,t1.create_date as createDate,t2.to_id as toId,t2.topic_name as topicName," +
                " t3.name as accountName,t3.photo as userPhoto, count(DISTINCT t4.c_id) commNum, t1.views as browNum, t1.create_user as userId  from qa_question as t1" +
                " left join qa_topic t2 on t1.topic_id=t2.to_id" +
                " left join qa_front_user t3 on t1.create_user=t3.id" +
                " left join qa_comment t4 on t1.q_id=t4.question_id" +
                " WHERE t1.q_id = "+quesId+" group by t1.q_id";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        List list = query.list();
        map.put("list",list);
        return map;
    }

    /**
     * 查询问题时返回的标签字符集
     * 获取当前问题包含的标签
     * @param labelStr
     * @return
     */
    public String[] LabelList(String labelStr) {
        String sql = "select label_name from qa_label where l_id in ("+labelStr+")";
        List list = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
        //存放入数组
        String[] labelNames  = (String[]) list.toArray(new String[list.size()]);
        return labelNames;
    }

    /**
     * 获取所有的话题列表
     * @return
     */
    public Map getTopicList() {

        return null;
    }


    /**
     * 获取回复总榜
     * @return
     */
    @Override
    public List getReplyRank() {
        int limit = 4;  // 默认获取排行前5个回复总榜
        String hql = "SELECT qu.id,     qu.name,    qu.photo,   count(qc.create_user) as num FROM qa_front_user qu " +
                "LEFT JOIN qa_comment qc on qu.id = qc.create_user group by create_user " +
                "order by count(qc.create_user) desc  limit ?" ;
        Query query = sessionFactory.getCurrentSession().createSQLQuery(hql);
        query.setInteger(0,limit);
        return query.list();
    }

    /**
     * 获取随机问题
     * @return
     */
    @Override
    public List gerRandomQues() {
        int limit = 5;
        String hql = " SELECT q_id , title FROM qa_question WHERE q_id >=" +
                " ((SELECT MAX(q_id) FROM qa_question)-(SELECT MIN(q_id) FROM qa_question)) * RAND() " +
                "+ (SELECT MIN(q_id) FROM qa_question)  LIMIT ?";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(hql);
        query.setInteger(0,limit);
        return query.list();
    }

    /**
     * 查询浏览量
     */
    public int getViews(int ques_id){
        String hql = "select t.views from QaQuestion t where t.id = ?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setInteger(0,ques_id);
        return (int) query.list().get(0);
    }

    /**
     * 更新浏览量
     * @param ques_id
     */
    public void updateViews(int ques_id, int views){
        String hql = "update QaQuestion q set q.views = ?  where q.qId = ?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setInteger(0,views);
        query.setInteger(1,ques_id);
        query.executeUpdate();
    }

}
