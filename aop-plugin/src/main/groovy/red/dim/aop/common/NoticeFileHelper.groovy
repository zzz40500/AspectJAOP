package red.dim.aop.common;

import org.aspectj.util.FileUtil
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by dim on 17/4/21.
 */

public class NoticeFileHelper {

    public static final String ASPECT_START = "-----------ASPECT_START-----------";
    public static final String ASPECT_END = "-----------ASPECT_END-----------";
    public static final String NOTICE = "NOTICE";


    public static void appendNoticeFile(File aspectRootPath, File file) {
        BufferedSink bufferedSink = null;
        try {
            bufferedSink = Okio.buffer(Okio.appendingSink(file));
            FileFilter fileFilter = new FileFilter() {
                @Override
                public boolean accept(File fff) {
                    return true;
                }
            };
            File[] listFiles = FileUtil.listFiles(aspectRootPath, fileFilter);
            bufferedSink.writeUtf8(ASPECT_START).writeUtf8("\n");
            if (listFiles != null) {
                for (File item : listFiles) {
                    if(item.getAbsolutePath().endsWith(".class")){
                        String path = item.getAbsolutePath().replaceFirst(aspectRootPath.getAbsolutePath() + "/", "");
                        bufferedSink.writeUtf8(path.substring(path.indexOf("/") + 1)).writeUtf8("\n");
                    }
                }
            }
            bufferedSink.writeUtf8(ASPECT_END).writeUtf8("\n");
            bufferedSink.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedSink != null) {
                try {
                    bufferedSink.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
