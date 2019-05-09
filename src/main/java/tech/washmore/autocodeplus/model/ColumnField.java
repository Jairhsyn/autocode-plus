package tech.washmore.autocodeplus.model;

import lombok.Data;
import tech.washmore.autocodeplus.common.db.ColumnName;
import tech.washmore.autocodeplus.util.Strings;

@Data
public class ColumnField {
    private String coloumComment;
    private String defaultValue;

    private boolean decimal;
    private boolean autoIncrement;
    private boolean notNull;
    private boolean primaryKey;
    private boolean uniqueKey;

    private String tableOriginalName;
    private String databaseName;
    private String columnName;
    private String jdbcType;
    private Class<?> javaClass;

    public String getFieldName() {
        return Strings.underlineToCamel(columnName, false);
    }
}
