package ${context.daoPackage};

import ${ext.daoManagerPackage}.${ext.daoManagerName};
import ${context.modelPackage}.${model.modelName};
<#if !model.primaryKeyCol.javaClass.name?starts_with("java.lang.")>

import ${model.primaryKeyCol.javaClass.name};
</#if>

import java.util.List;
import java.util.Map;

/**
 * @author Washmore
 * @version V1
 * @summary ${model.tableName}表<#if 0 < model.tableComment?length>(</#if>${model.tableComment}<#if 0 < model.tableComment?length>)</#if>的dao默认模板
 * @Copyright (c) ${.now?date?string["yyyy"]}, Washmore All Rights Reserved.
 * @since ${.now?date?string["yyyy年MM月dd日"]}
 */
public abstract class X${model.modelName}Dao extends ${ext.daoManagerName} {
    /**
     * 多条件组合查询符合条件的${model.modelName}对象列表,支持分页查询(使用start和offset参数)
     *
     * @param params 组合条件
     * @return 符合查询条件的${model.modelName}对象列表
     */
    public List<${model.modelName}> selectByMap(Map<String, Object> params) {
        return ${ext.sqlSessionCommon}.selectList("selectByMap", params);
    }

    /**
     * 多条件组合查询符合条件的${model.modelName}对象总量
     *
     * @param params 组合条件
     * @return 符合查询条件的${model.modelName}对象数量
     */
    public int countByMap(Map<String, Object> params) {
        return ${ext.sqlSessionCommon}.selectOne("countByMap", params);
    }

    /**
     * 按照样例查询符合条件的${model.modelName}对象列表,不支持分页查询
     *
     * @param example 样例
     * @return 符合查询条件的${model.modelName}对象列表
     */
    public List<${model.modelName}> selectByExample(${model.modelName} example) {
        return ${ext.sqlSessionCommon}.selectList("selectByExample", example);
    }

    /**
     * 按照样例查询符合条件的${model.modelName}对象总量
     *
     * @param example 样例
     * @return 符合查询条件的${model.modelName}对象数量
     */
    public int countByExample(${model.modelName} example) {
        return ${ext.sqlSessionCommon}.selectOne("countByExample", example);
    }

    /**
     * 批量插入${model.modelName}(全量字段)对象列表
     *
     * @param list ${model.modelName}对象列表
     * @return 受影响行数
     */
    public int batchInsert(List<${model.modelName}> list) {
        return ${ext.sqlSessionCommon}.insert("batchInsert", list);
    }

    /**
     * 插入${model.modelName}(非空字段)对象
     *
     * @param ${model.modelName?uncap_first} ${model.modelName}对象
     * @return 受影响行数
     */
    public int insertSelective(${model.modelName} ${model.modelName?uncap_first}) {
        return ${ext.sqlSessionCommon}.insert("insertSelective", ${model.modelName?uncap_first});
    }
<#if model.primaryKeyCol??>

    /**
     * 根据主键${model.primaryKeyCol.fieldName}查询${model.modelName}对象
     *
     * @param ${model.primaryKeyCol.fieldName} 主键
     * @return ${model.modelName}对象(允许为空)
     */
    public ${model.modelName} selectByPrimaryKey(${model.primaryKeyCol.javaClass.simpleName} ${model.primaryKeyCol.fieldName}) {
        return ${ext.sqlSessionCommon}.selectOne("selectByPrimaryKey", ${model.primaryKeyCol.fieldName});
    }

    /**
     * 根据主键${model.primaryKeyCol.fieldName}更新${model.modelName}(非空字段)对象
     *
     * @param ${model.modelName?uncap_first} 待更新${model.modelName}对象
     * @return 受影响行数
     */
    public int updateByPrimaryKeySelective(${model.modelName} ${model.modelName?uncap_first}) {
        return ${ext.sqlSessionCommon}.update("updateByPrimaryKeySelective", ${model.modelName?uncap_first});
    }

    /**
     * 根据主键${model.primaryKeyCol.fieldName}删除${model.modelName}
     *
     * @param ${model.primaryKeyCol.fieldName} 主键
     * @return 受影响行数
     */
    public int deleteByPrimaryKey(${model.primaryKeyCol.javaClass.simpleName} ${model.primaryKeyCol.fieldName}) {
        return ${ext.sqlSessionCommon}.delete("deleteByPrimaryKey", ${model.primaryKeyCol.fieldName});
    }

    /**
     * 根据主键${model.primaryKeyCol.fieldName}批量删除${model.modelName}
     *
     * @param list 主键列表
     * @return 受影响行数
     */
    public int batchDeleteByPrimaryKey(List<${model.primaryKeyCol.javaClass.simpleName}> list) {
        return ${ext.sqlSessionCommon}.delete("batchDeleteByPrimaryKey", list);
    }
</#if>

    /**
     * 根据多条件组合批量删除${model.modelName}
     *
     * @param params 组合条件
     * @return 受影响行数
     */
    public int deleteByMap(Map<String, Object> params) {
        return ${ext.sqlSessionCommon}.delete("deleteByMap", params);
    }
<#if model.primaryKeyCol??>

    /**
     * 根据主键${model.primaryKeyCol.fieldName}查询${model.modelName}对象(从只读数据源)
     *
     * @param ${model.primaryKeyCol.fieldName} 主键
     * @return ${model.modelName}对象(允许为空)
     */
    public ${model.modelName} selectByPrimaryKeyReadOnly(${model.primaryKeyCol.javaClass.simpleName} ${model.primaryKeyCol.fieldName}) {
        return ${ext.sqlSessionReadonly}.selectOne("selectByPrimaryKey", ${model.primaryKeyCol.fieldName});
    }
</#if>

    /**
     * 多条件组合查询符合条件的${model.modelName}对象列表,支持分页查询(使用start和offset参数)(从只读数据源)
     *
     * @param params 组合条件
     * @return 符合查询条件的${model.modelName}对象列表
     */
    public List<${model.modelName}> selectByMapReadOnly(Map<String, Object> params) {
        return ${ext.sqlSessionReadonly}.selectList("selectByMap", params);
    }

    /**
     * 多条件组合查询符合条件的${model.modelName}对象总量(从只读数据源)
     *
     * @param params 组合条件
     * @return 符合查询条件的${model.modelName}对象数量
     */
    public int countByMapReadOnly(Map<String, Object> params) {
        return ${ext.sqlSessionReadonly}.selectOne("countByMap", params);
    }

    /**
     * 按照样例查询符合条件的${model.modelName}对象列表,不支持分页查询(从只读数据源)
     *
     * @param example 样例
     * @return 符合查询条件的${model.modelName}对象列表
     */
    public List<${model.modelName}> selectByExampleReadOnly(${model.modelName} example) {
        return ${ext.sqlSessionReadonly}.selectList("selectByExample", example);
    }

    /**
     * 按照样例查询符合条件的${model.modelName}对象总量(从只读数据源)
     *
     * @param example 样例
     * @return 符合查询条件的${model.modelName}对象数量
     */
    public int countByExampleReadOnly(${model.modelName} example) {
        return ${ext.sqlSessionReadonly}.selectOne("countByExample", example);
    }
}