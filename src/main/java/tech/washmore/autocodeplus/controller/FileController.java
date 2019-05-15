package tech.washmore.autocodeplus.controller;

import com.google.common.collect.ImmutableMap;
import jdk.internal.util.xml.impl.Input;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.washmore.autocodeplus.common.result.JAssert;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
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

    @GetMapping("/file/template/{templateFileName}")
    public void process(@PathVariable("templateFileName") String templateFileName, HttpServletResponse response) throws Exception {
        response.setContentType("application/force-download;charset=utf8");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;fileName=" + templateFileName);

        if ("templates.zip".equals(templateFileName)) {
            OutputStream ops = response.getOutputStream();
            ZipOutputStream out = new ZipOutputStream(ops);

            zipResources(out, "", "templates");

            out.close();
            ops.flush();
            ops.close();
        } else {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/" + templateFileName);
            JAssert.found(inputStream != null, String.format("未找到对应的资源%s!", templateFileName));
            String template = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            Writer writer = response.getWriter();
            writer.write(template);
            writer.flush();
            writer.close();
        }
    }

    public void copyResourcesToTempDictionary(String sourceParentPath, String name, File tempParent) throws Exception {
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

    public void zipResources(ZipOutputStream out, String sourceParentPath, String name) throws Exception {
        String path = sourceParentPath + "/" + name;
        InputStream ips = this.getClass().getResourceAsStream(path);
        if (ips instanceof ByteArrayInputStream) {
            //取出文件夹中的文件（或子文件夹）
            List<String> children = IOUtils.readLines(ips, StandardCharsets.UTF_8);
            if (CollectionUtils.isEmpty(children)) {
                //如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                out.putNextEntry(new ZipEntry(sourceParentPath));
            } else {
                for (String child : children) {
                    zipResources(out, path, child);
                }
            }
        } else {
            //如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
            out.putNextEntry(new ZipEntry(path));
            IOUtils.write(IOUtils.toByteArray(ips), out);
            out.flush();
        }
    }

    public void zip(ZipOutputStream out, File sourceFile, String base) throws Exception {
        //如果路径为目录（文件夹）
        if (sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] fileList = sourceFile.listFiles();
            if (fileList.length == 0) {
                //如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
                System.out.println(base + "/");
                out.putNextEntry(new ZipEntry(base + "/"));
            } else {
                //如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
                for (File file : fileList) {
                    zip(out, file, base + "/" + file.getName());
                }
            }
        } else {
            //如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
            out.putNextEntry(new ZipEntry(base));
            IOUtils.write(FileUtils.readFileToByteArray(sourceFile), out);
            out.flush();
        }
    }
}
