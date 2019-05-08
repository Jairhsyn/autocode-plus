package tech.washmore.autocodeplus.service;

import org.apache.catalina.webresources.StandardRoot;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.washmore.autocodeplus.common.FreeMarkerTemplateUtil;
import tech.washmore.autocodeplus.common.result.exceptions.InvalidParamException;
import tech.washmore.autocodeplus.model.SysConfig;
import tech.washmore.autocodeplus.model.TableModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class CodeService {
    @Autowired
    private TableService tableService;

    public boolean process(SysConfig config) {
        try {
            //此方法返回读取文件字节的方式在linux系统中无异。

            List<TableModel> tms = tableService.getTableModelsByTableNames(config);
            if (CollectionUtils.isEmpty(tms)) {
                throw new InvalidParamException("未查询到任何有效的表!请检查配置项");
            }
            generateModels(config, tms);

            generateDaos(config, tms);

            generateMappers(config, tms);

            return true;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void generateModels(SysConfig config, List<TableModel> tms) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/model.ftl");
        String template = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        for (TableModel tm : tms) {
            String output = FreeMarkerTemplateUtil.build(template, new HashMap() {{
                put("randomLong", new Random().nextLong());
                put("context", config);
                put("model", tm);
            }});
            System.out.println(output);
            writeCode(config.getModelFullPath(), tm.getModelName() + ".java", output, true);
        }
    }

    public void generateMappers(SysConfig config, List<TableModel> tms) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/mapper.ftl");
        String template = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        for (TableModel tm : tms) {
            String output = FreeMarkerTemplateUtil.build(template, new HashMap() {{
                put("context", config);
                put("model", tm);
            }});
            System.out.println(output);
            writeCode(config.getMapperFullPath(), tm.getModelName() + "Mapper.xml", output, true);
        }
    }

    public void generateDaos(SysConfig config, List<TableModel> tms) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/dao.ftl");
        String template = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        for (TableModel tm : tms) {
            String output = FreeMarkerTemplateUtil.build(template, new HashMap() {{
                put("context", config);
                put("model", tm);
            }});
            System.out.println(output);
            writeCode(config.getDaoFullPath(), tm.getModelName() + "Dao.java", output, true);
        }
    }

    private void writeCode(String parentPath, String fileName, String code, boolean override) throws IOException {
        File parent = new File(parentPath);
        if (!parent.exists()) {
            parent.mkdirs();
        }
        File target = new File(parent, fileName);
        if (!target.exists()) {
        } else if (override) {
            parent.delete();
            parent.createNewFile();
        }
        FileUtils.writeStringToFile(target, code, StandardCharsets.UTF_8);
    }
}
