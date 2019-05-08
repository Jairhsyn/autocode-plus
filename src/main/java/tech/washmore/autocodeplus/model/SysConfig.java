package tech.washmore.autocodeplus.model;

import lombok.Data;
import tech.washmore.autocodeplus.util.Strings;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class SysConfig {
    private String rootDir;
    private String sourcePath;
    private String resourcePath;
    private String modelPackage;
    private String servicePackage;
    private String daoPackage;
    private String mapperPath;
    private List<String> tables;
    private Map<String, String> customTemplates;
    private Map<String, String> ext;

    public String getModelFullPath() {
        return rootDir + sourcePath + modelPackage.replace(".", "/");
    }

    public String getServiceFullPath() {
        return rootDir + sourcePath + servicePackage.replace(".", "/");
    }

    public String getDaoFullPath() {
        return rootDir + sourcePath + daoPackage.replace(".", "/");
    }

    public String getMapperFullPath() {
        return rootDir + resourcePath + mapperPath;
    }
}
