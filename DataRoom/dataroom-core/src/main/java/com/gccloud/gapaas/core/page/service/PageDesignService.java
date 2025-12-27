package com.gccloud.gapaas.core.page.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gccloud.gapaas.core.entity.PageDesignEntity;
import com.gccloud.gapaas.core.mapper.PageDesignMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PageDesignService extends ServiceImpl<PageDesignMapper, PageDesignEntity> {

    public PageDesignEntity getByCode(String code){
        LambdaQueryWrapper<PageDesignEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PageDesignEntity::getCode, code);
        PageDesignEntity pageDesignEntity = baseMapper.selectOne(queryWrapper);
        return pageDesignEntity;
    }
}
