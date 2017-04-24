package red.dim.aopexample;

import android.util.Log;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by dim on 17/4/23.
 */

@Aspect
public class Aop {

    private static final String TAG = "Aop";

    @Pointcut("execution( void red.dim.aopexample.AOPClick.projectClick())")
    public void projectClick() {
    }

    @Before("projectClick()")
    public void aop() {
        Log.d(TAG, " aop Before");
    }

}
