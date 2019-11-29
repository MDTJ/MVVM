package com.yunda.lib.base_module.aspectj;

import android.util.Log;

import com.yunda.annotation.SingleClick;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Created by mtt on 2019-11-29
 * Describe
 */
@Aspect
public class SingleClickAspect {

    @Pointcut("execution(@com.yunda.annotation.SingleClick * *(..))")
    public void pointcut(){

    }

    @Around("pointcut()&&@annotation(SingleClick)")
    public void aroundJoinPoint(ProceedingJoinPoint pjp,SingleClick SingleClick) throws Throwable{
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        SingleClick singleClick = methodSignature.getMethod().getAnnotation(SingleClick.class);
        long l = System.currentTimeMillis();
        if(l-Common.lastTime>=singleClick.value()){
            Common.lastTime=l;
            pjp.proceed();
        }
    }
    static class Common{
        public static long lastTime=0;

    }
}
