package tech.washmore.autocodeplus.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.washmore.autocodeplus.common.result.JAssert;
import tech.washmore.autocodeplus.util.JarUtil;
import tech.washmore.autocodeplus.util.ZipUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @GetMapping("/file/template/templates.zip")
    public void process(HttpServletResponse response) throws Exception {
        response.setContentType("application/force-download;charset=utf8");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;fileName=templates.zip");
        OutputStream ops = response.getOutputStream();
        ZipOutputStream out = new ZipOutputStream(ops);

        if (JarUtil.isJar()) {
            this.downloadInJar(out);
        } else {
            ZipUtil.zipResources(out, "", "templates");
        }

        out.close();
        ops.flush();
        ops.close();
    }

    private void downloadInJar(ZipOutputStream out) throws Exception {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver(getClass().getClassLoader());
        Resource[] resources = resourceResolver.getResources("templates/**/*");
        ZipUtil.zipResourcesInJar(out, resources);
    }

    private void copyResourcesToTempDictionary(String sourceParentPath, String name, File tempParent) throws Exception {
        String path = sourceParentPath + "/" + name;
        InputStream ips = this.getClass().getResourceAsStream(path);
        File file = new File(tempParent, name);
        if (file.exists()) {
            file.delete();
        }

        if (ips instanceof ByteArrayInputStream) {
            //文件夹
            file.mkdirs();
            List<String> children = IOUtils.readLines(ips, StandardCharsets.UTF_8);
            if (CollectionUtils.isEmpty(children)) {
                return;
            }
            for (String child : children) {
                copyResourcesToTempDictionary(path, child, file);
            }
        } else if (ips instanceof BufferedInputStream) {
            file.createNewFile();
            FileUtils.writeByteArrayToFile(file, IOUtils.toByteArray(ips));
        }
    }

}
