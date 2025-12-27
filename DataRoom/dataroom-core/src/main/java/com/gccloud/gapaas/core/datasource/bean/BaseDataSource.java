package com.gccloud.gapaas.core.datasource.bean;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gccloud.gapaas.core.MaxvConstant;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "dataSourceType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MySqlDatasource.class, name = MaxvConstant.Datasource.TYPE.MYSQL),
        @JsonSubTypes.Type(value = PostgreSqlDatasource.class, name = MaxvConstant.Datasource.TYPE.POSTGRESQL),
        @JsonSubTypes.Type(value = OracleDatasource.class, name = MaxvConstant.Datasource.TYPE.ORACLE)
})
public abstract class BaseDataSource {
}