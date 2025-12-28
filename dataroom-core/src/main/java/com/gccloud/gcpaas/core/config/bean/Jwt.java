package com.gccloud.gcpaas.core.config.bean;

import lombok.Data;

@Data
public class Jwt {
    /**
     * 颁发jwt者
     */
    private String issuer="dataRoom";
    /**
     * 密钥
     */
    private String secret;
    /**
     * 签名算法
     */
    private String alg;
    /**
     * jwt时效（单位为秒）
     */
    private Long expiration = 7200L;
    /**
     * tokenKey
     */
    private String tokenKey = "dataRoomToken";
}
