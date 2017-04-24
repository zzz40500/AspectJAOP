package red.dim.aop.transform.task;

import org.apache.commons.io.FileUtils;
import org.gradle.api.tasks.bundling.Jar;

import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


import red.dim.aop.transform.task.work.AspectJPrepareWork;
import red.dim.aop.transform.task.work.TaskBuild;
import red.dim.aop.common.NoticeFileHelper;

/**
 * Created by dim on 17/4/21.
 */

public class JarAspectJTask {

    public void execute(Jar jar) {
        AspectJPrepareWork aspectJPrepareWork = new AspectJPrepareWork();
        aspectJPrepareWork.addTask(new TaskBuild.JarTaskBuildConfig(jar.archivePath.absolutePath,
                jar.getArchivePath().getParentFile().getAbsolutePath() + "/temp"));
        aspectJPrepareWork.execute();
        JarFile zis = null;
        try {
            zis = new JarFile(jar.getArchivePath());
            ZipEntry entry = zis.getEntry(NoticeFileHelper.NOTICE);
            File noticeFile = new File(jar.getArchivePath().getParentFile().absolutePath + "/temp/" + NoticeFileHelper.NOTICE);
            if (!noticeFile.getParentFile().exists()) {
                noticeFile.getParentFile().mkdirs();
            }
            if (entry != null) {
                FileUtils.copyInputStreamToFile(zis.getInputStream(entry), noticeFile)
            }
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(jar.archivePath));
            NoticeFileHelper.appendNoticeFile(new File(jar.archivePath.getParentFile().absolutePath + "/temp"), noticeFile);
            ZipEntry zipEntry = new ZipEntry(NoticeFileHelper.NOTICE);
            zos.putNextEntry(zipEntry);
            byte[] array = FileUtils.readFileToByteArray(noticeFile);
            zos.write(array, 0, array.length);
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
