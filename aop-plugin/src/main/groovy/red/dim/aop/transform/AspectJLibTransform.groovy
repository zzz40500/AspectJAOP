package red.dim.aop.transform

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.google.common.collect.ImmutableSet
import org.gradle.api.Project
import red.dim.aop.transform.task.AspectJPrepareTask
import red.dim.aop.transform.task.CopyTask


/**
 * Created by dim on 17/4/20.
 */

public class AspectJLibTransform extends Transform {

    AspectJLibResourceTransform aspectJLibResourceTransform
    Project project;

    AspectJLibTransform(Project project, AspectJLibResourceTransform aspectJLibResourceTransform) {
        this.aspectJLibResourceTransform = aspectJLibResourceTransform
        this.project = project
    }

    @Override
    String getName() {
        return "aspectjLibClass"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return ImmutableSet.of(QualifiedContent.DefaultContentType.CLASSES)
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return ImmutableSet.of(QualifiedContent.Scope.PROJECT)
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        AspectJPrepareTask aspectJPrepareTask = new AspectJPrepareTask(project, null);
        aspectJPrepareTask.execute(transformInvocation, transformInvocation.incremental)
        aspectJLibResourceTransform.setAspectRootPath(new File(aspectJPrepareTask.aspectRootPath))
        new CopyTask().execute(transformInvocation, transformInvocation.incremental);
    }
}
