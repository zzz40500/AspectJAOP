package com.dim.proxy;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;


/**
 * Created by dim on 16/10/19.
 */
@Aspect
public class Aop {

    private static final String TAG = "Aop";

    @Pointcut("execution( void red.dim.aopexample.AOPClick.javaClick())")
    public void javaClick() {
    }

    @Before("javaClick()")
    public void aop() {
        System.out.println(TAG + "Before: javaClick");
    }
}
