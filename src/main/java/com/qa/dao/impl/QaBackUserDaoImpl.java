package com.qa.dao.impl;

import com.qa.dao.QaBackUserDao;
import com.qa.entity.QaBackUser;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

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
}
