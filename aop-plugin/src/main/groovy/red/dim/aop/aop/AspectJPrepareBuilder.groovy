package red.dim.aop.aop;

import com.google.common.io.ByteStreams;

import org.apache.commons.io.FileUtils;
import red.dim.aop.common.NoticeFileHelper;
import red.dim.aop.common.AspectJHelper;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import red.dim.aop.common.L;
import red.dim.aop.common.TextFileUtils

import static red.dim.aop.common.TextFileUtils.visit;
import static red.dim.aop.common.NoticeFileHelper.*;

/**
 * Created by dim on 17/4/18.
 */

public interface AspectJPrepareBuilder {

    class AspectJDirPrepareProcessBuilder {
        public File inputDir;
        public File outDir;
        public Set<String> binaryFiles;
        private List<String> outputList = new ArrayList();

        public AspectJDirPrepareProcessBuilder inputDir(String inputDir) {
            this.inputDir = new File(inputDir);
            return this;
        }

        public AspectJDirPrepareProcessBuilder outDir(String outDir) {
            this.outDir = new File(outDir);
            return this;
        }

        public AspectJDirPrepareProcessBuilder binaryFiles(Set<String> binaryFiles) {
            this.binaryFiles = binaryFiles;
            return this;
        }

        public List<String> build() {
            if (binaryFiles != null) {
                for (String binaryFile : binaryFiles) {
                    processClassFile(new File(binaryFile), outDir, binaryFile);
                }
            } else {
                processBuild(inputDir, outDir, inputDir.getAbsolutePath());
            }
            return outputList;
        }

        public void processBuild(File file, File targetDirFile, String srcDir) {
            if (file.isDirectory()) {
                if (file.listFiles() != null) {
                    for (File item : file.listFiles()) {
                        processBuild(item, targetDirFile, srcDir);
                    }
                }
            } else {
                processClassFile(file, targetDirFile, srcDir);
            }

        }

        private void processClassFile(File file, File outDir, String srcDir) {
            String targetPath = file.getAbsolutePath().replaceFirst(srcDir, outDir.getAbsolutePath());
            File targetFile = new File(targetPath);
            if (file.getName().endsWith(".class")) {
                try {
                    AspectJHelper.filterAspectJ(FileUtils.readFileToByteArray(file), targetFile, outputList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class AspectJJarPrepareProcessBuilder {
        private File inputJar;
        private File outDir;
        private List<String> outputList = new ArrayList();

        public AspectJJarPrepareProcessBuilder inputJar(String inputJar) {
            this.inputJar = new File(inputJar);
            return this;
        }

        public AspectJJarPrepareProcessBuilder outDir(String outDir) {
            this.outDir = new File(outDir);
            return this;
        }

        public List<String> build() {
            try {
                processBuild(inputJar, outDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return outputList;
        }

        private void processBuild(File zipFile, File targetDirFile) throws IOException {
            try {
                JarFile zis = new JarFile(zipFile);
                ZipEntry entry = zis.getEntry(NoticeFileHelper.NOTICE);
                final AtomicBoolean hasAspectInfo = new AtomicBoolean(false);
                if (entry != null) {
                    final List<String> entryList = new ArrayList<>();
                    visit(zis.getInputStream(entry), new TextFileUtils.TextVisitor() {
                        boolean start = false;

                        @Override
                        public void visit(String line) {

                            if (line == ASPECT_END) {
                                start = false;
                            }
                            if (start) {
                                entryList.add(line);
                                System.out.println(line);
                            }
                            if (line == ASPECT_START) {
                                hasAspectInfo.set(true);
                                start = true;
                            }

                        }
                    });
                    if (hasAspectInfo.get()) {
                        for (String entityName : entryList) {
                            entry = zis.getEntry(entityName);
                            if (entry != null) {
                                if (entry.getName().endsWith(".class")) {
                                    File targetFile = new File(targetDirFile.getAbsolutePath() + "/" + entry.getName());
                                    AspectJHelper.filterAspectJ(ByteStreams.toByteArray(zis.getInputStream(entry)), targetFile, outputList);
                                }
                            }
                        }
                        return;
                    }
                }

                Enumeration enumeration = zis.entries();
                while (enumeration.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) enumeration.nextElement();
                    String entryName = jarEntry.getName();
                    if (jarEntry.isDirectory()) continue;
                    InputStream inputStream = zis.getInputStream(jarEntry);
                    if (entryName.endsWith(".class")) {
                        File targetFile = new File(targetDirFile.getAbsolutePath() + "/" + entryName);
                        AspectJHelper.filterAspectJ(ByteStreams.toByteArray(inputStream), targetFile, outputList);
                    }
                }
                zis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
