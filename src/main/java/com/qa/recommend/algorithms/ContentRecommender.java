package com.qa.recommend.algorithms;

/**
 * Created by 3tu on 2019/5/13.
 */

import com.qa.dao.QaBackQuesDao;
import com.qa.recommend.reWrite.Keyword;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Logger;

/**
 * 基于内容的推荐
 *  思路：提取用户已有的关键词列表，与每个话题匹配的问题关键词（tf-idf）列表，做关键词相似度计算，取最相似的N个问题推荐给用户。
 */
@Component("ContentRecommender")
public class ContentRecommender {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private QaBackQuesDao qaBackQuesDao;


    /**
     * 根据用户的id，找出用户的画像集合
     * 根据画像集合找出相似问题
     * 返回问题id
     * @param userId
     */
    public void recommend(int userId){

        TFIDF tf = new TFIDF();

        // 读取用户画像
        String userInfo = "profile:"+userId;
        String profile = (String) redisTemplate.opsForValue().get(userInfo);

        // 用户已经读取过的记录
        String userIdentity = "userLog:userId_"+userId;
        List logList =redisTemplate.opsForList().range(userIdentity,0,-1);

        // 将读取的信息转换为keyword形式
        Map<Integer, ArrayList<Keyword>> userprofile = this.stringToKeyWord(profile);

        System.out.println("用户画像："+userprofile);
        System.out.println("用户浏览记录："+logList);



        // 获取用户感兴趣的话题下的所有问题标题
       Map<Integer,Double> quesIds = new HashMap<>();

        // 读取用户感兴趣的话题列表
        for(Map.Entry<Integer, ArrayList<Keyword>> entry : userprofile.entrySet()){

            ArrayList quesList = (ArrayList) qaBackQuesDao.getQuesByTopicId(entry.getKey(),logList);


            double cos = 0;

            // 计算dfidf
            for(int i = 0; i < quesList.size(); i++){
                Object[] obj = (Object[]) quesList.get(i);
                int qId = (int) obj[1];
                List<Keyword> key = tf.getTFIDF((String) obj[0], 5);      // 获取TFIDF
                System.out.println(qId+":"+key);
                cos = this.getMatchValue(entry.getValue(),key);
                if(cos != 0){
                    quesIds.put(qId,cos);
                }
            }
        }

        List<Map.Entry<Integer, Double>> infoIds = new ArrayList<>( quesIds.entrySet());
        //排序
        Collections.sort(infoIds, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1,
                               Map.Entry<Integer, Double> o2) {
                return (int) (o2.getValue() - o1.getValue());
            }
        });

        System.out.println("问题关键字："+quesIds);

    }


    // 将字符串转换为keyword
    private Map<Integer, ArrayList<Keyword>> stringToKeyWord(String str){

        Map<Integer, ArrayList<Keyword>> profile = new HashMap<>();

//        System.out.println("初始："+str);

        // 转换为json数据
        String jsonStr = str.replace("[","{");
        jsonStr = jsonStr.replace("=",":");
        jsonStr = jsonStr.replace("]","}");
        jsonStr = jsonStr.replace("/",":");
//        System.out.println("js:"+jsonStr);

        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
//        System.out.println("xxx1"+jsonObject);

        // 遍历所有key
        Iterator<String> it = jsonObject.keys();

        while (it.hasNext()) {
            String key = it.next();
            JSONObject ob = jsonObject.getJSONObject(key);
//            System.out.println(key);
//            System.out.println("ob:"+ob);
            Iterator<String> it1 = ob.keys();
            ArrayList<Keyword> list  = new ArrayList<>();
            while(it1.hasNext()){
                String key1 = it1.next();
                Object value =  ob.get(key1);
                Keyword keyword = new Keyword(key1,Double.parseDouble(String.valueOf(value)));
                list.add(keyword);
//                System.out.println(key1+":"+value);
            }
            profile.put(Integer.valueOf(key),list);
        }

//        System.out.println(profile);


        return profile;
    }




    /**
     * 获得用户的关键词列表和新闻关键词列表的匹配程度
     *
     * @return
     */
    public double getMatchValue(List<Keyword> userList,  List<Keyword> quesList){

//        System.out.println("list:"+userList);
//        System.out.println("ques:"+quesList);

        int len = userList.size()>quesList.size()?userList.size():quesList.size();

        double f1=0;        //  v1*v2
        double f2=0;        // v1的模
        double f3=0;        // v2的模

        double cos = 0;     // 余弦值

        double v1;
        double v2;
        for(int i = 0; i < len; i++){
            v1 = userList.size()<len?0:userList.get(i).getScore();
            v2 = quesList.size()<len?0:quesList.get(i).getScore();
            f1 += v1 * v2;
            f2 += Math.sqrt(v1*v1);
            f3 += Math.sqrt(v2*v2);
        }

        cos = f1/f2*f3;

        return cos;
    }

}
