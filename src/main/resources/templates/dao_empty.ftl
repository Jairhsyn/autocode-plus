package ${context.daoPackage};

import org.springframework.stereotype.Repository;

/**
 * @author Washmore
 * @version V1
 * @summary ${model.tableName}表<#if 0 < model.tableComment?length>(</#if>${model.tableComment}<#if 0 < model.tableComment?length>)</#if>的dao
 * @Copyright (c) ${.now?date?string["yyyy"]}, Washmore All Rights Reserved.
 * @since ${.now?date?string["yyyy年MM月dd日"]}
 */
@Repository
public class ${model.modelName}Dao extends X${model.modelName}Dao {
    //Fill By User
}