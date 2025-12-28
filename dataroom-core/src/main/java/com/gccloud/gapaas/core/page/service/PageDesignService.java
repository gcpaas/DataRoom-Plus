package com.gccloud.gapaas.core.page.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gccloud.gapaas.core.entity.PageEntity;
import com.gccloud.gapaas.core.mapper.PageDesignMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PageDesignService extends ServiceImpl<PageDesignMapper, PageEntity> {

    public PageEntity getByCode(String code){
        LambdaQueryWrapper<PageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PageEntity::getCode, code);
        PageEntity pageDesignEntity = baseMapper.selectOne(queryWrapper);
        return pageDesignEntity;
    }
}
