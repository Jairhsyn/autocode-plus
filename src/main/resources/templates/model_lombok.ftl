package ${context.modelPackage};

import lombok.Data;
import lombok.ToString;

<#list model.imports as imp>
import ${imp};
</#list>

/**
 * @author Washmore
 * @version V1
 * @summary ${model.tableName}表<#if 0 < model.tableComment?length>(</#if>${model.tableComment}<#if 0 < model.tableComment?length>)</#if>的model
 * @Copyright (c) ${.now?date?string["yyyy"]}, Washmore All Rights Reserved.
 * @since ${.now?date?string["yyyy年MM月dd日"]}
 */
@Data
@ToString
public class ${model.modelName} implements java.io.Serializable {
    private static final long serialVersionUID = ${randomLong?string["0"]}L;
<#list model.columns as col>
    /**
     * ${col.columnName}<#if 0 < col.coloumComment?length>(</#if>${col.coloumComment}<#if 0 < col.coloumComment?length>)</#if> 默认值:${col.defaultValue}
     */
    private ${col.javaClass.simpleName} ${col.fieldName};
</#list>
}