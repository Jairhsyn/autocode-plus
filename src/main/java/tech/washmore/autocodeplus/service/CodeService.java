package tech.washmore.autocodeplus.service;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tech.washmore.autocodeplus.common.FreeMarkerTemplateUtil;
import tech.washmore.autocodeplus.common.result.exceptions.AccessDeniedException;
import tech.washmore.autocodeplus.common.result.exceptions.InvalidParamException;
import tech.washmore.autocodeplus.common.result.exceptions.NotFoundException;
import tech.washmore.autocodeplus.model.CustomConfig;
import tech.washmore.autocodeplus.model.SysConfig;
import tech.washmore.autocodeplus.model.TableModel;
import tech.washmore.autocodeplus.model.param.AllConfig;
import tech.washmore.autocodeplus.model.param.OtherFile;
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
    public static final String OUTPUT_PATH_PREFIX;

    static {
        try {
            OUTPUT_PATH_PREFIX = new File(System.getProperty("java.io.tmpdir")).getCanonicalPath() + "/autocode/output/";
        } catch (IOException e) {
            throw new NotFoundException(-1, "OUTPUT_PATH_PREFIX初始化错误!", e);
        }
    }

    @Autowired
    private TableService tableService;

    public String process(AllConfig config) {
        try {
            //此方法返回读取文件字节的方式在linux系统中无异。

            List<TableModel> tms = tableService.getTableModelsByTableNames(config.getSysConfig());
            if (CollectionUtils.isEmpty(tms)) {
                throw new InvalidParamException("未查询到任何有效的表!请检查配置项");
            }


            File root = new File(OUTPUT_PATH_PREFIX + config.getSysConfig().getRootDir());
            System.out.println("root.getCanonicalPath():" + root.getCanonicalPath());


            if (!root.getCanonicalPath().startsWith(OUTPUT_PATH_PREFIX)) {
                throw new AccessDeniedException(String.format("你想拿%s这个路径干啥???", root.getCanonicalPath()));
            }

            if (!root.isDirectory() || config.getSysConfig().isReplaceAll()) {
                this.deleteDir(root.getCanonicalPath());
            }

            generateModels(config.getSysConfig(), config.getExtConfig(), tms);

            generateDaos(config.getSysConfig(), config.getExtConfig(), tms);

            generateMappers(config.getSysConfig(), config.getExtConfig(), tms);

            generateOthers(config.getSysConfig(), config.getExtConfig(), tms);

            return root.getCanonicalPath();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void generateOthers(SysConfig config, CustomConfig customConfig, List<TableModel> tms) {
        List<OtherFile> otherFiles = customConfig.getOtherFiles();
        if (CollectionUtils.isEmpty(otherFiles)) {
            return;
        }
        for (OtherFile other : otherFiles) {
            if (other.isEachTable()) {
                for (TableModel tm : tms) {
                    Map<String, Object> params = ImmutableMap.of(
                            "randomLong", new Random().nextLong(),
                            "context", config,
                            "model", tm,
                            "ext", customConfig.getExt()
                    );
                    String output = FreeMarkerTemplateUtil.build(other.getContent(), params);
                    writeCode(config.getRootDir() + FreeMarkerTemplateUtil.build(other.getFilePath(), params), FreeMarkerTemplateUtil.build(other.getFileNameExpression(), params), output, other.isOverride());
                }
            } else {
                Map<String, Object> params = ImmutableMap.of(
                        "randomLong", new Random().nextLong(),
                        "context", config,
                        "ext", customConfig.getExt()
                );
                String output = FreeMarkerTemplateUtil.build(other.getContent(), params);
                writeCode(config.getRootDir() + FreeMarkerTemplateUtil.build(other.getFilePath(), params), FreeMarkerTemplateUtil.build(other.getFileNameExpression(), params), output, other.isOverride());
            }
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

    private void generateCustemTemplates(String path, SysConfig config, CustomConfig customConfig, List<TableModel> tms, List<TemplateFile> modelFiles) throws IOException {
        for (TableModel tm : tms) {
            for (TemplateFile mf : modelFiles) {
                Map<String, Object> params = ImmutableMap.of(
                        "randomLong", new Random().nextLong(),
                        "context", config,
                        "model", tm,
                        "ext", customConfig.getExt()
                );
                String output = FreeMarkerTemplateUtil.build(mf.getContent(), params);
                writeCode(path, FreeMarkerTemplateUtil.build(mf.getFileNameExpression(), params), output, true);
            }
        }
    }

    private void writeCode(String parentPath, String fileName, String code, boolean override) {
        try {
            File parent = new File(OUTPUT_PATH_PREFIX + parentPath);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            File target = new File(parent, fileName);
            System.out.println("parent.getCanonicalPath():" + target.getCanonicalPath());
            if (!target.getCanonicalPath().startsWith(OUTPUT_PATH_PREFIX)) {
                throw new AccessDeniedException(String.format("你想拿%s这个路径干啥???", target.getCanonicalPath()));
            }
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

    public void deleteDir(String dirPath) {
        File file = new File(dirPath);
        if (file.isFile()) {
            System.out.println("file deleted:" + file.getAbsolutePath());
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                file.delete();
            } else {
                for (int i = 0; i < files.length; i++) {
                    this.deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }

}
