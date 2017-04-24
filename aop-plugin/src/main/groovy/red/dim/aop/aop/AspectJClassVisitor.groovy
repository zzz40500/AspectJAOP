package red.dim.aop.aop;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import static org.objectweb.asm.Opcodes.ASM5;


/**
 * Created by dim on 17/4/18.
 */

public class AspectJClassVisitor extends ClassVisitor {
    private boolean aspectJ;

    public AspectJClassVisitor(ClassWriter cw) {
        super(ASM5,cw);
    }
    public boolean isAspectJ() {
        return aspectJ;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (desc == "Lorg/aspectj/lang/annotation/Aspect;") {
            aspectJ = true;
        }
        return super.visitAnnotation(desc, visible);
    }
}
