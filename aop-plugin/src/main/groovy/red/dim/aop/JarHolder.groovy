package red.dim.aop

import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.artifacts.ResolvedDependency

import static com.android.SdkConstants.EXT_JAR
import static com.android.builder.model.AndroidProject.FD_INTERMEDIATES

/**
 * Created by dim on 17/4/15.
 */

public class JarHolder {

    private List<ClassJar> jarList = new ArrayList<>();
    private Project project;

    public void collect(Project project) {
        this.project = project;
        project.configurations.forEach({
            it.resolvedConfiguration.firstLevelModuleDependencies.forEach({
                collocate(it);
            })
        })
    }

    public void collocate(ResolvedDependency resolvedDependency) {
        resolvedDependency.children.forEach({
            collocate(it)
        })
        resolvedDependency.allModuleArtifacts.forEach({
            collocate(it)
        })
    }

    public void collocate(ResolvedArtifact artifact) {
        if (EXT_JAR == artifact.getExtension()) {
            jarList.add(new ClassJar(artifact));
        }
    }

    public ClassJar proceesPath(String path) {
        for (ClassJar classJar : jarList) {
            if (classJar.file == path) {
                return classJar;
            }
        }
        ClassJar classJar = parseClassJarFromPath(path);
        if (classJar != null) {
            jarList.add(classJar);
        }
        return classJar;
    }

    public ClassJar getClassJar(String group, String name) {
        for (ClassJar classJar : jarList) {
            if (classJar.group == group && classJar.name == name) {
                return classJar;
            }
        }
        return null;

    }

    public ClassJar parseClassJarFromPath(String path) {
        String exploded = project.getBuildDir().path + "/" + FD_INTERMEDIATES + "/exploded-aar/";
        String classJarEnd = "/jars/classes.jar";
        if (path.startsWith(exploded) && path.endsWith(classJarEnd)) {
            String id = path.substring(exploded.length(), path.length() - classJarEnd.length());
            String[] infos = id.split("/");
            if (infos.length == 3) {
                return new ClassJar(path, infos[2], infos[0], infos[1], null);
            } else if (infos.length == 4) {
                return new ClassJar(path, infos[2], infos[0], infos[1], infos[3]);
            }
            return null;
        }
    }

    public static class ClassJar {
        ClassJar(String file, String version, String group, String name, String classifier) {
            this.file = file
            this.version = version
            this.group = group
            this.name = name
            this.classifier = classifier
            id = "";
            if (!isEmptyString(group)) {
                id += "_" + group
            }
            if (!isEmptyString(name)) {
                id += "_" + name
            }
            if (!isEmptyString(version)) {
                id += "_" + version
            }
            if (!isEmptyString(classifier)) {
                id += "_" + classifier
            }
        }

        public ClassJar(ResolvedArtifact artifact) {
            file = artifact.file.path;
            version = artifact.moduleVersion.id.version
            group = artifact.moduleVersion.id.group
            name = artifact.moduleVersion.id.name
            classifier = artifact.classifier
            id = "";
            if (!isEmptyString(group)) {
                id += "_" + group
            }
            if (!isEmptyString(name)) {
                id += "_" + name
            }
            if (!isEmptyString(version)) {
                id += "_" + version
            }
            if (!isEmptyString(classifier)) {
                id += "_" + classifier
            }
        }
        String file;
        String version;
        String group;
        String name;
        String classifier;
        String id;

        public boolean isEmptyString(String text) {
            return text == null || text.trim().length() == 0;
        }
    }
}
