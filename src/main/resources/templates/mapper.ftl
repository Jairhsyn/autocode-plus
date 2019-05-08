<#macro hash value>${r"#{"}${value}}</#macro>
<#macro dol value>${r"${"}${value}}</#macro>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${context.daoPackage}.${model.modelName}Dao">

    <select id="selectByMap" parameterType="Map" resultMap="BaseResultMap">
        SELECT
            <include refid="Base_Column_List"/>
        FROM ${model.tableName}
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

    <select id="countByMap" parameterType="Map" resultMap="Integer">
        SELECT
            COUNT(*)
        FROM ${model.tableName}
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
        FROM ${model.tableName}
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

    <select id="countByExample" parameterType="${context.modelPackage}.${model.modelName}" resultMap="Integer">
        SELECT
            COUNT(*)
        FROM ${model.tableName}
        <where>
        <#list model.columns as col>
            <if test="${col.fieldName} != null">
                AND ${col.columnName} = <@hash col.fieldName + ",jdbcType=" + col.jdbcType/>
            </if>
        </#list>
        </where>
    </select>

    <insert id="batchInsert" parameterType="List" <#if model.primaryKeyCol??>keyProperty = "${model.primaryKeyCol.fieldName}"</#if>>
        INSERT INTO ${model.tableName}
            (<include refid="Base_Column_List"/>)
        VALUES
        <foreach collection="list" item="item" separator=",">
            (<#list model.columns as col><#if !col?is_first>,<#if col?index%4==0>
            <#else>${' '}</#if></#if><@hash col.fieldName + ",jdbcType=" + col.jdbcType/></#list>)
        </foreach>
    </insert>

    <insert id="insertSelective" parameterType="${context.modelPackage}.${model.modelName}" <#if model.primaryKeyCol??>keyProperty = "${model.primaryKeyCol.fieldName}"</#if>>
        INSERT INTO ${model.tableName}
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
        FROM user_wx
        WHERE ${model.primaryKeyCol.columnName} = <@hash model.primaryKeyCol.fieldName + ",jdbcType=" + model.primaryKeyCol.jdbcType/>
    </select>

    <update id="updateByPrimaryKeySelective" parameterType="${context.modelPackage}.${model.modelName}">
        UPDATE ${model.tableName}
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
        DELETE FROM ${model.tableName}
        WHERE ${model.primaryKeyCol.columnName} = <@hash model.primaryKeyCol.fieldName + ",jdbcType=" + model.primaryKeyCol.jdbcType/>
    </delete>

	<delete id="batchDeleteByPrimaryKey" parameterType="List">
        DELETE FROM ${model.tableName}
        <where>
            ${model.primaryKeyCol.columnName} IN
            <foreach collection="list" item="item" separator="," open="(" close=")">
                <@hash "item"/>
            </foreach>
        </where>
    </delete>
</#if>

    <delete id="deleteByMap" parameterType="Map">
        DELETE FROM ${model.tableName}
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

</mapper>
