package com.qa.service.Impl;

import com.qa.dao.QaBackUserDao;
import com.qa.entity.QaBackUser;
import com.qa.service.QaBackUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


//注入服务
@Service("QaBackUserService")
public class QaBackUserServiceImpl implements QaBackUserService{

    //自动注入QaBackUserDao,也可以使用@Autowired
    @Resource
    private QaBackUserDao qaUserDao;

    @Override
    public boolean addQbUser(QaBackUser qbUser) {
        this.qaUserDao.add(qbUser);
        return true;
    }

    @Override
    public QaBackUser login(QaBackUser qbUser) {
        return this.qaUserDao.login(qbUser);
    }

    @Override
    public List getAllQbUser() {
        return this.qaUserDao.getQaUserList();
    }

    @Override
    public QaBackUser getQbUserById(int id) {
        return this.qaUserDao.getQaUser(id);
    }

    @Override
    public boolean updateQbUser(QaBackUser qbUser) {
        this.qaUserDao.update(qbUser);
        return true;
    }

    @Override
    public boolean deleteQbUser(int id) {
        this.qaUserDao.delete(id);
        return true;
    }

    @Override
    public Map getBackUserList(String pages, String limit, String name) {
        return qaUserDao.getBackUserList(pages,limit,name);
    }

    @Override
    public boolean saveUserPhoto(int id, String realPath) {
        return qaUserDao.saveUserPhoto(id, realPath);
    }

    @Override
    public boolean banBackUser(int id, String status) {
        return qaUserDao.banBackUser(id,status);
    }

    @Override
    public boolean resetPassWord(int id, String password) {
        return qaUserDao.resetPassWord(id, password);
    }

    @Override
    public boolean updateBackUserTopic(int userId, int topicId) {
        return qaUserDao.updateBackUserTopic(userId, topicId);
    }

    @Override
    public boolean checkAccount(String account) {
        return qaUserDao.checkAccount(account);
    }

    @Override
    public boolean checkStatus(String account) {
        return qaUserDao.checkStatus(account);
    }
}
