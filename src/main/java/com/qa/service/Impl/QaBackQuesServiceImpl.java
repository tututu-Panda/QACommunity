package com.qa.service.Impl;

import com.qa.dao.QaBackQuesDao;
import com.qa.entity.BaCommToQues;
import com.qa.entity.BackQuestion;
import com.qa.entity.QaQuestion;
import com.qa.service.QaBackQuesService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


//注入服务
@Service("QaBackQuesService")
public class QaBackQuesServiceImpl implements QaBackQuesService{

    @Resource
    private QaBackQuesDao qaBackQuesDao;
    @Override
    public boolean addQues(QaQuestion qaQuestion) {
        return false;
    }

    @Override
    public boolean update(QaQuestion qaQuestion) {
        return false;
    }



    @Override
    public QaQuestion getQaQuestion(int l_id) {

        return null;
    }


    /**
     * 返回详细信息问题列表
     * @param pages
     * @param limit
     * @param rangeDate
     * @return
     */
    @Override
    public Map getQuestionList(String pages, String limit, String[] rangeDate, int role) {
        List<BackQuestion> questionList = new ArrayList<BackQuestion>();
        int page = Integer.parseInt(pages);
        int limits = Integer.parseInt(limit);
        Map map = this.qaBackQuesDao.getQuestionList(page, limits,rangeDate, role);
        List list = (List) map.get("list");
        //自定义了BackQuestion类，来存放问题对象
        for(int i = 0;i < list.size();i++) {
            Object[] object = (Object[]) list.get(i);// 每行记录不在是一个对象 而是一个数组

            //新建实例，并赋值
            BackQuestion bqt = new BackQuestion();
            bqt.setQuesId((Integer) object[0]);
            bqt.setQuesTitle((String) object[1]);
            bqt.setQuesDetail((String) object[2]);
            bqt.setCreateDate((Date) object[4]);
            bqt.setToId((Integer) object[5]);
            bqt.setTopicName((String) object[6]);
            bqt.setAccount((String) object[7]);
            bqt.setAccountName((String) object[8]);
            //将对象实例添加进入map集合
            questionList.add(bqt);
        }

        //删除原先的list键集合
        map.remove("list");
        map.put("quesLists",questionList);
        return map;
    }


    /**
     * 通过问题的l_id来获取问题的详细信息
     * @param l_id
     * @return
     */
    public Map getTheQuestion(int l_id) {
        Map map = this.qaBackQuesDao.getTheQuestion(l_id);
        return map;
    }

    /**
     * 根据日期段来获取该问题在该段时间内的浏览量
     * @param qId
     * @param time
     * @return
     */
    public Map getBrowseForDate(int qId, String[] time) {
        Map map = this.qaBackQuesDao.getBrowsDate(qId, time);
        List list = (List) map.get("list");
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        //图表信息，需要返回的数据特定形式，拼装日期和该日期对应的浏览量
        for (int i = 0; i < list.size();i++) {
            Object[] object = (Object[]) list.get(i);
            //对要返回的数据进行处理，如   2,1,1
            if(i != list.size() - 1) {
                sb1.append(object[0]+",");
                sb2.append(object[1]+",");
            }
            else {
                sb1.append(object[0]);
                sb2.append(object[1]);
            }

        }
        String dataString = sb1.toString();  //转化为字符串，下同
        String browCount = sb2.toString();
        map.remove("list");
        map.put("dataString", dataString);
        map.put("browCount", browCount);
        return map;
    }

    /**
     * 获取一级评论
     * @param q_id
     * @return
     */
    public Map getTheComment(int q_id,String page) {
        List<BaCommToQues> baCommToQuesList = new ArrayList<>();
//        qaBackQuesDao.getCountOfSonComment(48);
        Map map = this.qaBackQuesDao.getTheComment(q_id,page);
        List list = (List) map.get("commentList");
        //同理，新建class,获取评论，用户以及赞数
        for(int i = 0;i < list.size();i++) {
            Object[] object = (Object[]) list.get(i);// 每行记录不在是一个对象 而是一个数组
            BaCommToQues bct = new BaCommToQues();
            bct.setCommId((Integer) object[0]);
            bct.setCommentCount(this.qaBackQuesDao.getCountOfSonComment((Integer) object[0]));
            bct.setContent((String) object[1]);
            bct.setCreateDate((Date) object[2]);
            bct.setAccountName((String) object[3]);
            bct.setHeadPhoto((String)object[4]);

            baCommToQuesList.add(bct);

        }
        map.remove("commentList");
        map.put("commList", baCommToQuesList);
        map.put("count", map.get("count"));
        map.put("page", map.get("page"));

        return map;
    }

    /**
     * 二级评论
     * @param pq_id
     * @return
     */
    public Map getTheComment_two(int pq_id) {
        List<BaCommToQues> baCommToQuesList = new ArrayList<>();
        Map map  = this.qaBackQuesDao.getTheComment_two(pq_id);
        List list = (List)map.get("two_commentList");
        //同理，新建class,获取评论，用户以及赞数
        for (Object aList : list) {
            Object[] object = (Object[]) aList;
            BaCommToQues bbt = new BaCommToQues();
            bbt.setCommId((Integer) object[0]);
            bbt.setContent((String) object[1]);
            bbt.setCreateDate((Date) object[2]);
            bbt.setAccountName((String) object[3]);
            bbt.setHeadPhoto((String)object[4]);

            baCommToQuesList.add(bbt);
        }
        map.remove("two_commentList");
        map.put("two_list", baCommToQuesList);
        return map;
    }

    /**
     * 删除问题
     * @param c_ids
     * @return
     */
    @Override
    public boolean deleteQues(List<Integer> c_ids) {
        if(this.qaBackQuesDao.deleteQuestion(c_ids)) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 删除评论
     * @param c_id
     * @return
     */
    public boolean deleteComm(int c_id) {
        if(this.qaBackQuesDao.deleteComment(c_id)) {
            return true;
        }else {
            return false;
        }
    }


    /**
     * 获取审核问题列表
     * @param pages
     * @param limit
     * @param rangeDate
     * @param check
     * @return
     */
    @Override
    public Map getCheckQuestionList(String pages, String limit, String[] rangeDate, int check, int topic) {
        List<BackQuestion> questionList = new ArrayList<BackQuestion>();
        int page = Integer.parseInt(pages);
        int limits = Integer.parseInt(limit);
        Map map = this.qaBackQuesDao.getCheckQuestionList(page, limits,rangeDate,check, topic);
        List list = (List) map.get("list");
        //自定义了BackQuestion类，来存放问题对象
        for(int i = 0;i < list.size();i++) {
            Object[] object = (Object[]) list.get(i);// 每行记录不在是一个对象 而是一个数组

            //新建实例，并赋值
            BackQuestion bqt = new BackQuestion();
            bqt.setQuesId((Integer) object[0]);
            bqt.setQuesTitle((String) object[1]);
            bqt.setQuesDetail((String) object[2]);
//            System.out.println(object[3]);
            bqt.setCreateDate((Date) object[3]);
            bqt.setChecked((Integer) object[4]);
            bqt.setToId((Integer) object[5]);
            bqt.setTopicName((String) object[6]);
            bqt.setAccount((String) object[7]);
            bqt.setAccountName((String) object[8]);
            //将对象实例添加进入map集合
            questionList.add(bqt);
        }

        //删除原先的list键集合
        map.remove("list");
        map.put("quesLists",questionList);
        return map;
    }


    /**
     * 问题审核
     * @param ids
     * @param check
     * @return
     */
    @Override
    public boolean checkQues(List<Integer> ids, int check) {
        if(this.qaBackQuesDao.checkQues(ids,check)) {
            return true;
        }else {
            return false;
        }
    }

}
