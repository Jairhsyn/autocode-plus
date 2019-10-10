package tech.washmore.autocodeplus.model;

import lombok.Data;
import org.springframework.util.StringUtils;
import tech.washmore.autocodeplus.util.Strings;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TableModel {
    private String tableName;
    private String tableOriginalName;

    private String tableComment;

    public String getModelName() {
        return Strings.underlineToCamel(tableName, true);
    }

    public ColumnField getPrimaryKeyCol() {
        return columns.stream().filter(ColumnField::isPrimaryKey).findFirst().orElse(null);
    }

    public ColumnField getAutoIncrementCol() {
        return columns.stream().filter(ColumnField::isAutoIncrement).findFirst().orElse(null);
    }

    public List<ColumnField> getUniqueKeyCols() {
        return columns.stream().filter(ColumnField::isUniqueKey).collect(Collectors.toList());
    }

    public List<ColumnField> getForeignKeyCols() {
        return columns.stream().filter(col -> com.google.common.base.Strings.nullToEmpty(col.getColoumComment()).contains("#外键#")).collect(Collectors.toList());
    }


    public List<String> getImports() {
        return this.columns.stream().map(c -> c.getJavaClass().getName()).filter(n -> !n.startsWith("java.lang.")).distinct().sorted().collect(Collectors.toList());
    }

    private List<ColumnField> columns;

    public List<ColumnField> getColumnsWithoutPrimaryKey() {
        return columns.stream().filter(col -> !col.isPrimaryKey()).collect(Collectors.toList());
    }

}
