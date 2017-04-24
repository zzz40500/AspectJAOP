package red.dim.aop

import org.gradle.api.Plugin;
import org.gradle.api.Project
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.bundling.Jar
import red.dim.aop.transform.AspectJAppTransform
import red.dim.aop.transform.AspectJLibResourceTransform
import red.dim.aop.transform.AspectJLibTransform
import red.dim.aop.transform.task.JarAspectJTask

/**
 * Created by dim on 17/4/15.
 */

public class AspectJPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        def isApp = project.plugins.withType(AppPlugin)
        def isLib = project.plugins.withType(LibraryPlugin)
        def javaLib = project.plugins.withType(JavaPlugin)
        if (isApp) {
            JarHolder jarHolder = new JarHolder();
            AspectJAppTransform aspectJTransform = new AspectJAppTransform(project, jarHolder);
            project.android.registerTransform(aspectJTransform)
            project.afterEvaluate {
                jarHolder.collect(project);
            }
        } else if (isLib) {
            AspectJLibResourceTransform aspectJLibResourceTransform = new AspectJLibResourceTransform(project);
            AspectJLibTransform aspectJLibTransform = new AspectJLibTransform(project, aspectJLibResourceTransform);
            project.android.registerTransform(aspectJLibTransform)
            project.android.registerTransform(aspectJLibResourceTransform)
        } else if (javaLib) {
            Jar jar = project.tasks.findByName("jar");
            jar.doLast {
                new JarAspectJTask().execute(jar);
            }
        }

    }
}
