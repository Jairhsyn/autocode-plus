package tech.washmore.autocodeplus.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Washmore
 * @version V1.0
 * @summary zip压缩
 * @Copyright (c) 2018, Washmore All Rights Reserved.
 * @since 2018/1/15
 */
public class ZipUtil {

    public synchronized static void zipResources(ZipOutputStream out, String sourceParentPath, String name) throws Exception {
        String path = sourceParentPath + "/" + name;
        InputStream ips = ZipUtil.class.getResourceAsStream(path);
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

    public synchronized static void zipResourcesInJar(ZipOutputStream out, Resource[] resources) throws Exception {
        for (Resource resource : resources) {
            String tempUrl = resource.getURL().toString().substring(resource.getURL().toString().lastIndexOf("!") + 2);
            out.putNextEntry(new ZipEntry(tempUrl));
            if (resource.contentLength() > 0) {
                InputStream ips = resource.getInputStream();
                IOUtils.write(IOUtils.toByteArray(ips), out);
            }
            out.flush();
        }
    }

    public synchronized static void zip(ZipOutputStream out, File sourceFile, String base) throws IOException {
        //如果路径为目录（文件夹）
        if (sourceFile.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] fileList = sourceFile.listFiles();
            if (fileList.length == 0) {
                //如果文件夹为空，则只需在目的地zip文件中写入一个目录进入点
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
