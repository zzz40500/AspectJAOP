package red.dim.aop.aop

import org.apache.commons.io.FileUtils
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.aspectj.weaver.Dump
import red.dim.aop.common.L
import com.google.common.base.Joiner

/**
 * Created by dim on 17/4/15.
 */

public class AspectJProcessBuilder {

    String encoding;
    String sourceCompatibility;
    String targetCompatibility;
    String outputDir;
    String outputJar;
    Set<String> classpath = new HashSet<>();
    Set<String> aspectPath = new HashSet<>();
    String bootClassPath;
    String inPath;
    Set<String> binaryFiles;
    String dumpPath;


    public AspectJProcessBuilder encoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public AspectJProcessBuilder sourceCompatibility(String sourceCompatibility) {
        this.sourceCompatibility = sourceCompatibility;
        return this;

    }

    public AspectJProcessBuilder targetCompatibility(String targetCompatibility) {
        this.targetCompatibility = targetCompatibility;
        return this;

    }

    public AspectJProcessBuilder classpath(Set<String> classpath) {
        this.classpath = classpath;
        return this;

    }

    public AspectJProcessBuilder aspectPath(Set<String> aspectPath) {
        this.aspectPath = aspectPath;
        return this;

    }

    public AspectJProcessBuilder bootClassPath(String bootClassPath) {
        this.bootClassPath = bootClassPath;
        return this;

    }

    public AspectJProcessBuilder binaryFiles(Set<String> binaryFiles) {
        this.binaryFiles = binaryFiles;
        return this;

    }

    public AspectJProcessBuilder dumpPath(String dumpPath) {
        this.dumpPath = dumpPath;
        return this;

    }

    public AspectJProcessBuilder outputJar(String outputJar) {
        this.outputJar = outputJar;
        return this;

    }

    public AspectJProcessBuilder outputDir(String outputDir) {
        this.outputDir = outputDir;
        return this;

    }

    public AspectJProcessBuilder inPath(String inPath) {
        this.inPath = inPath;
        return this;

    }

    public void build() {
//        long startTime = System.currentTimeMillis();
        if (aspectPath.size() > 0) {
            def args = [
                    "-showWeaveInfo",
                    "-Xlint:ignore",
                    "-warn:none",
                    "-encoding", encoding,
                    "-source", sourceCompatibility,
                    "-target", targetCompatibility,
                    "-classpath", classpath.join(File.pathSeparator),
                    "-aspectpath", aspectPath.join(File.pathSeparator),
            ];
            if (outputDir != null) {
                args << "-d"
                args << outputDir;
            }
            if (outputJar != null) {
                args << "-outjar"
                args << outputJar;
            }
            if (bootClassPath != null) {
                args << "-bootclasspath"
                args << bootClassPath
            }
            if (binaryFiles != null) {
                args << "-infiles"
                args << inPath + File.pathSeparator + binaryFiles.join(File.pathSeparator)
            } else if (inPath != null) {
                args << "-inpath"
                args << inPath
            }
//            L.log(Joiner.on(" ").join(args))
            Main m = new Main();
            MessageHandler handler = new MessageHandler(true);
//            Dump.setDumpDirectory()
            Dump.setDumpDirectory(dumpPath)
            m.run(args as String[], handler);
            for (IMessage message : handler.getMessages(null, true)) {
                switch (message.getKind()) {
                    case IMessage.ABORT:
                    case IMessage.ERROR:
                    case IMessage.FAIL:
                        L.log(message.message);
                    case IMessage.WARNING:
//                        L.log(message.message);
                        break;
                    case IMessage.INFO:
//                        L.log(message.message);
                        break;
                    case IMessage.DEBUG:
//                        L.log(message.message);
                        break;
                }
            }
            m.quit();
        } else {
            if (outputDir != null) {
                FileUtils.copyDirectory(new File(inPath), new File(outputDir));
            }
            if (outputJar != null) {
                FileUtils.copyFile(new File(inPath), new File(outputJar));
            }
        }
//        L.log("inPath: " + inPath + " time " + (System.currentTimeMillis() - startTime) + "ms")
    }


    public void printFirstLine(String txt) {
        def split = txt.split("\n")
        if (split != null && split.length > 0) {
            L.log(split[0]);
        }
    }
}
