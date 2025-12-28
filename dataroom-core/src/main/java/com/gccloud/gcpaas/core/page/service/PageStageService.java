package com.gccloud.gcpaas.core.page.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gccloud.gcpaas.core.constant.PageStatus;
import com.gccloud.gcpaas.core.entity.PageStageEntity;
import com.gccloud.gcpaas.core.mapper.PageStageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PageStageService extends ServiceImpl<PageStageMapper, PageStageEntity> {

    public PageStageEntity getByCode(String code, PageStatus status) {
        LambdaQueryWrapper<PageStageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PageStageEntity::getCode, code)
                .eq(PageStageEntity::getPageStatus, status);
        return baseMapper.selectOne(queryWrapper, false);
    }

}
