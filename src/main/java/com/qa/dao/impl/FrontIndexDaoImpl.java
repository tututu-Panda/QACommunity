package com.qa.dao.impl;

import com.qa.dao.FrontIndexDao;
import com.qa.recommend.algorithms.ContentRecommender;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.*;

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
//        System.out.println("----"+topic);
        if(topic != 0){

            // 根据子标签查询问题详情
            sql =  "select t1.q_id as qId,    t1.title as qTitle,     t1.create_date as createDate,       " +
                    "t2.to_id as toId,      t2.topic_name as topicName,     t3.id as id,  " +
                    "t3.name as accountName,    t3.photo as userPhoto,      count(DISTINCT t4.c_id) commNum,    " +
                    "t1.views as browNum from qa_question as t1  " +
                    " left join qa_topic t2 on t1.topic_id=t2.to_id "+
                    " left join qa_front_user t3 on t1.create_user=t3.id" +
                    " left join qa_comment t4 on t1.q_id=t4.question_id" +
                    " where t1.topic_id = "+topic+
                    " and t1.checked = 0 "+
                    " group by t1.q_id order by "+orderString+" desc ";
        }


        else{
            sql = "select t1.q_id as qId,    t1.title as qTitle,     t1.create_date as createDate,       " +
                    "t2.to_id as toId,      t2.topic_name as topicName,     t3.id as id,  " +
                    "t3.name as accountName,    t3.photo as userPhoto,      count(DISTINCT t4.c_id) commNum,    " +
                    "t1.views as browNum from qa_question as t1" +
                    " left join qa_topic t2 on t1.topic_id=t2.to_id" +
                    " left join qa_front_user t3 on t1.create_user=t3.id" +
                    " left join qa_comment t4 on t1.q_id=t4.question_id" +
                    " where t1.checked = 0  " +
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
     * 1. 根据用户获取推荐信息
     * 2. 推荐信息不足时，随机获取
     * 获取随机问题
     * @return
     */
    @Override
    public List gerRandomQues(ArrayList<Integer>... list) {

        final int limitSize = 5;        // 最多推荐数目5条

        List result = new ArrayList();

        // 首先判断是否传过来问题id集合，以及该集合长度
        String hql;
        int limit ;
       Query query;

        if(list.length > 0){
            hql  = " SELECT q_id , title FROM qa_question WHERE checked = 0 AND q_id  in (:list)";
            query = sessionFactory.getCurrentSession().createSQLQuery(hql);
            query.setParameterList("list",list[0]);
            limit = limitSize-query.list().size();          // 去掉未过审的问题
            result.addAll(query.list());

            hql  = " SELECT distinct  q_id , title FROM qa_question WHERE checked = 0 AND q_id NOT IN (:qids)AND q_id >=" +
                    " ((SELECT MAX(q_id) FROM qa_question)-(SELECT MIN(q_id) FROM qa_question)) * RAND() " +
                    "+ (SELECT MIN(q_id) FROM qa_question)  LIMIT ?  ";

            query = sessionFactory.getCurrentSession().createSQLQuery(hql);
            query.setParameterList("qids",list[0]);
            query.setInteger(0,limit);
            result.addAll(query.list());
            return result.subList(0,5);

        }
        // 游客或未产生用户图像时
        else {
            limit = limitSize;
            // 根据limit值随机选择
            hql = " SELECT distinct  q_id , title FROM qa_question WHERE checked = 0 AND q_id >=" +
                    " ((SELECT MAX(q_id) FROM qa_question)-(SELECT MIN(q_id) FROM qa_question)) * RAND() " +
                    "+ (SELECT MIN(q_id) FROM qa_question)  LIMIT ?  ";
            query = sessionFactory.getCurrentSession().createSQLQuery(hql);
            query.setInteger(0, limit);
            result.addAll(query.list());

            return result;
        }
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

    /**
     * 搜索问题
     * @param page
     * @param orderType
     * @param topic
     * @param search
     * @return
     */
    public  Map getSearchQues(int page, int orderType, int topic, String search){
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
        if(topic != 0){

            // 根据子标签查询问题详情
            sql =  "select t1.q_id as qId,    t1.title as qTitle,     t1.create_date as createDate,       " +
                    "t2.to_id as toId,      t2.topic_name as topicName,     t3.id as id,  " +
                    "t3.name as accountName,    t3.photo as userPhoto,      count(DISTINCT t4.c_id) commNum,    " +
                    "t1.views as browNum from qa_question as t1  " +
                    " left join qa_topic t2 on t1.topic_id=t2.to_id "+
                    " left join qa_front_user t3 on t1.create_user=t3.id" +
                    " left join qa_comment t4 on t1.q_id=t4.question_id" +
                    " where t1.topic_id = "+topic+
                    " and t1.checked = 0 "+
                    "and t1.title LIKE '%"+search+"%'"+
                    " group by t1.q_id order by "+orderString+" desc ";
        }


        else{
            sql = "select t1.q_id as qId,    t1.title as qTitle,     t1.create_date as createDate,       " +
                    "t2.to_id as toId,      t2.topic_name as topicName,     t3.id as id,  " +
                    "t3.name as accountName,    t3.photo as userPhoto,      count(DISTINCT t4.c_id) commNum,    " +
                    "t1.views as browNum from qa_question as t1" +
                    " left join qa_topic t2 on t1.topic_id=t2.to_id" +
                    " left join qa_front_user t3 on t1.create_user=t3.id" +
                    " left join qa_comment t4 on t1.q_id=t4.question_id" +
                    " where t1.checked = 0  " +
                    "and t1.title LIKE '%"+search+"%'"+
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
        map.put("search", search);
        map.put("list", list);
        return map;
    }

}
