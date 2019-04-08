package com.qa.aop;

import com.opensymphony.xwork2.ActionContext;
import com.qa.entity.QaBackUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 3tu on 2019/4/7.
 */

/**
 * 用户禁言后,不能发表文章及回复信息
 */
@Aspect
@Component
public class TalkAspect {

    /**
     * 定义切入点检测的方法
     */
    @Pointcut(value = "execution(* com.qa.action.FrontQuestionAction.*Handle(..))")
//    @Pointcut(value="execution (* com.qa.action.FrontIndexAction.*(..))")
    public void BeforePoint() {}

    /**
     * 如果检测到该用户被禁言,那么用户便不能调用方法
     */
//    @Around(value = "BeforePoint()")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        Object o = null;

        Map session = ActionContext.getContext().getSession();
        HashMap user = (HashMap) session.get("frontUser");
        int status = (int) user.get("status");
        System.out.println("status:"+status);
        if(status == 1){
            o = joinPoint.proceed();
        }else{

        }
        return o;
    }

}
