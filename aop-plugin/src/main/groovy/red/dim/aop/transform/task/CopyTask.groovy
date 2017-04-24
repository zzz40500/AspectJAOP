package red.dim.aop.transform.task

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.Status
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import org.apache.commons.io.FileUtils

import static com.android.build.api.transform.Status.ADDED
import static com.android.build.api.transform.Status.CHANGED
import static com.android.build.api.transform.Status.REMOVED

/**
 * Created by dim on 17/4/21.
 */

public class CopyTask {

    public void execute(TransformInvocation transformInvocation, boolean incremental) {

        if (incremental) {

            for (TransformInput transformInput : transformInvocation.inputs) {
                for (DirectoryInput directoryInput : transformInput.directoryInputs) {
                    File dir = transformInvocation.getOutputProvider().getContentLocation(directoryInput.name, directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY);
                    for (File file : directoryInput.getChangedFiles().keySet()) {
                        Status status = directoryInput.getChangedFiles().get(file);
                        String targetPath = file.getAbsolutePath().replaceFirst(directoryInput.getFile().getAbsolutePath(), dir.getAbsolutePath());
                        switch (status) {
                            case REMOVED:
                                FileUtils.forceDeleteOnExit(new File(targetPath));
                                break;
                            case CHANGED:
                                FileUtils.forceDeleteOnExit(new File(targetPath));
                            case ADDED:
                                FileUtils.copyFile(file, new File(targetPath))
                                break;
                            default:
                                break;
                        }
                    }
                }
                for (JarInput jarInput : transformInput.jarInputs) {
                    File targetPath = transformInvocation.outputProvider.getContentLocation(jarInput.name,
                            jarInput.contentTypes, jarInput.scopes, Format.JAR);
                    switch (jarInput.status) {
                        case REMOVED:
                            FileUtils.forceDeleteOnExit(targetPath);
                            break;
                        case CHANGED:
                            FileUtils.forceDeleteOnExit(targetPath);
                        case ADDED:
                            FileUtils.copyFile(file, targetPath)
                            break;
                        default:
                            break;
                    }
                }
            }


        } else {
            transformInvocation.outputProvider.deleteAll();
            for (TransformInput transformInput : transformInvocation.inputs) {
                for (DirectoryInput directoryInput : transformInput.directoryInputs) {
                    File des = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
                            directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY);
                    FileUtils.copyDirectory(directoryInput.file, des);
                }
                for (JarInput jarInput : transformInput.jarInputs) {
                    File des = transformInvocation.outputProvider.getContentLocation(jarInput.name,
                            jarInput.contentTypes, jarInput.scopes, Format.JAR);
                    FileUtils.copyFile(jarInput.file, des);
                }
            }
        }
    }
}
