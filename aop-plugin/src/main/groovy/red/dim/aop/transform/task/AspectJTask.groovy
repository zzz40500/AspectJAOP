package red.dim.aop.transform.task;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.Status;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;

import org.apache.commons.io.FileUtils
import org.gradle.api.Project;
import org.gradle.api.tasks.compile.JavaCompile;

import red.dim.aop.AspectJException;
import red.dim.aop.JarHolder;
import red.dim.aop.transform.task.work.AspectWork;
import red.dim.aop.transform.task.work.TaskBuild;

import static com.android.build.api.transform.Status.ADDED;
import static com.android.build.api.transform.Status.CHANGED;
import static com.android.build.api.transform.Status.REMOVED;

/**
 * Created by dim on 17/4/19.
 */

public class AspectJTask {

    Project project;
    JarHolder jarHolders;
    AspectJPrepareTask aspectJPrepareTask;

    AspectJTask(Project project, JarHolder jarHolders, AspectJPrepareTask aspectJPrepareTask) {
        this.project = project
        this.jarHolders = jarHolders
        this.aspectJPrepareTask = aspectJPrepareTask
    }

    public void execute(TransformInvocation transformInvocation, boolean incremental) {
        int index = transformInvocation.context.path.indexOf("For");
        String buildFlavor = transformInvocation.context.path.substring(index + 3);
        JavaCompile javaCompile = project.tasks.findByName("compile${buildFlavor}JavaWithJavac");
        if (javaCompile == null) {
            throw new AspectJException("not found JavaCompile");
        }
        File logs = new File(project.buildDir.absolutePath + File.separator + "outputs" + File.separator + "logs");
        if (!logs.exists()) {
            logs.mkdirs()
        }
        AspectWork aspectWork = new AspectWork(javaCompile.options.encoding, javaCompile.targetCompatibility, javaCompile.sourceCompatibility, logs.absolutePath);
        File aspectRootPath = new File(aspectJPrepareTask.getAspectRootPath());
        def subFilePaths = aspectRootPath.listFiles();
        if (subFilePaths != null && subFilePaths.size() > 0) {
            for (File aspectRootFile : subFilePaths) {
                aspectWork.addAspectPath(aspectRootFile.absolutePath)
            }
        }
        if (incremental) {
            for (TransformInput transformInput : transformInvocation.inputs) {
                for (DirectoryInput directoryInput : transformInput.directoryInputs) {
                    File dir = transformInvocation.getOutputProvider().getContentLocation(directoryInput.name, directoryInput.getContentTypes(), directoryInput.getScopes(), Format.DIRECTORY);
                    Set<String> binaryFiles = new HashSet<>();
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
                                binaryFiles.add(file.path);
                                break;
                            default:
                                break;
                        }
                    }
                    if (binaryFiles.size() > 0)
                        aspectWork.addTask(
                                new TaskBuild.DirListTaskBuildConfig(binaryFiles, directoryInput.file.path, dir.path));
                }
                for (JarInput jarInput : transformInput.jarInputs) {
                    JarHolder.ClassJar classJar = jarHolders.proceesPath(jarInput.file.path);
                    File targetPath;
                    if (classJar != null) {
                        targetPath = transformInvocation.outputProvider.getContentLocation(classJar.id,
                                jarInput.contentTypes, jarInput.scopes, Format.JAR);
                    } else {
                        targetPath = transformInvocation.outputProvider.getContentLocation(jarInput.name,
                                jarInput.contentTypes, jarInput.scopes, Format.JAR);
                    }
                    switch (jarInput.status) {
                        case REMOVED:
                            FileUtils.forceDeleteOnExit(targetPath);
                            break;
                        case CHANGED:
                            FileUtils.forceDeleteOnExit(targetPath);
                        case ADDED:
                            aspectWork.addTask(new TaskBuild.JarTaskBuildConfig(jarInput.file.path, targetPath));
                            break;
                        default:
                            break;
                    }
                }
            }

            javaCompile.getClasspath().getFiles().forEach({
                aspectWork.addClassPath(it.absolutePath);
            })
            aspectWork.bootClasspath(javaCompile.getOptions().bootClasspath);
            aspectWork.execute();

        } else {
            transformInvocation.outputProvider.deleteAll();
            for (TransformInput transformInput : transformInvocation.inputs) {
                for (DirectoryInput directoryInput : transformInput.directoryInputs) {
                    File des = transformInvocation.outputProvider.getContentLocation(directoryInput.name,
                            directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY);
                    if (directoryInput.file.size() > 0)
                        aspectWork.addTask(
                                new TaskBuild.DirListTaskBuildConfig(null, directoryInput.file.path, des.path));

                }
                for (JarInput jarInput : transformInput.jarInputs) {
                    JarHolder.ClassJar classJar = jarHolders.proceesPath(jarInput.file.path);
                    File des;
                    if (classJar != null) {
                        des = transformInvocation.outputProvider.getContentLocation(classJar.id,
                                jarInput.contentTypes, jarInput.scopes, Format.JAR);
                    } else {
                        des = transformInvocation.outputProvider.getContentLocation(jarInput.name,
                                jarInput.contentTypes, jarInput.scopes, Format.JAR);
                    }
                    aspectWork.addTask(new TaskBuild.JarTaskBuildConfig(jarInput.file.path, des.path));
                }
            }

            javaCompile.getClasspath().getFiles().forEach({
                aspectWork.addClassPath(it.absolutePath);
            })
            aspectWork.bootClasspath(javaCompile.getOptions().bootClasspath);
            aspectWork.execute();
        }

    }
}
