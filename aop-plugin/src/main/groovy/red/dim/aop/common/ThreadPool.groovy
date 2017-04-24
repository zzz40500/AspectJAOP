package red.dim.aop.common

import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors

/**
 * Created by dim on 17/4/18.
 */

public class ThreadPool {

    static ExecutorService sExecutor = Executors.newFixedThreadPool(Runtime.runtime.availableProcessors() + 1);
    private List<Callable> callableList = new ArrayList<>();

    public void submit(Callable callable) {
        callableList.add(callable);
    }

    public void invokeAll() {
        sExecutor.invokeAll(callableList);
        callableList.clear();
    }

}
