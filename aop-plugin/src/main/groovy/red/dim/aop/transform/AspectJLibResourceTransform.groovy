package red.dim.aop.transform

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.google.common.collect.ImmutableSet
import com.google.common.collect.Lists
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import red.dim.aop.common.NoticeFileHelper

import static com.android.builder.model.AndroidProject.FD_INTERMEDIATES

/**
 * Created by dim on 17/4/20.
 */

public class AspectJLibResourceTransform extends Transform {

    private static final String NOTICE = NoticeFileHelper.NOTICE;

    private File aspectRootPath;

    Project project;

    AspectJLibResourceTransform(Project project) {
        this.project = project
    }

    void setAspectRootPath(File aspectRootPath) {
        this.aspectRootPath = aspectRootPath
    }

    @Override
    String getName() {
        return "aspectjLibResource"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return ImmutableSet.of(QualifiedContent.DefaultContentType.RESOURCES)
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return ImmutableSet.of(QualifiedContent.Scope.PROJECT)
    }

    @Override
    boolean isIncremental() {
        return false;
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        if (aspectRootPath == null) {
            int index = transformInvocation.context.path.indexOf("For");
            String buildFlavor = transformInvocation.context.path.substring(index + 3);
            aspectRootPath = new File(project.getBuildDir().path + "/" + FD_INTERMEDIATES + "/aspectPath/" + buildFlavor);
        }
        transformInvocation.getOutputProvider().deleteAll();
        File resourceFile;
        boolean hasNotice = false;
        for (TransformInput input : transformInvocation.getInputs()) {
            for (DirectoryInput dir : input.directoryInputs) {
                File outputDir = transformInvocation.outputProvider.getContentLocation(dir.name, dir.contentTypes, dir.scopes,
                        Format.DIRECTORY);
                copyDir(dir, outputDir)
                if (resourceFile == null) {
                    resourceFile = outputDir;
                }
                FileUtils.copyDirectory(dir, outputDir);
                File notice = new File(outputDir, NOTICE);
                if (notice.exists()) {
                    hasNotice = true;
                    NoticeFileHelper.appendNoticeFile(aspectRootPath, notice);
                }
            }

        }
        if (!hasNotice) {
            if (resourceFile == null) {
                resourceFile = new File(transformInvocation.outputProvider.getContentLocation("resource", ImmutableSet.of(QualifiedContent.DefaultContentType.RESOURCES), ImmutableSet.of(QualifiedContent.Scope.PROJECT),
                        Format.DIRECTORY), NOTICE)
                resourceFile.deleteOnExit()
            }
            if (!resourceFile.getParentFile().exists()) {
                resourceFile.getParentFile().mkdirs();
            }
            NoticeFileHelper.appendNoticeFile(aspectRootPath, resourceFile);
        }
    }

}
