package tech.washmore.autocodeplus.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.washmore.autocodeplus.common.db.DbUtil;
import tech.washmore.autocodeplus.model.ColumnField;
import tech.washmore.autocodeplus.model.TableModel;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {
    @Autowired
    private DbUtil dbUtil;

    public List<TableModel> getTableModelsByTableNames(List<String> tableNames) {
        return tableNames.stream().map(this::getTableModelByTableName).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public TableModel getTableModelByTableName(String tableName) {
        TableModel tm = new TableModel();
        tm.setTableName(tableName);

        String tableComment = this.queryTableCommnet(tableName);
        tm.setTableComment(Objects.toString(tableComment, ""));
        List<ColumnField> columnFields = dbUtil.queryColumnMetaDatasByTableName(tableName);
        columnFields.forEach(cf -> {
            JSONObject json = this.queryColumnExt(tableName, cf.getColumnName());
            cf.setColoumComment(Objects.toString(json.getString("COMMENT"), ""));
            cf.setDefaultValue(Objects.toString(json.getString("DEFAULT"), ""));
        });
        tm.setColumns(columnFields);
        return tm;
    }

    public JSONObject queryColumnExt(String tableName, String columnName) {
        return ((JSONObject) dbUtil.queryOne("show full columns from " + tableName + " where Field = ?", JSONObject.class, columnName));
    }

    public String queryTableCommnet(String tableName) {
        return ((JSONObject) dbUtil.queryOne("show table status where name = ?;", JSONObject.class, tableName)).getString("TABLE_COMMENT");
    }
}
