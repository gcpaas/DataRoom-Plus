package com.gccloud.gcpaas.core.dataset.bean;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gccloud.gcpaas.core.constant.DataRoomConstant;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "datasetType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = JsonDataset.class, name = DataRoomConstant.Dataset.TYPE.JSON),
        @JsonSubTypes.Type(value = GroovyDataset.class, name = DataRoomConstant.Dataset.TYPE.GROOVY),
        @JsonSubTypes.Type(value = HttpDataset.class, name = DataRoomConstant.Dataset.TYPE.HTTP),
        @JsonSubTypes.Type(value = MySqlDataset.class, name = DataRoomConstant.Dataset.TYPE.MYSQL)
})
public abstract class BaseDataset {

}