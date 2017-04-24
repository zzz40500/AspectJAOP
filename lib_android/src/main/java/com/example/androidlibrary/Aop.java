package com.example.androidlibrary;



import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;



/**
 * Created by dim on 16/10/19.
 */
@Aspect
public class Aop {

    private static final String TAG = "Aop";

    @Pointcut("execution( void red.dim.aopexample.AOPClick.androidLibClick())")
    public void androidLibClick() {
    }

    @Before("androidLibClick()")
    public void aop() {
        CCC.d();
    }
}
