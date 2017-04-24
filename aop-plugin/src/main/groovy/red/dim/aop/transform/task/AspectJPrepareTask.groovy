package red.dim.aop.transform.task;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.Status;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;

import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import red.dim.aop.JarHolder;
import red.dim.aop.transform.task.work.AspectJPrepareWork;
import red.dim.aop.transform.task.work.TaskBuild;

import static com.android.build.api.transform.Status.ADDED;
import static com.android.build.api.transform.Status.CHANGED;
import static com.android.build.api.transform.Status.REMOVED;
import static com.android.builder.model.AndroidProject.FD_INTERMEDIATES;

/**
 * Created by dim on 17/4/19.
 */

public class AspectJPrepareTask {

    Project project;
    JarHolder jarHolders;
    AspectJPrepareWork aspectJPrepareWork;
    String aspectRootPath;

    AspectJPrepareTask(Project project, JarHolder jarHolders) {
        this.project = project
        this.jarHolders = jarHolders
    }

    public void execute(TransformInvocation transformInvocation, boolean incremental) {
        aspectJPrepareWork = new AspectJPrepareWork();
        int index = transformInvocation.context.path.indexOf("For");
        String buildFlavor = transformInvocation.context.path.substring(index + 3);
        aspectRootPath = project.getBuildDir().path + "/" + FD_INTERMEDIATES + "/aspectPath/" + buildFlavor;
        File aspectPath = new File(aspectRootPath);
        if (!aspectPath.exists()) {
            aspectPath.mkdirs()
        }
        if (incremental) {
            for (TransformInput transformInput : transformInvocation.inputs) {
                for (DirectoryInput directoryInput : transformInput.directoryInputs) {
                    File des = new File(aspectPath, "class" + directoryInput.name)
                    Set<String> binaryFiles = new HashSet<>();
                    for (File file : directoryInput.getChangedFiles().keySet()) {
                        Status status = directoryInput.getChangedFiles().get(file);
                        switch (status) {
                            case REMOVED:
                                FileUtils.forceDeleteOnExit(des);
                                break;
                            case CHANGED:
                                FileUtils.forceDeleteOnExit(des);
                            case ADDED:
                                binaryFiles.add(file.path);
                                break;
                            default:
                                break;
                        }
                    }
                    aspectJPrepareWork.addTask(
                            new TaskBuild.DirListTaskBuildConfig(binaryFiles, directoryInput.file.path, des.path));
                }

                for (JarInput jarInput : transformInput.jarInputs) {
                    File des = new File(aspectPath, jarInput.name)

                    switch (jarInput.status) {
                        case REMOVED:
                            FileUtils.forceDeleteOnExit(des);
                            break;
                        case CHANGED:
                            FileUtils.forceDeleteOnExit(des);
                        case ADDED:
                            aspectJPrepareWork.addTask(new TaskBuild.JarTaskBuildConfig(jarInput.file.path, des.path));
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
                    File des = new File(aspectPath, directoryInput.name)
                    aspectJPrepareWork.addTask(new TaskBuild.DirListTaskBuildConfig(null, directoryInput.file.path, des.path))
                }
                for (JarInput jarInput : transformInput.jarInputs) {
                    File des = new File(aspectPath, jarInput.name)
                    aspectJPrepareWork.addTask(new TaskBuild.JarTaskBuildConfig(jarInput.file.path, des.path));
                }
            }
        }
        aspectJPrepareWork.execute();
    }


    public String getAspectRootPath() {
        return aspectRootPath;
    }

    public boolean isChanged() {
        return aspectJPrepareWork != null && aspectJPrepareWork.outputAspectJ.size() > 0;
    }
}
