package red.dim.aop.common;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import red.dim.aop.aop.AspectJClassVisitor;

/**
 * Created by dim on 17/4/23.
 */

public class AspectJHelper {

    private static void filterAspectJ(byte[] bytes, File targetFile, List outputList) {
        try {
            ClassReader classReader = new ClassReader(bytes);
            ClassWriter cw = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
            AspectJClassVisitor aspectJClassVisitor = new AspectJClassVisitor(cw);
            classReader.accept(aspectJClassVisitor, ClassReader.EXPAND_FRAMES);

            if (aspectJClassVisitor.isAspectJ()) {
                try {
                    FileUtils.writeByteArrayToFile(targetFile, cw.toByteArray());
                    outputList.add(targetFile.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            try {
                ClassReader classReader = new ClassReader(bytes);
                ClassWriter cw = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
                AspectJClassVisitor aspectJClassVisitor = new AspectJClassVisitor(cw);

                classReader.accept(aspectJClassVisitor, ClassReader.SKIP_FRAMES);
                if (aspectJClassVisitor.isAspectJ()) {
                    try {
                        FileUtils.writeByteArrayToFile(targetFile, cw.toByteArray());
                        outputList.add(targetFile.getPath());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (Exception e1) {
            }
        }
    }
}
