package tech.washmore.autocodeplus.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.washmore.autocodeplus.common.db.DbUtil;
import tech.washmore.autocodeplus.model.ColumnField;
import tech.washmore.autocodeplus.model.SysConfig;
import tech.washmore.autocodeplus.model.TableModel;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TableService {
    @Autowired
    private DbUtil dbUtil;

    public List<TableModel> getTableModelsByTableNames(SysConfig config) {
        List<String> tableNames = config.getTables();
        return tableNames.stream().map(t -> this.getTableModelByTableName(t, config)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public TableModel getTableModelByTableName(String tableName, SysConfig config) {
        TableModel tm = new TableModel();

        if (CollectionUtils.isEmpty(config.getTablePrefixes())) {
            tm.setTableName(tableName.toUpperCase());
        } else {
            for (String tablePrefix : config.getTablePrefixes()) {
                if (tableName.toUpperCase().startsWith(tablePrefix.toUpperCase())) {
                    tm.setTableName(tableName.toUpperCase().substring(tablePrefix.length()));
                    break;
                }
            }
            if (StringUtils.isEmpty(tm.getTableName())) {
                tm.setTableName(tableName.toUpperCase());
            }
        }
        String tableComment = this.queryTableCommnet(tableName);
        tm.setTableComment(Objects.toString(tableComment, ""));
        List<ColumnField> columnFields = dbUtil.queryColumnMetaDatasByTableName(tableName);
        columnFields.forEach(cf -> {
            JSONObject json = this.queryColumnExt(tableName, cf.getColumnName());
            cf.setColoumComment(Objects.toString(json.getString("COMMENT"), ""));
            cf.setDefaultValue(Objects.toString(json.getString("DEFAULT"), ""));
            cf.setTableName(tm.getTableName());
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
