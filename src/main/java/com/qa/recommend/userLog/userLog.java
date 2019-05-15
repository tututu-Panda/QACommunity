package com.qa.recommend.userLog;

/**
 * Created by 3tu on 2019/5/9.
 */

import com.qa.dao.FrontIndexDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 用户浏览记录相关操作
 */

@Component("userLog")
public class userLog {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private FrontIndexDao frontIndexDao;

    /**
     * 添加用户浏览记录到redis中
     */
    public void addUserLog(int userId, int quesId) {

        String userIdentity = "userLog:userId_" + userId;

        List list = redisTemplate.opsForList().range(userIdentity, 0, -1);

        // 问题id去重
        if (!list.contains(quesId)) {
            // 更新当前用户的浏览数据（最多不超过5个）
            if (redisTemplate.opsForList().size(userIdentity) >= 5) {
                redisTemplate.opsForList().rightPop(userIdentity);
            }
            redisTemplate.opsForList().leftPush(userIdentity, quesId);

        }
    }


    /**
     * 获取用户浏览记录列表
     */
    public Map getUserLogById(int userId){
        String userIdentity = "userLog:userId_"+userId;

        Map articleList ;
        List t ;
        String title ;
        int topic ;

        List list = redisTemplate.opsForList().range(userIdentity,0,-1);

        // "（话题id-》标题）"
        HashMap<Integer, LinkedList<String>> map = new HashMap<>();
//        HashMap<Integer, HashMap<Integer, String>> map = new HashMap<>();
//        LinkedList<String> lnk = new LinkedList<>();

        // 遍历每个问题
        for(int i = 0; i< list.size(); i++){
            LinkedList<String> lnk;
//            // 存储“话题id-标题”
//            HashMap<Integer, String> temp = new HashMap<>();

            int id = (int) list.get(i);       // 问题编号
            articleList =  frontIndexDao.getTheQuesInfo(id);     // 问题详情
            t = (List) articleList.get("list");

            Object[] ob = (Object[])  t.get(0);     // 问题对象
            title = (String) ob[1];
            topic = (Integer) ob[5];

            if(map.get(topic) == null){
                lnk = new LinkedList<>();
            }else{
               lnk = map.get(topic);
            }

            lnk.add(title);

            map.put(topic,lnk);

        }
       return map;
    }

}
