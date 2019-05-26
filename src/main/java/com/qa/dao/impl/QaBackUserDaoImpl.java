package com.qa.dao.impl;

import com.qa.dao.QaBackUserDao;
import com.qa.entity.QaBackUser;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Transactional(rollbackFor = Exception.class)//出现Exception异常回滚
@Repository("QaBackUserDao")  //进行注入
public class QaBackUserDaoImpl implements QaBackUserDao{
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;


    @Override
    public void add(QaBackUser qbUser) {
        sessionFactory.getCurrentSession().save(qbUser);
    }

    @Override
    public QaBackUser getQaUser(int id) {
        return (QaBackUser) sessionFactory.getCurrentSession().get(QaBackUser.class, id);
    }

    @Override
    public void update(QaBackUser qaUser) {
        sessionFactory.getCurrentSession().update(qaUser);
    }

    @Override
    public void delete(int id) {
        sessionFactory.getCurrentSession().delete(
                sessionFactory.getCurrentSession().get(QaBackUser.class, id)
        );
    }

    @Override
    public QaBackUser login(QaBackUser qaUser) {
        Iterator it;
        String hql = "FROM QaBackUser bu where bu.account=? and bu.password=?";
//        System.out.println(hql);
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString(0, qaUser.getAccount());
        query.setString(1, qaUser.getPassword());
        it = query.iterate();
        if (it.hasNext()) {
            List list = query.list();
            return (QaBackUser) list.get(0);
        }else {
            return null;
        }
    }

    @Override
    public List getQaUserList() {
        return sessionFactory.getCurrentSession().createQuery("FROM QaBackUser").list();
    }

    @Override
    public boolean saveUserPhoto(int id, String realPath) {
        String hql = "update QaBackUser set photo=? where id=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString(0,realPath);
        query.setInteger(1,id);

        int result = query.executeUpdate();
        return result != 0;

    }


    /**
     * 获取后台管理员
     * @return
     * @param pages
     * @param limit
     * @param name
     */
    @Override
    public Map getBackUserList(String pages, String limit, String name) {
        int firstRe = 0;            // 查询的第一个结果
        int lastRe = 0;             // 查询的最后一个结果
        int count = 0;              // 查询到总数
        int page = 0;
        int lim = 0;
        String sql;                 // 查询语句
        Query query = null;
        List comlist=null;
        Map userList = new HashMap();       // 结果集合

        // 将字符串转换为int
        page = Integer.parseInt(pages) - 1;
        lim = Integer.parseInt(limit);

        firstRe = page * lim;

        sql = "SELECT t1.id as id, t1.account as account, t1.name as name, t1.sex*1 , t1.email as email, t2.topic_name as topicName, t1.status, t1.topic " +
                " from  qa_back_user t1 " +
                "LEFT JOIN qa_topic t2 ON t1.topic = t2.to_id WHERE t1.role = 1";


        if(!name.equals("")){
            sql += " AND t1.name like ? or t1.account like ? ";
            query = sessionFactory.getCurrentSession().createSQLQuery(sql);
            query.setString(0,"%"+name+"%");
            query.setString(1,"%"+name+"%");
        }else{
            query = sessionFactory.getCurrentSession().createSQLQuery(sql);
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
     * 禁用用户
     * @param id
     * @param status
     * @return
     */
    @Override
    public boolean banBackUser(int id, String status) {

        int statu = Integer.parseInt(status);
        System.out.println(statu);

        boolean b = false;
        String hql = "update QaBackUser u set u.status=? where id =?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setInteger(0,statu);
        query.setInteger(1,id);
        int result = query.executeUpdate();
        if(result != 0){
            b = true;
        }
        return b;
    }


    /**
     * 重置用户密码
     * @param id
     * @param password
     * @return
     */
    @Override
    public boolean resetPassWord(int id, String password) {
        String hql = "update QaBackUser qf set qf.password=? where qf.id=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString(0,password);
        query.setInteger(1,id);
        int result = query.executeUpdate();
        return result != 0;
    }


    /**
     * 更新用户管理的话题
     * @param userId
     * @param topicId
     * @return
     */
    @Override
    public boolean updateBackUserTopic(int userId, int topicId) {

        String hql = "update QaBackUser qf set qf.topic=? where qf.id=?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setInteger(0,topicId);
        query.setInteger(1, userId);
        int result = query.executeUpdate();
        if(result != 0) {
            return true;
        }else{
            return false;
        }
    }


    /**
     * 检查账户是否存在
     * @param account
     * @return
     */
    @Override
    public boolean checkAccount(String account) {
        String hql = "select id from QaBackUser  where account = ?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString(0,account);
        if(query.list().size() > 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 检查该用户是否被禁用
     * @param account
     * @return
     */
    @Override
    public boolean checkStatus(String account) {
        String hql = "FROM QaBackUser bu where bu.account=? and bu.status = 1";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        query.setString(0,account);
        if(query.list().size() > 0){
            return true;
        }else{
            return false;
        }
    }
}
