package com.gccloud.gcpaas.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 中转态页面（历史记录、快照）
 */
@Data
@TableName("`mv_page_stage`")
public class PageStageEntity extends BaseEntity {
    /**
     * 页面编码
     */
    private String code;
    /**
     * 记录说明
     */
    private String remark;
    /**
     * 中转状态、如：快照、历史记录、临时保存等
     * 参考 ${@link  com.xiaoka.maxv.MaxvConstant.PageStage.STATE}
     */
    private String state;
    /**
     * 中转的目标对象，用于反序列化使用
     */
    private String stateTarget;
    /**
     * 配置信息
     */
    private String entityConfig;
}
