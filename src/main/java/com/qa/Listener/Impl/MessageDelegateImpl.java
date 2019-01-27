package com.qa.Listener.Impl;

import com.qa.Listener.MessageDelegate;
import com.qa.dao.FrontIndexDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by 3tu on 2019/1/27.
 */
public class MessageDelegateImpl implements MessageDelegate {

    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private FrontIndexDao frontIndexDao;

    @Override
    public void handleMessage(String message) {
        // 获取字符串
        StringBuffer str = new StringBuffer(message).delete(0,7);
        int num = str.indexOf("_");
        String st = str.substring(0, num);
       int id = Integer.parseInt((String) str.subSequence(num+1,str.length()));
        System.out.println("mes:------"+st);

        // 浏览量
        if(st .equals("views")){
            // 首先获取redis 中的缓存
            int views = (int) redisTemplate.opsForValue().get(new String(str));
            // 操作存储到数据库中
            System.out.println("--"+id);
            System.out.println("--"+views);
            frontIndexDao.updateViews(id, views);
        }

    }


    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public FrontIndexDao getFrontIndexDao() {
        return frontIndexDao;
    }

    public void setFrontIndexDao(FrontIndexDao frontIndexDao) {
        this.frontIndexDao = frontIndexDao;
    }
}
