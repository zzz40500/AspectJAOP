package red.dim.aop.transform.task.work;

/**
 * Created by dim on 17/4/17.
 */

public interface TaskBuild {

    class JarTaskBuildConfig implements TaskBuild {
        public String inputJar;
        public String outputJar;

        public JarTaskBuildConfig(String inputJar, String outputJar) {
            this.inputJar = inputJar;
            this.outputJar = outputJar;
        }
    }

    class DirListTaskBuildConfig implements TaskBuild {
        public Set<String> binaryFiles;
        public String inputDir;
        public String outputDir;

        public DirListTaskBuildConfig(Set<String> binaryFiles, String inputDir, String outputDir) {
            this.binaryFiles = binaryFiles;
            this.inputDir = inputDir;
            this.outputDir = outputDir;
        }
    }

}
