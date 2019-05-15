package tech.washmore.autocodeplus.model.param;

import lombok.Data;

@Data
public class OtherFile {
    private String fileNameExpression;
    private boolean eachTable;
    private String filePath;
    private String content;
    private boolean override;
}
