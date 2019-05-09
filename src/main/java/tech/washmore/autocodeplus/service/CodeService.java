package tech.washmore.autocodeplus.service;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.washmore.autocodeplus.common.FreeMarkerTemplateUtil;
import tech.washmore.autocodeplus.common.result.exceptions.InvalidParamException;
import tech.washmore.autocodeplus.model.CustomConfig;
import tech.washmore.autocodeplus.model.SysConfig;
import tech.washmore.autocodeplus.model.TableModel;
import tech.washmore.autocodeplus.model.param.AllConfig;
import tech.washmore.autocodeplus.model.param.TemplateFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class CodeService {
    @Autowired
    private TableService tableService;

    public boolean process(AllConfig config) {
        try {
            //此方法返回读取文件字节的方式在linux系统中无异。

            List<TableModel> tms = tableService.getTableModelsByTableNames(config.getSysConfig());
            if (CollectionUtils.isEmpty(tms)) {
                throw new InvalidParamException("未查询到任何有效的表!请检查配置项");
            }
            generateModels(config.getSysConfig(), config.getExtConfig(), tms);

            generateDaos(config.getSysConfig(), config.getExtConfig(), tms);

            generateMappers(config.getSysConfig(), config.getExtConfig(), tms);

            return true;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void generateModels(SysConfig config, CustomConfig customConfig, List<TableModel> tms) throws Exception {
        List<TemplateFile> modelFiles = customConfig.getModelFiles();
        if (!customConfig.isModelEnable() || CollectionUtils.isEmpty(modelFiles)) {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/model.ftl");
            String template = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            for (TableModel tm : tms) {
                Map<String, Object> params = ImmutableMap.of(
                        "randomLong", new Random().nextLong(),
                        "context", config,
                        "model", tm,
                        "ext", customConfig.getExt()
                );
                String output = FreeMarkerTemplateUtil.build(template, params);
                writeCode(config.getModelFullPath(), tm.getModelName() + ".java", output, true);
            }
            return;
        }
        generateCustemTemplates(config.getModelFullPath(), config, customConfig, tms, modelFiles);
    }

    public void generateMappers(SysConfig config, CustomConfig customConfig, List<TableModel> tms) throws Exception {

        List<TemplateFile> modelFiles = customConfig.getMapperFiles();
        if (!customConfig.isMapperEnable() || CollectionUtils.isEmpty(modelFiles)) {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/mapper.ftl");
            String template = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            for (TableModel tm : tms) {
                Map<String, Object> params = ImmutableMap.of(
                        "randomLong", new Random().nextLong(),
                        "context", config,
                        "model", tm,
                        "ext", customConfig.getExt()
                );
                String output = FreeMarkerTemplateUtil.build(template, params);
                writeCode(config.getMapperFullPath(), tm.getModelName() + "Mapper.xml", output, true);
            }
            return;
        }
        generateCustemTemplates(config.getMapperFullPath(), config, customConfig, tms, modelFiles);
    }

    public void generateDaos(SysConfig config, CustomConfig customConfig, List<TableModel> tms) throws Exception {
        List<TemplateFile> modelFiles = customConfig.getDaoFiles();
        if (!customConfig.isDaoEnable() || CollectionUtils.isEmpty(modelFiles)) {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("templates/dao.ftl");
            String template = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            for (TableModel tm : tms) {
                Map<String, Object> params = ImmutableMap.of(
                        "randomLong", new Random().nextLong(),
                        "context", config,
                        "model", tm,
                        "ext", customConfig.getExt()
                );
                String output = FreeMarkerTemplateUtil.build(template, params);
                writeCode(config.getDaoFullPath(), tm.getModelName() + "Dao.java", output, true);
            }
            return;
        }
        generateCustemTemplates(config.getDaoFullPath(), config, customConfig, tms, modelFiles);
    }

    private void generateCustemTemplates(String path, SysConfig sysConfig, CustomConfig customConfig, List<TableModel> tms, List<TemplateFile> modelFiles) throws IOException {
        for (TableModel tm : tms) {
            for (TemplateFile mf : modelFiles) {
                Map<String, Object> params = ImmutableMap.of(
                        "randomLong", new Random().nextLong(),
                        "context", sysConfig,
                        "model", tm,
                        "ext", customConfig.getExt()
                );
                String output = FreeMarkerTemplateUtil.build(mf.getContent(), params);
                System.out.println(output);
                writeCode(path, FreeMarkerTemplateUtil.build(mf.getFileNameExpression(), params), output, true);
            }
        }
    }

    private void writeCode(String parentPath, String fileName, String code, boolean override) {
        try {
            File parent = new File(parentPath);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            File target = new File(parent, fileName);
            if (!target.exists()) {
                target.createNewFile();
            } else if (!override) {
                return;
            }
            target.delete();
            target.createNewFile();
            FileUtils.writeStringToFile(target, code, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
