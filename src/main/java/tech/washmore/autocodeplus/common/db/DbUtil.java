package tech.washmore.autocodeplus.common.db;

import org.apache.ibatis.type.JdbcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import tech.washmore.autocodeplus.model.ColumnField;

import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DbUtil {
    @Autowired
    private DbConfig dbConfig;

    public int count(String sql, Object... params) {
        try (Connection connection = dbConfig.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i, params[i]);
            }
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Object queryOne(String sql, Class<?> resultType, Object... params) {
        List result = queryList(sql, resultType, params);
        if (CollectionUtils.isEmpty(result)) {
            return null;
        }
        return result.get(0);
    }

    public List<ColumnField> queryColumnMetaDatasByTableName(String tableName) {
        try (Connection connection = dbConfig.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("select * from " + tableName + " limit 1;");
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData metaData = (com.mysql.cj.jdbc.result.ResultSetMetaData) resultSet.getMetaData();
            return Arrays.stream(metaData.getFields()).map(f -> {
                ColumnField md = new ColumnField();
                md.setDatabaseName(f.getDatabaseName());
                md.setColumnName(f.getName());
                md.setJdbcType(JdbcType.forCode(f.getMysqlType().getJdbcType()).name());
                md.setAutoIncrement(f.isAutoIncrement());
                md.setNotNull(f.isNotNull());
                md.setPrimaryKey(f.isPrimaryKey());
                md.setUniqueKey(f.isUniqueKey());
                md.setDecimal(f.getMysqlType().isDecimal());
                if ("[B".equals(f.getMysqlType().getClassName())) {
                    md.setJavaClass(byte[].class);
                } else {
                    try {
                        String clsName = f.getMysqlType().getClassName();
                        if ("java.sql.Timestamp".equals(f.getMysqlType().getClassName())) {
                            clsName = "java.util.Date";
                        }
                        md.setJavaClass(Class.forName(clsName));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                return md;
            }).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List queryList(String sql, Class<?> resultType, Object... params) {
        try (Connection connection = dbConfig.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (int i = 1; i <= params.length; i++) {
                statement.setObject(i, params[i - 1]);
            }
            List result = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                if (Map.class.isAssignableFrom(resultType)) {
                    Map map = (Map) resultType.newInstance();
                    java.sql.ResultSetMetaData metaData = resultSet.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        String column = metaData.getColumnName(i);
                        map.put(column, resultSet.getObject(i));
                    }
                    result.add(map);
                } else if (resultType.getClassLoader() == null) {
                    result.add(resultSet.getObject(1, resultType));
                } else {
                    Object target = resultType.newInstance();
                    Field[] fields = resultType.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String column = field.getName();
                        ColumnName cn = field.getDeclaredAnnotation(ColumnName.class);
                        if (cn != null) {
                            column = cn.value();
                        }
                        Object columnValue = null;
                        try {
                            columnValue = resultSet.getObject(column);
                        } catch (Exception ex) {
                        }
                        field.set(target, columnValue);
                        field.setAccessible(false);
                    }
                    result.add(target);
                }
            }
            return result;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
