package tech.washmore.autocodeplus.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class FreeMarkerTemplateUtil {
    public static String build(String template, Map<String, Object> params) {
        Configuration conf = new Configuration(Configuration.VERSION_2_3_28);
        try {
            Template temp = new Template("temp", template, conf);
            StringWriter writer = new StringWriter();
            temp.process(params, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
