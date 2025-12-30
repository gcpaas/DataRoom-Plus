package com.gccloud.gcpaas.core.datasource.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class PostgreSqlDatasource extends BaseDataSource {
    @Schema(description = "驱动名称")
    private String driverName;
    @Schema(description = "用户名")
    private String username;
    /**
     * 数据源密码
     */
    @Schema(description = "数据源密码")
    private String password;
    @Schema(description = "地址")
    private String url;

    @Override
    public void desensitize() {
        // 清空密码
        this.setPassword(null);
    }

    @Override
    public void updatedSensitive(BaseDataSource baseDataSource) {
        if (StringUtils.isBlank(password)) {
            PostgreSqlDatasource db = (PostgreSqlDatasource) baseDataSource;
            password = db.getPassword();
        }
    }
}
