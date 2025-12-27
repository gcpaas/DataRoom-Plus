package com.gccloud.gapaas.core.page.dto;

import lombok.Data;

@Data
public class PageStageSearchDto {
    private Integer size;
    private Integer current;
    private String code;
    private String state;
    private String startDate;
    private String endDate;
}
