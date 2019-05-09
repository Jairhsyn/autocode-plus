package tech.washmore.autocodeplus.model.param;

import lombok.Data;

@Data
public class TemplateFile {
    public interface FILE_TYPE {
        String DAO = "dao";
        String MAPPER = "mapper";
        String MODEL = "model";
    }

    private String fileNameExpression;
    private String name;
    private String type;
    private String content;
}
