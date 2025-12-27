package com.gccloud.gapaas.core.datasource.bean;

import lombok.Data;

/**
 * 关系型数据库数据源，如：MySQL、Oracle、PG等
 */
@Data
public class RelationalDatasource extends BaseDataSource {
    private String driverName;
    private String username;
    private String password;
    private String url;
}
