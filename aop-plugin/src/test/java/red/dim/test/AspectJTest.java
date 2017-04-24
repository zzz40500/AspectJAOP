package red.dim.test;

import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.tools.ajc.Main;

import java.util.ArrayList;
import java.util.List;

import red.dim.aop.common.L;


/**
 * Created by didm on 17/4/22.
 */

public class AspectJTest {


    public static void main(String[] args) {


        long time = System.currentTimeMillis();
        for (int i = 0; i < 1; i++) {
            tast2();
        }
        System.out.println("  " + (System.currentTimeMillis() - time));

//        tast1();
    }

    private static void tast1() {
        List arg = new ArrayList();
        arg.add("");
        arg.add("-showWeaveInfo");
        arg.add("-encoding");
        arg.add("-outxml");
        arg.add("UTF-8");
        arg.add("-source");
        arg.add("1.7");
        arg.add("-target");
        arg.add("1.7");
//        arg.add("-classpath");
//        arg.add("/Users/didm/.gradle/caches/modules-2/files-2.1/com.google.code.gson/gson/2.8.0/c4ba5371a29ac9b2ad6129b1d39ea38750043eff/gson" +
//                "-2.8.0.jar:/Users/didm/Documents/zzz40500/AspectJAOP/lib_android/build/intermediates/bundles/defa" +
//                "ult/classes.jar:/Users/didm/.android/build-cache/9527c6752a5c97f27ad3f57709f265fbd1800316/output/jars/cla" +
//                "sses.jar:/Users/didm/android/sdk/extras/android/m2repository/com/android/support/support-annotations/25.1.1/support-" +
//                "annotations-25.1.1.jar:/Users/didm/Documents/zzz40500/AspectJAOP/lib_java/build/libs/lib_java.jar:/Users/didm/.android/buil" +
//                "d-cache/4b9c73fbd64e6ad8b40d3d5c93a94c5423773e75/output/jars/classes.jar:/Users/didm/.gradle/caches/modules-2/files-2.1/org.asp" +
//                "ectj/aspectjrt/1.8.9/87372d9e2323313bec24b09a325bfea8ae0eb867/aspectjrt-1.8.9.jar:/Users/didm/.android/build-cache/bb397ca8908dc7dd51204d37" +
//                "d324370f7b5f531/output/jars/classes.jar:/Users/didm/.android/build-cache/38602503bdc738ac7ef8bc5f5b1e4561873e6a91/output/jars/classes.jar:/Users/d" +
//                "idm/.android/build-cache/27a8920246b15dc46b08db0d58019ff52e0f4c56/output/jars/classes.jar:/Users/didm/.android/build-cache/4e61a1bdb" +
//                "29b62e5697a67cd01a9655" +
//                "d86396ba2/output/jars/classes.jar:/Users/didm/.android/build-cache/64f24854c17a0178668b88394b0af70c6af59d36/output/jars/classes.jar:/Us" +
//                "ers/didm/.android/build-cache/7942" +
//                "c4503099c32152cb4b13017e3b7524b0924c/output/jars/classes.jar:/Users/didm/.gradle/caches/modules-2/files-2.1/com.squareup.okhttp3/okhttp/" +
//                "3.4.1/c7c4f9e35c2fd5900da24f9872e39718" +
//                "01f08ce0/okhttp-3.4.1.jar:/Users/didm/.gradle/caches/modules-2/files-2.1/com.squareup.okio/okio/1.9.0/f824591a0016efbaeddb8300bee54832a13" +
//                "98cfa/okio-1.9.0.jar:/Users/didm/.android/bu" +
//                "ild-cache/efd537e82777817863b90b4554311ac2b6a6401a/output/jars/classes.jar:/Users/didm/.android/build-cache/b4c002eab4c0dccdd92aae19eb3" +
//                "a2b6226b55c18/output/jars/classes.jar:/Users/didm/.android/" +
//                "build-cache/78b334d6fce143346579d7632c7582ea34e0a0b1/output/jars/classes.jar:/Users/didm/.gradle/caches/modules-2/files-2.1/com.squareup.okhttp/okhttp/2.7.5/7a15a7db50f86c4b64aa3" +
//                "367424a60e3a325b8f1/okhttp-2.7.5.jar:/Users/didm/.andro" +
//                "id/build-cache/44ea74e932d0072491265a94f3032be4b20a0f4a/output/jars/classes.jar:/" +
//                "Users/didm/.android/build-cache/63fa544116c3ffa9ab38eb11544eadf882dc6e1e/output/jars/classes.jar");
        arg.add("-aspectpath");
        arg.add("/Users/didm/Documents/zzz40500/AspectJAOP/example/build/intermediates/aspectPath/DimDebug/a1d7ab294832b6c973b1c5e9dfd86caf0f326fca");
        arg.add("-outjar");
        arg.add("/Users/didm/Documents/zzz40500/AspectJAOP/example/build/intermediates/transforms/aspectj/dim/debug/jars/1/10/okhttp.jar");
//        arg.add("-bootclasspath");
//        arg.add("/Users/didm/android/sdk/platforms/android-25/android.jar");
        arg.add("-inpath");
        arg.add("/Users/didm/Documents/zzz40500/AspectJAOP/example/build/intermediates/classes/dim/debug");
//        arg.add("/Users/didm/.gradle/caches/modules-2/files-2.1/com.squareup.okhttp3/okhttp/3.4.1/c7c4f9e35c2fd5900da24f9872e3971801f08ce0/okhttp-3.4.1.jar");
        Main m = new Main();
        MessageHandler handler = new MessageHandler(true);
        String[] strs = (String[]) arg.toArray(new String[0]);
        m.run(strs, handler);
        for (IMessage message : handler.getMessages(null, true)) {
            L.log(message.getMessage());
//            switch (message.getKind()) {
//                case ABORT:
//                case ERROR:
//                case FAIL:
//
//                case IMessage.WARNING:
////                        L.log(message.message);
//                    break;
//                case IMessage.INFO:
////                        L.log(message.message);
//                    break;
//                case IMessage.DEBUG:
////                        L.log(message.message);
//                    break;
//            }
        }
        m.quit();
    }

    private static void tast2() {
        List arg = new ArrayList();
        arg.add("");
        arg.add("-showWeaveInfo");
        arg.add("-encoding");
        arg.add("-outxml");
        arg.add("UTF-8");
        arg.add("-source");
        arg.add("1.7");
        arg.add("-target");
        arg.add("1.7");
        arg.add("-classpath");
        arg.add("/Users/didm/.gradle/caches/modules-2/files-2.1/com.google.code.gson/gson/2.8.0/c4ba5371a29ac9b2ad6129b1d39ea38750043eff/gson" +
                "-2.8.0.jar:/Users/didm/Documents/zzz40500/AspectJAOP/lib_android/build/intermediates/bundles/defa" +
                "ult/classes.jar:/Users/didm/.android/build-cache/9527c6752a5c97f27ad3f57709f265fbd1800316/output/jars/cla" +
                "sses.jar:/Users/didm/android/sdk/extras/android/m2repository/com/android/support/support-annotations/25.1.1/support-" +
                "annotations-25.1.1.jar:/Users/didm/Documents/zzz40500/AspectJAOP/lib_java/build/libs/lib_java.jar:/Users/didm/.android/buil" +
                "d-cache/4b9c73fbd64e6ad8b40d3d5c93a94c5423773e75/output/jars/classes.jar:/Users/didm/.gradle/caches/modules-2/files-2.1/org.asp" +
                "ectj/aspectjrt/1.8.9/87372d9e2323313bec24b09a325bfea8ae0eb867/aspectjrt-1.8.9.jar:/Users/didm/.android/build-cache/bb397ca8908dc7dd51204d37" +
                "d324370f7b5f531/output/jars/classes.jar:/Users/didm/.android/build-cache/38602503bdc738ac7ef8bc5f5b1e4561873e6a91/output/jars/classes.jar:/Users/d" +
                "idm/.android/build-cache/27a8920246b15dc46b08db0d58019ff52e0f4c56/output/jars/classes.jar:/Users/didm/.android/build-cache/4e61a1bdb" +
                "29b62e5697a67cd01a9655" +
                "d86396ba2/output/jars/classes.jar:/Users/didm/.android/build-cache/64f24854c17a0178668b88394b0af70c6af59d36/output/jars/classes.jar:/Us" +
                "ers/didm/.android/build-cache/7942" +
                "c4503099c32152cb4b13017e3b7524b0924c/output/jars/classes.jar:/Users/didm/.gradle/caches/modules-2/files-2.1/com.squareup.okhttp3/okhttp/" +
                "3.4.1/c7c4f9e35c2fd5900da24f9872e39718" +
                "01f08ce0/okhttp-3.4.1.jar:/Users/didm/.gradle/caches/modules-2/files-2.1/com.squareup.okio/okio/1.9.0/f824591a0016efbaeddb8300bee54832a13" +
                "98cfa/okio-1.9.0.jar:/Users/didm/.android/bu" +
                "ild-cache/efd537e82777817863b90b4554311ac2b6a6401a/output/jars/classes.jar:/Users/didm/.android/build-cache/b4c002eab4c0dccdd92aae19eb3" +
                "a2b6226b55c18/output/jars/classes.jar:/Users/didm/.android/" +
                "build-cache/78b334d6fce143346579d7632c7582ea34e0a0b1/output/jars/classes.jar:/Users/didm/.gradle/caches/modules-2/files-2.1/com.squareup.okhttp/okhttp/2.7.5/7a15a7db50f86c4b64aa3" +
                "367424a60e3a325b8f1/okhttp-2.7.5.jar:/Users/didm/.andro" +
                "id/build-cache/44ea74e932d0072491265a94f3032be4b20a0f4a/output/jars/classes.jar:/" +
                "/Users/didm/android/sdk/platforms/android-25/android.jar:" +
                "Users/didm/.android/build-cache/63fa544116c3ffa9ab38eb11544eadf882dc6e1e/output/jars/classes.jar");
        arg.add("-aspectpath");
        arg.add("/Users/didm/Documents/zzz40500/AspectJAOP/example/build/intermediates/aspectPath/DimDebug/a1d7ab294832b6c973b1c5e9dfd86caf0f326fca");
        arg.add("-d");
        arg.add("/Users/didm/Documents/zzz40500/AspectJAOP/example/build/intermediates/transforms/aspectj/dim/debug/folders/cc");
        arg.add("-bootclasspath");
        arg.add("/Users/didm/android/sdk/platforms/android-25/android.jar");
        arg.add("-infiles");
        arg.add("/Users/didm/Documents/zzz40500/AspectJAOP/example/build/intermediates/classes/dim/debug:/Users/didm/Documents/zzz40500/AspectJAOP/example/build/intermediates/classes/dim/debug/red/dim/aopexample/MainActivity.class");
//        arg.add("-inpath");
//        arg.add("/Users/didm/.gradle/caches/modules-2/files-2.1/com.squareup.okhttp3/okhttp/3.4.1/c7c4f9e35c2fd5900da24f9872e3971801f08ce0/okhttp-3.4.1.jar");
        Main m = new Main();
        MessageHandler handler = new MessageHandler(true);
        String[] strs = (String[]) arg.toArray(new String[0]);
        m.run(strs, handler);
        for (IMessage message : handler.getMessages(null, true)) {
            L.log(message.getMessage() );
//            switch (message.getKind()) {
//                case ABORT:
//                case ERROR:
//                case FAIL:
//
//                case IMessage.WARNING:
////                        L.log(message.message);
//                    break;
//                case IMessage.INFO:
////                        L.log(message.message);
//                    break;
//                case IMessage.DEBUG:
////                        L.log(message.message);
//                    break;
//            }
        }
        m.quit();
    }
}
