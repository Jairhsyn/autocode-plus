<#macro hash value>${r"#{"}${value}}</#macro>
<#macro dol value>${r"${"}${value}}</#macro>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${model.modelName}Mapper">

    <select id="selectByMap" parameterType="Map" resultMap="BaseResultMap">
        SELECT
            <include refid="Base_Column_List"/>
        FROM ${model.tableOriginalName}
        <where>
        <#list model.columns as col>
            <if test="${col.fieldName} != null">
                AND ${col.columnName} = <@hash col.fieldName + ",jdbcType=" + col.jdbcType/>
            </if>
        </#list>
        </where>
        <#if model.autoIncrementCol??>
        ORDER BY ${model.autoIncrementCol.columnName} DESC
        </#if>
        <if test="start != null and offset != null">
            LIMIT <@hash "start"/>, <@hash "offset"/>
        </if>
    </select>

    <select id="countByMap" parameterType="Map" resultType="int">
        SELECT
            COUNT(*)
        FROM ${model.tableOriginalName}
        <where>
        <#list model.columns as col>
            <if test="${col.fieldName} != null">
                AND ${col.columnName} = <@hash col.fieldName + ",jdbcType=" + col.jdbcType/>
            </if>
        </#list>
        </where>
    </select>

    <select id="selectByExample" parameterType="${context.modelPackage}.${model.modelName}" resultMap="BaseResultMap">
        SELECT
            <include refid="Base_Column_List"/>
        FROM ${model.tableOriginalName}
        <where>
        <#list model.columns as col>
            <if test="${col.fieldName} != null">
                AND ${col.columnName} = <@hash col.fieldName + ",jdbcType=" + col.jdbcType/>
            </if>
        </#list>
        </where>
        <#if model.autoIncrementCol??>
        ORDER BY ${model.autoIncrementCol.columnName} DESC
        </#if>
    </select>

    <select id="countByExample" parameterType="${context.modelPackage}.${model.modelName}" resultType="int">
        SELECT
            COUNT(*)
        FROM ${model.tableOriginalName}
        <where>
        <#list model.columns as col>
            <if test="${col.fieldName} != null">
                AND ${col.columnName} = <@hash col.fieldName + ",jdbcType=" + col.jdbcType/>
            </if>
        </#list>
        </where>
    </select>

    <insert id="batchInsert" parameterType="List" <#if model.primaryKeyCol??>keyProperty = "${model.primaryKeyCol.fieldName}"</#if>>
        INSERT INTO ${model.tableOriginalName}
            (<include refid="Base_Column_List_Without_PrimaryKey"/>)
        VALUES
        <foreach collection="list" item="item" separator=",">
            (<#list model.columnsWithoutPrimaryKey as col><#if !col?is_first>,<#if col?index%4==0>
            <#else>${' '}</#if></#if><@hash col.fieldName + ",jdbcType=" + col.jdbcType/></#list>)
        </foreach>
    </insert>

    <insert id="insertSelective" parameterType="${context.modelPackage}.${model.modelName}" <#if model.primaryKeyCol??>keyProperty = "${model.primaryKeyCol.fieldName}"</#if>>
        INSERT INTO ${model.tableOriginalName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#list model.columns as col>
            <if test="${col.fieldName} != null">
                ${col.columnName},
            </if>
        </#list>
        </trim>
        <trim prefix="VALUES (" suffix=")" suffixOverrides=",">
        <#list model.columns as col>
            <if test="${col.fieldName} != null">
                <@hash col.fieldName + ",jdbcType=" + col.jdbcType/>,
            </if>
        </#list>
        </trim>
    </insert>
<#if model.primaryKeyCol??>

	<select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="<#if model.primaryKeyCol.javaClass.classLoader??>${model.primaryKeyCol.javaClass.name}<#else>${model.primaryKeyCol.javaClass.simpleName}</#if>">
        SELECT
            <include refid="Base_Column_List"/>
        FROM ${model.tableOriginalName}
        WHERE ${model.primaryKeyCol.columnName} = <@hash model.primaryKeyCol.fieldName + ",jdbcType=" + model.primaryKeyCol.jdbcType/>
    </select>

    <update id="updateByPrimaryKeySelective" parameterType="${context.modelPackage}.${model.modelName}">
        UPDATE ${model.tableOriginalName}
        <set>
    <#list model.columns as col>
        <#if model.primaryKeyCol.fieldName != col.fieldName>
            <if test="${col.fieldName} != null">
                ${col.columnName} = <@hash col.fieldName + ",jdbcType=" + col.jdbcType/>,
            </if>
        </#if>
    </#list>
        </set>
        WHERE ${model.primaryKeyCol.columnName} = <@hash model.primaryKeyCol.fieldName + ",jdbcType=" + model.primaryKeyCol.jdbcType/>
    </update>

	<delete id="deleteByPrimaryKey" parameterType="<#if model.primaryKeyCol.javaClass.classLoader??>${model.primaryKeyCol.javaClass.name}<#else>${model.primaryKeyCol.javaClass.simpleName}</#if>">
        DELETE FROM ${model.tableOriginalName}
        WHERE ${model.primaryKeyCol.columnName} = <@hash model.primaryKeyCol.fieldName + ",jdbcType=" + model.primaryKeyCol.jdbcType/>
    </delete>

	<delete id="batchDeleteByPrimaryKey" parameterType="List">
        DELETE FROM ${model.tableOriginalName}
        <where>
            ${model.primaryKeyCol.columnName} IN
            <foreach collection="list" item="item" separator="," open="(" close=")">
                <@hash "item"/>
			</foreach>
        </where>
    </delete>
</#if>
<#if model.uniqueKeyCols??>
    <#list model.uniqueKeyCols as uniqueKeyCol>

	<select id="selectBy${uniqueKeyCol.fieldName?cap_first}" resultMap="BaseResultMap" parameterType="<#if uniqueKeyCol.javaClass.classLoader??>${uniqueKeyCol.javaClass.name}<#else>${uniqueKeyCol.javaClass.simpleName}</#if>">
        SELECT
            <include refid="Base_Column_List"/>
        FROM ${model.tableOriginalName}
        WHERE ${uniqueKeyCol.columnName} = <@hash uniqueKeyCol.fieldName + ",jdbcType=" + uniqueKeyCol.jdbcType/>
		LIMIT 1
    </select>

    <update id="updateBy${uniqueKeyCol.fieldName?cap_first}Selective" parameterType="${context.modelPackage}.${model.modelName}">
        UPDATE ${model.tableOriginalName}
        <set>
    <#list model.columns as col>
        <#if uniqueKeyCol.fieldName != col.fieldName>
		<#if model.primaryKeyCol.fieldName != col.fieldName>
            <if test="${col.fieldName} != null">
                ${col.columnName} = <@hash col.fieldName + ",jdbcType=" + col.jdbcType/>,
            </if>
		</#if>
        </#if>
    </#list>
        </set>
        WHERE ${uniqueKeyCol.columnName} = <@hash uniqueKeyCol.fieldName + ",jdbcType=" + uniqueKeyCol.jdbcType/>
    </update>

	<delete id="deleteBy${uniqueKeyCol.fieldName?cap_first}" parameterType="<#if uniqueKeyCol.javaClass.classLoader??>${uniqueKeyCol.javaClass.name}<#else>${uniqueKeyCol.javaClass.simpleName}</#if>">
        DELETE FROM ${model.tableOriginalName}
        WHERE ${uniqueKeyCol.columnName} = <@hash uniqueKeyCol.fieldName + ",jdbcType=" + uniqueKeyCol.jdbcType/>
    </delete>
	</#list>
</#if>
<#if model.foreignKeyCols??>
    <#list model.foreignKeyCols as foreignKeyCol>

	<select id="selectBy${foreignKeyCol.fieldName?cap_first}" resultMap="BaseResultMap" parameterType="<#if foreignKeyCol.javaClass.classLoader??>${foreignKeyCol.javaClass.name}<#else>${foreignKeyCol.javaClass.simpleName}</#if>">
        SELECT
            <include refid="Base_Column_List"/>
        FROM ${model.tableOriginalName}
        WHERE ${foreignKeyCol.columnName} = <@hash foreignKeyCol.fieldName + ",jdbcType=" + foreignKeyCol.jdbcType/>
    </select>

    <update id="updateBy${foreignKeyCol.fieldName?cap_first}Selective" parameterType="${context.modelPackage}.${model.modelName}">
        UPDATE ${model.tableOriginalName}
        <set>
    <#list model.columns as col>
        <#if foreignKeyCol.fieldName != col.fieldName>
		<#if model.primaryKeyCol.fieldName != col.fieldName>
            <if test="${col.fieldName} != null">
                ${col.columnName} = <@hash col.fieldName + ",jdbcType=" + col.jdbcType/>,
            </if>
		</#if>
        </#if>
    </#list>
        </set>
        WHERE ${foreignKeyCol.columnName} = <@hash foreignKeyCol.fieldName + ",jdbcType=" + foreignKeyCol.jdbcType/>
    </update>

	<delete id="deleteBy${foreignKeyCol.fieldName?cap_first}" parameterType="<#if foreignKeyCol.javaClass.classLoader??>${foreignKeyCol.javaClass.name}<#else>${foreignKeyCol.javaClass.simpleName}</#if>">
        DELETE FROM ${model.tableOriginalName}
        WHERE ${foreignKeyCol.columnName} = <@hash foreignKeyCol.fieldName + ",jdbcType=" + foreignKeyCol.jdbcType/>
    </delete>
	</#list>
</#if>

    <delete id="deleteByMap" parameterType="Map">
        DELETE FROM ${model.tableOriginalName}
        <where>
        <#list model.columns as col>
            <if test="${col.fieldName} != null">
                AND ${col.columnName} = <@hash col.fieldName + ",jdbcType=" + col.jdbcType/>
            </if>
        </#list>
        </where>
    </delete>

    <resultMap id="BaseResultMap" type="${context.modelPackage}.${model.modelName}">
<#list model.columns as col>
    <#if col.primaryKey>
        <id column="${col.columnName}" property="${col.fieldName}" jdbcType="${col.jdbcType}"/>
    <#else >
        <result column="${col.columnName}" property="${col.fieldName}" jdbcType="${col.jdbcType}"/>
    </#if>
</#list>
    </resultMap>

    <sql id="Base_Column_List">
        <#list model.columns as col><#if !col?is_first>,<#if col?index%4==0>
        <#else>${' '}</#if></#if>${col.columnName}</#list>
    </sql>

    <sql id="Base_Column_List_Without_PrimaryKey">
        <#list model.columnsWithoutPrimaryKey as col><#if !col?is_first>,<#if col?index%4==0>
        <#else>${' '}</#if></#if>${col.columnName}</#list>
    </sql>
</mapper>
