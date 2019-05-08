package tech.washmore.autocodeplus.controller;

import com.google.common.collect.ImmutableMap;
import freemarker.template.Template;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.washmore.autocodeplus.common.FreeMarkerTemplateUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Controller
public class PageController {
    @GetMapping("/hello")
    public String hello() {
        return "index";
    }

    @GetMapping("/convert")
    @ResponseBody
    public String convert() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/mapper.ftl");//此方法返回读取文件字节的方式在linux系统中无异。
        String result = FreeMarkerTemplateUtil.build(IOUtils.toString(inputStream, StandardCharsets.UTF_8), ImmutableMap.of("table_name", "Test"));
        return result;
    }

    @GetMapping("/world")
    public String world() {
        return "index2";
    }
}
