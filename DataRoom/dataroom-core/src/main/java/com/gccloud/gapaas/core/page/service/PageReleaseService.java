package com.gccloud.gapaas.core.page.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gccloud.gapaas.core.entity.PageReleaseEntity;
import com.gccloud.gapaas.core.mapper.PageReleaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PageReleaseService extends ServiceImpl<PageReleaseMapper, PageReleaseEntity> {

    public PageReleaseEntity getByCode(String code){
        LambdaQueryWrapper<PageReleaseEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PageReleaseEntity::getCode, code);
        PageReleaseEntity pageReleaseEntity = baseMapper.selectOne(queryWrapper);
        return pageReleaseEntity;
    }
}
