package red.dim.aop.common;

import java.util.logging.Logger;

/**
 * Created by dim on 17/4/20.
 */

public class L {

    static Logger logger = Logger.getLogger("fastAspectJ ");
    public static void log(String txt) {
//        logger.
        System.out.println("[fastAspectJ] " + txt);
    }

}
