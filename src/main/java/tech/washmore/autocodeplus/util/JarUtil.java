package tech.washmore.autocodeplus.util;

import com.alibaba.fastjson.JSON;

import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

public class JarUtil {
    private static final byte[] JAR_MAGIC = {'P', 'K', 3, 4};

    public static boolean isJar() {
        URL url = JarUtil.class.getResource("/");
        byte[] buffer = new byte[JAR_MAGIC.length];
        try (InputStream is = url.openStream()) {
            is.read(buffer, 0, JAR_MAGIC.length);
            if (Arrays.equals(buffer, JAR_MAGIC)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Failure to read the stream means this is not a JAR
        }
        return false;
    }
}
