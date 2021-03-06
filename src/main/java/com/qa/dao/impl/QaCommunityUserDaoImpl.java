package com.qa.dao.impl;

import com.qa.dao.QaCommunityUserDao;
import com.qa.entity.QaFrontUser;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 */

@Repository("QaCommunityUserDao")
@Transactional(rollbackFor = Exception.class)//出现Exception异常回滚
public class QaCommunityUserDaoImpl implements QaCommunityUserDao {

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;

    /**
     * 描述： 获得前台用户列表
    */
    @Override
    public Map getCommunityUserList(String pages, String limit, String name, String[] rangeDate) {

        int firstRe = 0;            // 查询的第一个结果
        int lastRe = 0;             // 查询的最后一个结果
        int count = 0;              // 查询到总数
        int page = 0;
        int lim = 0;
        String sql;                 // 查询语句
        Timestamp times[] = new Timestamp[2];   // 查询时间
        Query query = null;
        List comlist=null;
        Map userList = new HashMap();       // 结果集合


        // 将字符串转换为int
        page = Integer.parseInt(pages) - 1;
        lim = Integer.parseInt(limit);

        firstRe = page * lim;


        if(rangeDate[0] == null){
            times[0] = Timestamp.valueOf("1999-01-01 00:00:00");
            times[1] = new Timestamp(new Date().getTime());
        }else{
            times[0] = Timestamp.valueOf(rangeDate[0]);
            times[1] = Timestamp.valueOf(rangeDate[1]);
        }


        sql = "From QaFrontUser as qf ";

//        System.out.println(name);

        if(!name.equals("")){
            sql += "where qf.name like ?";
            query = sessionFactory.getCurrentSession().createQuery(sql);
            query.setString(0,"%"+name+"%");
        }

        else if(rangeDate[0] != null){
            sql += "where create_date BETWEEN ? AND ?";
            query = sessionFactory.getCurrentSession().createQuery(sql);
            query.setTimestamp(0,times[0]);
            query.setTimestamp(1,times[1]);
        }else{
            query = sessionFactory.getCurrentSession().createQuery(sql);
        }



        // 得到数据长度
        count = query.list().size();
        // 设置查询第一个数据以及查询null的数量
        query.setFirstResult(firstRe);
        query.setMaxResults(lim);
        comlist = query.list();


        userList.put("count",count);
        userList.put("list",comlist);

        return userList;
    }


    /**
     *
     * 描述： 禁言用户集合
    */
    @Override
    public boolean banComUser(List<Integer> ids,String comment) {
        boolean b = false;
        String hql = "update QaFrontUser u set u.status=0,u.comment=?  where id in (:ids)";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString(0,comment);

        int result = query.setParameterList("ids", ids).executeUpdate();
        if(result != 0){
            b = true;
        }
        return b;
    }


    /**
     *
     * 描述： 更新用户信息
     * @param user
     */
    @Override
    public boolean updateUser(int user,String password) {
        String hql = "update QaFrontUser qf set qf.password=? where qf.id=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString(0,password);
        query.setInteger(1,user);
        int result = query.executeUpdate();
        if(result != 0) {
            return true;
        }else{
            return false;
        }
    }

    @Override
    public QaFrontUser findById(int id) {
        String hql = "From QaFrontUser where id = ?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setInteger(0,id);
        QaFrontUser user = (QaFrontUser) query.list().get(0);
        return user;
    }


    // 解除用户禁言
    @Override
    public boolean banComUser(List<Integer> ids) {
        boolean b = false;
        String hql = "update QaFrontUser u set u.status=1  where id in (:ids)";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        int result = query.setParameterList("ids", ids).executeUpdate();
        if(result != 0){
            b = true;
        }
        return b;
    }


    // 获取最新用户以及总用户数
    public Map getLatestUserAndAllUser(){

        Map userList = new HashMap();       // 结果集合

        long latest = 0;
        long count = 0;
        Query query = null;

        // 查找前一周至现在新增用户
        Date today = new Date();
        Date yesterdayDate = new Date(today.getTime()-86400000L * 7);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(yesterdayDate);
        String hql = "select count(*) from QaFrontUser  where createDate > ?";
        query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString(0,yesterday);
        latest = (long) query.list().get(0);
        userList.put("latest",latest);

        // 查找总用户
        hql = "select count(*) from QaFrontUser ";
        query = sessionFactory.getCurrentSession().createQuery(hql);
        count = (long) query.list().get(0);
        userList.put("count",count);

        return userList;
    }

}
