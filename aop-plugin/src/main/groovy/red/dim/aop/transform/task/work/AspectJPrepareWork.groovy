package red.dim.aop.transform.task.work

import red.dim.aop.aop.AspectJPrepareBuilder

import java.util.concurrent.Callable;


import red.dim.aop.common.ThreadPool;

/**
 * Created by dim on 17/4/18.
 */

public class AspectJPrepareWork {

    private List<TaskBuild> taskBuildList = new ArrayList<>();
    private List<String> outputAspectJ = new ArrayList<>();

    public AspectJPrepareWork addTask(TaskBuild taskBuild) {
        taskBuildList.add(taskBuild);
        return this;
    }

    public void execute() {
        ThreadPool threadPool = new ThreadPool();
        for (TaskBuild taskBuild : taskBuildList) {
            final TaskBuild item = taskBuild;
            threadPool.submit(new Callable() {
                @Override
                public Object call() throws Exception {
                    if (item instanceof TaskBuild.JarTaskBuildConfig) {
                        outputAspectJ.addAll(new AspectJPrepareBuilder.AspectJJarPrepareProcessBuilder()
                                .inputJar(((TaskBuild.JarTaskBuildConfig) item).inputJar)
                                .outDir(((TaskBuild.JarTaskBuildConfig) item).outputJar)
                                .build());

                    } else if (item instanceof TaskBuild.DirListTaskBuildConfig) {
                        outputAspectJ.addAll(new AspectJPrepareBuilder
                                .AspectJDirPrepareProcessBuilder().inputDir(((TaskBuild.DirListTaskBuildConfig) item).inputDir)
                                .outDir(((TaskBuild.DirListTaskBuildConfig) item).outputDir)
                                .binaryFiles(((TaskBuild.DirListTaskBuildConfig) item).binaryFiles)
                                .build());
                    }
                    return null;
                }
            });
        }
        threadPool.invokeAll();
    }

    public List<String> getOutputAspectJ() {
        return outputAspectJ;
    }
}
