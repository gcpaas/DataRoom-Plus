package com.gccloud.gcpaas.core.dataset.bean;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.gccloud.gcpaas.core.MaxvConstant;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "datasetType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = JsonDataset.class, name = MaxvConstant.Dataset.TYPE.JSON),
        @JsonSubTypes.Type(value = GroovyDataset.class, name = MaxvConstant.Dataset.TYPE.GROOVY),
        @JsonSubTypes.Type(value = HttpDataset.class, name = MaxvConstant.Dataset.TYPE.HTTP),
        @JsonSubTypes.Type(value = MySqlDataset.class, name = MaxvConstant.Dataset.TYPE.MYSQL)
})
public abstract class BaseDataset {

}