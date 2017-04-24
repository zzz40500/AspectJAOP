package red.dim.aop.transform.task

import com.android.build.api.transform.JarInput
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import red.dim.aop.JarHolder;

/**
 * Created by dim on 17/4/19.
 */

public class JarAnalyseTask {
    JarHolder jarHolders;

    JarAnalyseTask(JarHolder jarHolders) {
        this.jarHolders = jarHolders
    }

    public void execute(TransformInvocation transformInvocation, boolean incremental) {
        for (TransformInput transformInput : transformInvocation.inputs) {
            for (JarInput jarInput : transformInput.jarInputs) {
                jarHolders.proceesPath(jarInput.file.path);
            }
        }
    }
}
