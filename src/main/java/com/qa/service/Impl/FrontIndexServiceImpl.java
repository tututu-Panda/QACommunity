package com.qa.service.Impl;

import com.qa.dao.FrontIndexDao;
import com.qa.dao.QaBackQuesDao;
import com.qa.entity.BackQuestion;
import com.qa.service.FrontIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.transform.Templates;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *Create by 3tu on 2017/12/28
 */
@Service("FrontIndexService")
public class FrontIndexServiceImpl implements FrontIndexService{

    @Resource
    private FrontIndexDao frontIndexDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public Map getQuesIndex(String pages, String orderTypes, int topic) {
        List<BackQuestion> questionList = new ArrayList<>();
        int page = Integer.parseInt(pages);
        int orderType = Integer.parseInt(orderTypes);
        Map map = this.frontIndexDao.getQuesIndex(page, orderType,topic);
        List list = (List)map.get("list");

        for(int i = 0;i < list.size();i++) {

            Object[] object = (Object[]) list.get(i);
            BackQuestion bq = new BackQuestion();
            bq.setQuesId((Integer) object[0]);
            bq.setQuesTitle((String) object[1]);
            bq.setCreateDate((Date) object[2]);
            bq.setToId((Integer)object[3]);
            bq.setTopicName((String)object[4]);
            bq.setId((int)(object[5]));
            bq.setAccountName((String)object[6]);
            bq.setHeadPhoto((String)object[7]);
            bq.setCommentCount((BigInteger) object[8]);
            bq.setBrowseCount((int)object[9]);

//            // redis获取该问题id的浏览量,如果没有再进行查询添加
//            if(redisTemplate.opsForValue().get(("shadow:views_"+object[0])) != null){
//                System.out.println("获取缓存---");
//                bq.setBrowseCount((int) redisTemplate.opsForValue().get(("views_"+object[0])));
//            }else{
//                System.out.println("添加缓存----");
//                // 查询浏览量,并存入redis
//                int views = frontIndexDao.getViews((Integer) object[0]);
//                // 设置一个shadowkey用户过期事件回调
//                // 在回调事件中,通过key并同步到数据库中
//                redisTemplate.opsForValue().set("shadow:views_"+object[0],"",1, TimeUnit.DAYS);
//                redisTemplate.opsForValue().set("views_"+object[0],views);
//                bq.setBrowseCount(views);
//            }

            questionList.add(bq);
        }


        map.remove("list");
        map.put("quesLists", questionList);
//        System.out.println("123");
//        System.out.println(map.get("quesLists"));
        return map;

    }

    public Map getTheQuesInfo(int quesId) {

        List<BackQuestion> question = new ArrayList<>();
        Map map = this.frontIndexDao.getTheQuesInfo(quesId);
        List list = (List)map.get("list");

        int views;

        // 并发访问浏览量
        // (某时刻刚刚进行redis添加到mysql数据库, 其他线程访问该变量时redis为空, 这样可能导致重置访问量)
        synchronized (this){
        // redis获取该问题id的浏览量,如果没有再进行查询添加
        if(redisTemplate.opsForValue().get(("shadow:views_"+quesId)) != null){
            System.out.println("获取缓存---");
            views = (int) redisTemplate.opsForValue().get(("views_"+quesId));
        }else{
            System.out.println("添加缓存----");
            // 查询浏览量,并存入redis
            views = frontIndexDao.getViews(quesId);
            // 设置一个shadowkey用户过期事件回调
            // 在回调事件中,通过key并同步到数据库中
            redisTemplate.opsForValue().set("shadow:views_"+quesId,"",1, TimeUnit.DAYS);
            redisTemplate.opsForValue().set("views_"+quesId,views);
        }

//        System.out.println("sdadsda:"+redisTemplate.opsForValue().get(("views_"+quesId)));
            // 每次自增1
            redisTemplate.opsForValue().set("views_"+quesId,++views);
        }

        Object[] object = (Object[]) list.get(0);// 每行记录不在是一个对象 而是一个数组
        String labelIds = (String) object[3];
        String[] labelNames = this.frontIndexDao.LabelList(labelIds);  //调用函数获取每个问题所对应的标签信息



        //新建实例，并赋值
        BackQuestion bqt = new BackQuestion();
        bqt.setQuesId((Integer) object[0]);
        bqt.setQuesTitle((String) object[1]);
        bqt.setQuesDetail((String) object[2]);
        bqt.setLabels(labelNames);
        bqt.setCreateDate((Date) object[4]);
        bqt.setToId((Integer) object[5]);
        bqt.setTopicName((String) object[6]);
        bqt.setAccountName((String) object[7]);
        bqt.setHeadPhoto((String)object[8]);
        bqt.setCommentCount((BigInteger)object[9]);
        bqt.setBrowseCount(views);
        bqt.setId((int)object[11]);
        //将对象实例添加进入map集合
        question.add(bqt);


        map.remove("list");
        map.put("question",question);

        return map;
    }

    public Map getTheOneComm(int q_id) {

        return null;
    }

    public Map getTheTwoComm(int pq_id) {
        return null;
    }

    @Override
    public Map getTopicIndex() {
        Map map = this.frontIndexDao.getTopicIndex();
        return map;
    }

    @Override
    public List getReplyRank() {

        // 利用 redis 设置缓存周榜
        if(redisTemplate.opsForList().size("rank") == 0){
            List list = frontIndexDao.getReplyRank();
            redisTemplate.opsForList().rightPush("rank",list);
            redisTemplate.expire("rank",7, TimeUnit.DAYS);
            System.out.println("正在缓存---");
        }
//       List list = redisTemplate.opsForList().range("rank", 0 , redisTemplate.opsForList().size("rank"));
//        List ob = (List) list.get(0);
//        System.out.println("ob++++++"+ob.get(0));
            return (List) redisTemplate.opsForList().range("rank", 0, redisTemplate.opsForList().size("rank")).get(0);
    }

    @Override
    public List getRandomQues() {
        return frontIndexDao.gerRandomQues();
    }
}
