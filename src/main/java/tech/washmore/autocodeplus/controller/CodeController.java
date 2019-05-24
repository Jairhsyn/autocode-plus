package tech.washmore.autocodeplus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.washmore.autocodeplus.common.result.exceptions.AccessDeniedException;
import tech.washmore.autocodeplus.common.result.exceptions.InvalidParamException;
import tech.washmore.autocodeplus.model.SysConfig;
import tech.washmore.autocodeplus.model.param.AllConfig;
import tech.washmore.autocodeplus.service.CodeService;
import tech.washmore.autocodeplus.util.ZipUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

@RestController
public class CodeController {
    @Autowired
    private CodeService codeService;

    @PostMapping("/code/process")
    public String process(@RequestBody AllConfig config) {
        return codeService.process(config);
    }

    @GetMapping("/code/download")
    public void download(HttpServletResponse response, @RequestParam("path") String path) throws IOException {
        response.setContentType("application/force-download;charset=utf8");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;fileName=source.zip");
        OutputStream ops = response.getOutputStream();
        ZipOutputStream out = new ZipOutputStream(ops);

        File parent = new File(path);
        System.out.println("parent.getCanonicalPath():" + parent.getCanonicalPath());
        if (!parent.getCanonicalPath().startsWith(CodeService.OUTPUT_PATH_PREFIX)) {
            throw new AccessDeniedException(String.format("你想拿%s这个路径干啥???", parent.getCanonicalPath()));
        }

        if (!parent.exists() || !parent.isDirectory()) {
            throw new InvalidParamException(String.format("参数路径[%s]错误,请尝试重新生成代码下载!", path));
        }
        ZipUtil.zip(out, parent, path.substring(path.lastIndexOf("/") + 1));

        out.close();
        ops.flush();
        ops.close();
    }
}
