package com.gccloud.gapaas.core.dataset.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gccloud.gapaas.core.entity.DatasetEntity;
import com.gccloud.gapaas.core.mapper.DatasetMapper;
import org.springframework.stereotype.Service;

@Service
public class DatasetService extends ServiceImpl<DatasetMapper, DatasetEntity> {

    public DatasetEntity getByCode(String code) {
        LambdaQueryWrapper<DatasetEntity> queryWrapper = new LambdaQueryWrapper<DatasetEntity>().eq(DatasetEntity::getCode, code);
        DatasetEntity dataSetDefinition = baseMapper.selectOne(queryWrapper);
        return dataSetDefinition;
    }
}
