package com.qa.dao.impl;

import com.qa.dao.QaBackQuesDao;
import com.qa.entity.QaQuestion;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
@Transactional(rollbackFor = Exception.class)//出现Exception异常回滚
@Repository("QaBackQuesDao") //注入
public class QaBackQuesDaoImpl implements QaBackQuesDao{
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    /**
     *
     * 后台某些功能如添加或编辑不需要，暂时不写
     * 添加问题功能在前台写
     * @param qaQuestion
     * @return
     */
    @Override
    public boolean addQues(QaQuestion qaQuestion) {
        return false;
    }

    @Override
    public boolean update(QaQuestion qaQuestion) {
        return false;
    }


    @Override
    public QaQuestion getQaQuestion(int l_id) {
        return null;
    }

    @Override
    /**
     *
     * 获取所有的问题信息列表：这里定义map集合来存放数据，因为map集合是key-value形式，
     * 结果可以清晰显示，个人见解
     *
     */
    public Map getQuestionList(int page, int limits, String[] rangeDate) {
        int firstRe = 0;            // 查询的第一个结果
        int count = 0;              // 查询到总数
        int pages = page - 1;           //页码
        Timestamp times[] = new Timestamp[2];   // 定义时间参数

        if(rangeDate[0].equals("")){
            times[0] = Timestamp.valueOf("1999-01-01 00:00:00");
            times[1] = new Timestamp(new Date().getTime());
        }else{
            times[0] = Timestamp.valueOf(rangeDate[0]);
            times[1] = Timestamp.valueOf(rangeDate[1]);
        }

        Map map = new HashMap();
        /*因为没有建立外键，所以不用hql实现；
         * 此处书写原生sql语句进行查询，注意sql语句的书写正确；
         * 左连接-连表查出问题列表并且查询各个问题的所属话题以及创建用户
         */
        String sql = "select t1.q_id as qId,t1.title as qTitle,t1.detail as qDetail,t1.label_ids as labelIds,t1.create_date as createDate,t2.to_id as toId,t2.topic_name as topicName," +
                "t3.account as account,t3.name as accountName from qa_question as t1 " +
                " left join qa_topic t2 on t1.topic_id=t2.to_id" +
                " left join qa_front_user t3 on t1.create_user=t3.id where t1.checked = 0 AND t1.create_date BETWEEN  ? and ?";

//        String sql = "from QaQuestion  t1 left  join t1.topicId left  join fetch t1.createUser where t1.createUser between ? and ?";

        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        query.setTimestamp(0,times[0]);
        query.setTimestamp(1,times[1]);
        List list = query.list();
        //获取到记录长度（总的页数）
        count = list.size();

        firstRe = pages * limits;   //当前该显示的记录开始点
        query.setFirstResult(firstRe);
        query.setMaxResults(limits);
        list = query.list();

//        System.out.println("limits"+limits);

        map.put("count",count);     //总数
        map.put("list",list);       //数据
        return map;   //返回结果map
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
     * 通过问题的l_id来获取该问题的详细信息
     * @param l_id
     * @return
     */
    public Map getTheQuestion(int l_id) {
        Map map = new HashMap();  //实例化map集合

        // 问题详情
        String sql = "select t1.title as qTitle,t1.detail as quesDetail, t1.create_date as createDate, t2.topic_name as topicName," +
                " t3.name as accountName,t3.photo as userPhoto, count(DISTINCT t4.c_id) commNum, t1.views as browNum  from qa_question as t1" +
                " left join qa_topic t2 on t1.topic_id=t2.to_id" +
                " left join qa_front_user t3 on t1.create_user=t3.id" +
                " left join qa_comment t4 on t1.q_id=t4.question_id" +
                " WHERE t1.q_id = "+l_id+" group by t1.q_id";

        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);

        // 标签名称
        String sql1 = "select label_ids FROM qa_question WHERE q_id = "+l_id ;
        List list = sessionFactory.getCurrentSession().createSQLQuery(sql1).list();
        String label  = (String) list.get(0);
        String sql2 = "select label_name from qa_label where l_id  in ("+label+")";
       list =  sessionFactory.getCurrentSession().createSQLQuery(sql2).list();
        String[] labelNames  = (String[]) list.toArray(new String[list.size()]);

        //存放入数组
        map.put("list",query.list().get(0));
        map.put("labels",labelNames);
        map.put("q_id",l_id);

        return map;
    }

    /**
     * 根据时间段获取浏览量：开始时间-结束时间
     * @param time
     * @return
     */
    public Map getBrowsDate(int q_id, String[] time) {
        Map map = new HashMap();  //实例化map集合
        //对日期进行处理
        Timestamp times[] = new Timestamp[2];
        //时间格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        //时间赋值，如果有时间传过来，则进行赋值，如果没有，则将时间段设置在当前日期与前10天之间
        //时间格式都是从0点开始
        if(time == null || (time != null && time.length == 0)) {
            times[0] = Timestamp.valueOf(format.format(new Timestamp(new Date().getTime() - (long) 10 * 24 * 60 * 60 * 1000)));
            times[1] = Timestamp.valueOf(format.format(new Timestamp(new Date().getTime())));
        }else {
            times[0] = Timestamp.valueOf(format.format(Timestamp.valueOf(time[0])));
            times[1] = Timestamp.valueOf(format.format(Timestamp.valueOf(time[1])));
        }

        //首先对时间范围和问题进行筛选，接着格式化时间分组计数
        String sql3 = "select date_format(browse_date,'%Y-%m-%d') btime, count(id) sums from (select * from qa_question_browse where q_id ="+ q_id +" and browse_date between '"+ times[0] +"' and '"+times[1]+"') as view1 GROUP BY btime";
        Query query3 = sessionFactory.getCurrentSession().createSQLQuery(sql3);
        List list = query3.list();
        map.put("list", list);

        return map;
    }

    /**
     * 获取该问题下的一级评论
     * @param q_id
     * @return
     */
    public Map getTheComment(int q_id) {
        Map map = new HashMap();
        //同样原生sql，需要左连接，因为当评论没有赞的时候一样要查询出来
        String sql = "select t1.c_id as commId,     t1.content as content,      t1.create_date as createDate,       " +
                "t2.name as accountName,         t2.photo as hphoto from qa_comment as t1" +
                " left join qa_front_user t2 on t1.create_user = t2.id" +
                " where t1.pid is null and t1.question_id="+q_id+" GROUP BY t1.c_id";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        List list = query.list();
        map.put("commentList", list);
        this.getCountOfSonComment(48);
        return map;
    }

    /**
     * 获取每个评论下的二级评论总数
     * @param c_id
     * @return
     */
    public int getCountOfSonComment(int c_id){
        String hql = "select  count(cId) from QaComment where pid = ? ";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setInteger(0,c_id);
        long count = (long) query.list().get(0);
//        System.out.println("dao:"+count);
        return (int) count;
    }

    /**
     * 获取评论下的评论，即为二级评论
     * PS:暂时写到二级评论，暂不考虑无限级评论
     * @param pq_id  父级评论ID
     * @return
     */
    public Map getTheComment_two(int pq_id) {
        Map map = new HashMap();
        String sql = "select t1.c_id as commId,t1.content as content,t1.create_date as createDate,t2.name as accountName,t2.photo as hphoto from qa_comment as t1" +
                " left join qa_front_user t2 on t1.create_user=t2.id" +
                " where t1.pid="+pq_id+" group by t1.c_id";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        List list = query.list();
        map.put("two_commentList", list);
        return map;
    }

    /**
     * 删除问题：删除问题时会将该问题下的所有的评论信息全部删除
     * @param q_ids 问题逻辑id
     * @return boolean
     */
    @Override
    public boolean deleteQuestion(List<Integer> q_ids) {

        try{
            //删除该问题下的所有的评论（包括一级评论和二级评论）
            String hql2 = "delete QaComment t1 where t1.questionId in (:qIds)";
            Query query2 = sessionFactory.getCurrentSession().createQuery(hql2);
            query2.setParameterList("qIds",q_ids);
            query2.executeUpdate();

            //再删除问题
            String hql1 = "Delete from QaQuestion where qId in (:qIds)";
            Query query1 = sessionFactory.getCurrentSession().createQuery(hql1);

            int result1 = query1.setParameterList("qIds", q_ids).executeUpdate();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除评论
     * @param c_id 评论的id
     * @return boolean
     */
    public boolean deleteComment(int c_id) {
        try{
            //删除该评论，c_id为评论id：（1）当删除的是一级评论是会将该评论下的二级删除；（2）当为二级评论直接删除二级评论
            String hql1 = "delete QaComment t1 where t1.cId = ? or t1.pid = ?";
            Query query1 = sessionFactory.getCurrentSession().createQuery(hql1);
            query1.setInteger(0, c_id);
            query1.setInteger(1, c_id);
            query1.executeUpdate();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查找该标签是否存在下级问题
     * @param l_id
     * @return
     */
    @Override
    public boolean checkLabel(int l_id) {
        String hql = "from QaQuestion where labelIds like ?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString(0, "%"+l_id+"%");
        return query.list().size() != 0;
    }


    /**
     * 获取审核列表
     * @param page
     * @param limits
     * @param rangeDate
     * @param check
     * @return
     */
    @Override
    public Map getCheckQuestionList(int page, int limits, String[] rangeDate, int check) {
        int firstRe = 0;            // 查询的第一个结果
        int count = 0;              // 查询到总数
        int pages = page - 1;           //页码
        Timestamp times[] = new Timestamp[2];   // 定义时间参数

        if(rangeDate[0].equals("")){
            times[0] = Timestamp.valueOf("1999-01-01 00:00:00");
            times[1] = new Timestamp(new Date().getTime());
        }else{
            times[0] = Timestamp.valueOf(rangeDate[0]);
            times[1] = Timestamp.valueOf(rangeDate[1]);
        }

        Map map = new HashMap();
        /*因为没有建立外键，所以不用hql实现；
         * 此处书写原生sql语句进行查询，注意sql语句的书写正确；
         * 左连接-连表查出问题列表并且查询各个问题的所属话题以及创建用户
         */
        String sql = "select t1.q_id as qId,t1.title as qTitle,t1.detail as qDetail,t1.create_date as createDate,t1.checked as checked , t2.to_id as toId,t2.topic_name as topicName," +
                "t3.account as account,t3.name as accountName from qa_question as t1 " +
                " left join qa_topic t2 on t1.topic_id=t2.to_id" +
                " left join qa_front_user t3 on t1.create_user=t3.id where t1.checked = ? AND t1.create_date BETWEEN  ? and ? order BY  t1.checked";

//        String sql = "from QaQuestion  t1 left  join t1.topicId left  join fetch t1.createUser where t1.createUser between ? and ?";

        Query query = sessionFactory.getCurrentSession().createSQLQuery(sql);
        query.setInteger(0 ,check);
        query.setTimestamp(1,times[0]);
        query.setTimestamp(2,times[1]);
        List list = query.list();
        //获取到记录长度（总的页数）
        count = list.size();

        firstRe = pages * limits;   //当前该显示的记录开始点
        query.setFirstResult(firstRe);
        query.setMaxResults(limits);
        list = query.list();

//        System.out.println("limits"+limits);

        map.put("count",count);     //总数
        map.put("list",list);       //数据
        return map;   //返回结果map
    }


//    /**
//     * 不通过该问题集合
//     * @param ids
//     * @return
//     */
//    @Override
//    public boolean noPassQues(List<Integer> ids) {
//        try{
//            //不通过该问题
//            String hql1 = "update QaQuestion set checked = 2 where qId in (:qIds)";
//            Query query1 = sessionFactory.getCurrentSession().createQuery(hql1);
//
//            int result1 = query1.setParameterList("qIds", ids).executeUpdate();
//            return true;
//        }catch(Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 通过问题集合
//     * @param ids
//     * @return
//     */
//    @Override
//    public boolean passQues(List<Integer> ids) {
//        try{
//            //不通过该问题
//            String hql1 = "update QaQuestion set checked = 0 where qId in (:qIds)";
//            Query query1 = sessionFactory.getCurrentSession().createQuery(hql1);
//
//            int result1 = query1.setParameterList("qIds", ids).executeUpdate();
//            return true;
//        }catch(Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    @Override
    public boolean checkQues(List<Integer> ids, int check) {
        try{
            //不通过该问题
            String hql1 = "update QaQuestion set checked =? where qId in (:qIds)";
            Query query1 = sessionFactory.getCurrentSession().createQuery(hql1);

            query1.setInteger(0,check);
            query1.setParameterList("qIds", ids).executeUpdate();
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
