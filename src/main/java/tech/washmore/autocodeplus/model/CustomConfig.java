package tech.washmore.autocodeplus.model;

import lombok.Data;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.washmore.autocodeplus.model.param.OtherFile;
import tech.washmore.autocodeplus.model.param.TemplateFile;
import tech.washmore.autocodeplus.model.param.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class CustomConfig {
    private boolean daoEnable;
    private boolean mapperEnable;
    private boolean modelEnable;
    private List<TemplateFile> daoFiles;
    private List<TemplateFile> mapperFiles;
    private List<TemplateFile> modelFiles;
    private List<Variable> variables;
    private List<OtherFile> otherFiles;

    public Map<String, String> getExt() {
        if (CollectionUtils.isEmpty(variables)) {
            return new HashMap<>();
        }
        return variables.stream().filter(entry -> !StringUtils.isEmpty(entry.getKey())).collect(Collectors.toMap(Variable::getKey, Variable::getValue, (v1, v2) -> v2));
    }

}
