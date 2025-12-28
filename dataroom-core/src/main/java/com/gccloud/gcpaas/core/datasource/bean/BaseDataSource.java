package com.gccloud.gcpaas.core.datasource.bean;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gccloud.gcpaas.core.constant.DataRoomConstant;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "dataSourceType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = MySqlDatasource.class, name = DataRoomConstant.Datasource.TYPE.MYSQL),
        @JsonSubTypes.Type(value = PostgreSqlDatasource.class, name = DataRoomConstant.Datasource.TYPE.POSTGRESQL),
        @JsonSubTypes.Type(value = OracleDatasource.class, name = DataRoomConstant.Datasource.TYPE.ORACLE)
})
public abstract class BaseDataSource {
}