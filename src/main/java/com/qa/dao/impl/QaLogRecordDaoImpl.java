package com.qa.dao.impl;

import com.qa.dao.QaLogRecordDao;
import com.qa.entity.QaLogRecord;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
// 进行注入
@Repository("QaLogRecordDao")
public class QaLogRecordDaoImpl implements QaLogRecordDao {

    // 注入sessionFactory
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;


    /**
     *
     * 描述：根据传递过来的logRecord实体,将其添加到数据库中
    */
    @Override
    public void addLog(QaLogRecord logRecord) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.save(logRecord);
        transaction.commit();
        session.close();
    }

    /**
     *
     * 描述：根据id集合,删除日志信息
    */
    @Override
    public Boolean deleteLog(List<Integer> logRecords) {
        boolean b = false;
        String hql = "Delete from QaLogRecord where id in (:ids)";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);

        int result = query.setParameterList("ids", logRecords).executeUpdate();
        if(result != 0){
            b = true;
        }
        return b;
    }

    /**
     *
     * 描述：根据传入的条件集合,进行日志查找
    */
    @Override
    public Map queryLog(String pages, int limit, String[] time) {

        int firstRe = 0;            // 查询的第一个结果
        int lastRe = 0;             // 查询的最后一个结果
        int count = 0;              // 查询到总数
        int page = 0;
        int lim = 0;

        Timestamp times[] = new Timestamp[2];
        Map map = new HashMap();

        // 如果时间为空,则给予默认值
        if(time[0].equals("")){
            times[0] = Timestamp.valueOf("1999-01-01 00:00:00");
            times[1] = new Timestamp(new Date().getTime());
        }else{
            times[0] = Timestamp.valueOf(time[0]);
            times[1] = Timestamp.valueOf(time[1]);
        }

        // 将字符串转换为int
        page = Integer.parseInt(pages) - 1;
        lim = limit;

//        System.out.println("page:"+pages);
//        System.out.println("lim:"+lim);
//        System.out.println("time:"+time);
//        System.out.println("time1:"+times[0]+"times2:"+times[1]);

        String hql = "FROM QaLogRecord log WHERE createDate BETWEEN ? AND ?";
        Query query = sessionFactory.getCurrentSession().createQuery(hql);  // 用于数据截取
        query.setTimestamp(0,times[0]);
        query.setTimestamp(1,times[1]);
        // 得到数据长度
        List list = query.list();
        count = list.size();

        // 设置查询第一个数据以及查询的数量
        firstRe = page * lim;
        query.setFirstResult(firstRe);
        query.setMaxResults(limit);
         list = query.list();

        map.put("list",list);
        map.put("count",count);
        return map;
    }
}
