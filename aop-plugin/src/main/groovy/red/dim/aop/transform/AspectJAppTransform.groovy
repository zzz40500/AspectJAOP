package red.dim.aop.transform

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project
import red.dim.aop.JarHolder
import red.dim.aop.common.L
import red.dim.aop.transform.task.AspectJPrepareTask
import red.dim.aop.transform.task.AspectJTask
import red.dim.aop.transform.task.JarAnalyseTask


/**
 * Created by dim on 17/4/15.
 */

public class AspectJAppTransform extends Transform {

    Project project;
    JarHolder jarHolders;

    AspectJAppTransform(Project project, JarHolder jarHolders) {
        this.project = project
        this.jarHolders = jarHolders
    }

    @Override
    String getName() {
        return "aspectj"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        long start = System.currentTimeMillis();
        JarAnalyseTask jarAnalyseTask = new JarAnalyseTask(jarHolders);
        AspectJPrepareTask aspectJPrepareTask = new AspectJPrepareTask(project, jarHolders);
        AspectJTask aspectJTask = new AspectJTask(project, jarHolders, aspectJPrepareTask);
        jarAnalyseTask.execute(transformInvocation, transformInvocation.incremental)
        aspectJPrepareTask.execute(transformInvocation, transformInvocation.incremental)
        aspectJTask.execute(transformInvocation, transformInvocation.incremental && !aspectJPrepareTask.changed)
        L.log("aspectj time [ " + (System.currentTimeMillis() - start) + " ms]");
    }
}
