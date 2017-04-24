package red.dim.aop.transform.task.work

import red.dim.aop.aop.AspectJProcessBuilder
import red.dim.aop.common.ThreadPool

import java.util.concurrent.Callable;

/**
 * Created by dim on 17/4/15.
 */

public class AspectWork {

    public Set<String> aspectPath = new HashSet<File>()
    public Set<String> classPath = new HashSet<File>()

    String bootClasspath;
    String encoding = "utf-8";
    String sourceCompatibility = "1.7";
    String targetCompatibility = "1.7";
    String dumpPath;


    public List<TaskBuild> taskBuildList = new ArrayList<>();

    AspectWork(String encoding, String sourceCompatibility, String targetCompatibility, String dumpPath) {
        this.encoding = encoding
        this.sourceCompatibility = sourceCompatibility
        this.targetCompatibility = targetCompatibility
        this.dumpPath = dumpPath;
    }

    AspectWork() {
    }


    public AspectWork addTask(TaskBuild taskBuild) {
        taskBuildList.add(taskBuild);
        return this;
    }

    public AspectWork addAspectPath(String path) {
        aspectPath.add(path);
        return this;
    }

    public AspectWork addClassPath(String path) {
        classPath.add(path);
        return this;
    }

    public AspectWork bootClasspath(String bootClasspath) {
        this.bootClasspath = bootClasspath;
        return this;
    }

    public void execute() {
        ThreadPool threadPool = new ThreadPool();
        for (TaskBuild taskBuild : taskBuildList) {
            final TaskBuild item = taskBuild;
            threadPool.submit(new Callable() {

                @Override
                Object call() throws Exception {
                    try {
                        AspectJProcessBuilder aspectJProcessBuilder = new AspectJProcessBuilder()
                                .encoding(encoding)
                                .classpath(classPath)
                                .sourceCompatibility(sourceCompatibility)
                                .targetCompatibility(targetCompatibility)
                                .dumpPath(dumpPath)
                                .bootClassPath(bootClasspath)
                                .aspectPath(aspectPath)
                        if (item instanceof TaskBuild.JarTaskBuildConfig) {
                            File file = new File(item.outputJar);
                            if (!file.getParentFile().exists()) {
                                file.getParentFile().mkdirs();
                            }
                            aspectJProcessBuilder.inPath(item.inputJar).outputJar(item.outputJar);
                        } else if (item instanceof TaskBuild.DirListTaskBuildConfig) {
                            aspectJProcessBuilder.inPath(item.inputDir).outputDir(item.outputDir);
                            aspectJProcessBuilder.binaryFiles(item.binaryFiles);
                        }
                        aspectJProcessBuilder.build();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null
                }
            })

        }
        threadPool.invokeAll();
    }

}
