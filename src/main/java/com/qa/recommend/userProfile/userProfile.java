package com.qa.recommend.userProfile;

/**
 * Created by 3tu on 2019/5/9.
 */

import com.qa.recommend.algorithms.TFIDF;
import com.qa.recommend.reWrite.Keyword;
import com.qa.recommend.userLog.userLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 用户的画像相关操作
 */

@Component("userProfile")
public class userProfile {

    //设置TFIDF提取的关键词数目
    private static final int KEY_WORDS_NUM = 10;

    //每日衰减系数
    private static final double DEC_COEE=0.7;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private userLog userlog;


    // 根据浏览记录更新用户画像
    public Map<Integer, ArrayList<Keyword>> refreshUserProfile(int userId) {

        TFIDF tf = new TFIDF();
        Map<Integer, ArrayList<com.qa.recommend.reWrite.Keyword>> allword = new HashMap<>();

        // ① 获取浏览记录
         Map<Integer, LinkedList<String>> map = userlog.getUserLogById(userId);


        // ② 根据浏览记录生成关键字
        for(Map.Entry<Integer, LinkedList<String>> entry : map.entrySet()){
            // 话题-标题
            int topicId = entry.getKey();
            LinkedList<String> list = entry.getValue();

            ArrayList<com.qa.recommend.reWrite.Keyword> kw = new ArrayList<>();

            for (String title:list){
                List<Keyword> key = tf.getTFIDF(title, KEY_WORDS_NUM);      // 获取TFIDF
                kw.addAll(key);     // 加入集合中
            }

            // 对TFIDF值排序（Ketword类已经实现）
            Collections.sort(kw);

            allword.put(topicId,kw);
        }


//        this.productProfile(allword);



        return allword;
    }

        // 根据关键字生成用户画像
    public Map<Integer, ArrayList<Keyword>> productProfile(Map<Integer, ArrayList<Keyword>> words){

        // 画像集合
        Map<Integer, ArrayList<Keyword>> profile = new HashMap<>();

        for(Map.Entry<Integer, ArrayList<Keyword>> entry : words.entrySet()){

            int topicId = entry.getKey();
            ArrayList<Keyword> key= entry.getValue();

            if(key.size() > 5){
                // 需要添加new ，否则会对原集合产生影响
                key = new ArrayList<>(key.subList(0, 5));
            }
            profile.put(topicId,key);
        }

        return profile;


    }

    // 将用户画像加入redis缓存
    public void addProfile(int userId, Map<Integer, ArrayList<Keyword>> profile) {

        System.out.println("将"+userId+"的画像加入redis");
        redisTemplate.opsForValue().set("profile:"+userId,profile.toString());

    }
}
