package tech.washmore.autocodeplus.controller;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
public class FileController {

    @PostMapping("/file/parse")
    public Map<String, String> process(@RequestParam("file") MultipartFile file, @RequestParam String type, @RequestParam(required = false, defaultValue = "0") String index) {
        try {
            return ImmutableMap.of("index", index, "type", type, "name", file.getOriginalFilename(), "content", new String(file.getBytes(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/file/template/{templateFileName}")
    public void process(@PathVariable("templateFileName") String templateFileName, HttpServletResponse response) throws IOException {
        response.setContentType("application/force-download;charset=utf8");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;fileName=" + templateFileName);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/" + templateFileName);
        String template = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        response.getWriter().write(template);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
