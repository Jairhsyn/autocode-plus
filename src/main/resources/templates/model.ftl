package ${context.modelPackage};

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
public class ${model.modelName} implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
<#list model.columns as col>
    /**
     * ${col.columnName}<#if 0 < col.coloumComment?length>(</#if>${col.coloumComment}<#if 0 < col.coloumComment?length>)</#if> 默认值:${col.defaultValue}
     */
    private ${col.javaClass.simpleName} ${col.fieldName};
</#list>
<#list model.columns as col>
    /**
     * ${col.columnName}<#if 0 < col.coloumComment?length>(</#if>${col.coloumComment}<#if 0 < col.coloumComment?length>)</#if> 默认值:${col.defaultValue}
     */
    public ${col.javaClass.simpleName} get${col.fieldName?cap_first}() {
        return this.${col.fieldName};
    }

    public void set${col.fieldName?cap_first}(${col.javaClass.simpleName} ${col.fieldName}) {
        this.${col.fieldName} = ${col.fieldName};
    }
</#list>
    @Override
    public String toString() {
        return "${model.modelName?cap_first}{" +
<#list model.columns as col>
               "<#if !col?is_first>, </#if>${col.fieldName}=<#if !col.decimal>\'</#if>" + ${col.fieldName}<#if !col.decimal> + "\'"</#if> +
</#list>
               "}";
    }

}