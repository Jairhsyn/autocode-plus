package ${context.daoPackage};

import org.apache.ibatis.annotations.Mapper;
import ${context.modelPackage}.${model.modelName};
<#if model.primaryKeyCol.javaClass.classLoader??>import ${model.primaryKeyCol.javaClass.name}</#if>

import java.util.List;
import java.util.Map;

/**
 * @author Washmore
 * @version V1
 * @summary ${model.tableName}表<#if 0 < model.tableComment?length>(</#if>${model.tableComment}<#if 0 < model.tableComment?length>)</#if>的dao
 * @Copyright (c) ${.now?date?string["yyyy"]}, Washmore All Rights Reserved.
 * @since ${.now?date?string["yyyy年MM月dd日"]}
 */
@Mapper
public interface ${model.modelName}Dao {
	List<${model.modelName}> selectByMap(Map<String, Object> params);

    int countByMap(Map<String, Object> params);

    List<${model.modelName}> selectByExample(${model.modelName} example);

    int countByExample(${model.modelName} example);

    int batchInsert(List<${model.modelName}> list);

    int insertSelective(${model.modelName} ${model.modelName?uncap_first});

<#if model.primaryKeyCol??>
    ${model.modelName} selectByPrimaryKey(${model.primaryKeyCol.javaClass.simpleName} ${model.primaryKeyCol.fieldName});

    int updateByPrimaryKeySelective(${model.modelName} ${model.modelName?uncap_first});

    int deleteByPrimaryKey(${model.primaryKeyCol.javaClass.simpleName} ${model.primaryKeyCol.fieldName});

    int batchDeleteByPrimaryKey(List<${model.primaryKeyCol.javaClass.simpleName}> list);
</#if>

    int deleteByMap(Map<String, Object> params);
}