package com.gccloud.gapaas.core.datasource.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gccloud.gapaas.core.entity.DataSourceEntity;
import com.gccloud.gapaas.core.mapper.DatasourceMapper;
import org.springframework.stereotype.Service;

@Service
public class DatasourceService extends ServiceImpl<DatasourceMapper, DataSourceEntity> {

    public DataSourceEntity getByCode(String code) {
        LambdaQueryWrapper<DataSourceEntity> queryWrapper = new LambdaQueryWrapper<DataSourceEntity>().eq(DataSourceEntity::getCode, code);
        DataSourceEntity dataSourceDefinition = baseMapper.selectOne(queryWrapper);
        return dataSourceDefinition;
    }

}
