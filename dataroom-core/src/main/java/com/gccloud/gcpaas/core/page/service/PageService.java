package com.gccloud.gcpaas.core.page.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gccloud.gcpaas.core.entity.PageEntity;
import com.gccloud.gcpaas.core.mapper.PageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PageService extends ServiceImpl<PageMapper, PageEntity> {

    public PageEntity getByCode(String code){
        LambdaQueryWrapper<PageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PageEntity::getCode, code);
        PageEntity pageDesignEntity = baseMapper.selectOne(queryWrapper);
        return pageDesignEntity;
    }
}
