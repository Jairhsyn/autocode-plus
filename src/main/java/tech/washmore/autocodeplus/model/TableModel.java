package tech.washmore.autocodeplus.model;

import lombok.Data;
import tech.washmore.autocodeplus.util.Strings;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class TableModel {
    private String tableName;
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

    public List<String> getImports() {
        return this.columns.stream().filter(col -> col.getJavaClass().getClassLoader() != null).map(c -> c.getJavaClass().getName()).distinct().sorted().collect(Collectors.toList());
    }

    private List<ColumnField> columns;
}
