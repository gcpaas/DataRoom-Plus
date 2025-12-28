package com.gccloud.gcpaas.core.datasource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gccloud.gcpaas.core.DataRoomConstant;
import com.gccloud.gcpaas.core.bean.Resp;
import com.gccloud.gcpaas.core.entity.DataSourceEntity;
import com.gccloud.gcpaas.core.mapper.DataSourceMapper;
import com.gccloud.gcpaas.core.util.CodeWorker;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 数据源
 */
@RestController
@Controller
@RequestMapping("/dataRoom/dataSource")
public class DatasourceController {

    @Resource
    private DataSourceMapper datasourceMapper;

    @GetMapping("/list")
    public Resp<List<DataSourceEntity>> list(@RequestParam(name = "name", required = false) String name) {
        LambdaQueryWrapper<DataSourceEntity> queryWrapper = new LambdaQueryWrapper<>();
        // 排除的字段
        List<String> excludeFields = Lists.newArrayList("dataSource");
        queryWrapper.select(DataSourceEntity.class, tableFieldInfo -> {
            if (excludeFields.contains(tableFieldInfo.getProperty())) {
                return false;
            }
            return true;
        });
        queryWrapper.orderByDesc(DataSourceEntity::getUpdateDate);
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(DataSourceEntity::getName, name);
        }
        List<DataSourceEntity> list = datasourceMapper.selectList(queryWrapper);
        return Resp.success(list);
    }

    @GetMapping("/detail/{code}")
    public Resp<DataSourceEntity> detail(@PathVariable("code") String code) {
        DataSourceEntity datasourceEntity = datasourceMapper.getByCode(code);
        return Resp.success(datasourceEntity);
    }

    @PostMapping("/insert")
    public Resp<String> insert(@RequestBody DataSourceEntity datasourceEntity) {
        datasourceEntity.setCode(CodeWorker.generateCode(DataRoomConstant.Datasource.CODE_PREFIX));
        datasourceMapper.insert(datasourceEntity);
        return Resp.success(datasourceEntity.getId());
    }

    @PostMapping("/update")
    public Resp<String> update(@RequestBody DataSourceEntity datasourceEntity) {
        datasourceEntity.setUpdateDate(new Date());
        datasourceMapper.updateById(datasourceEntity);
        return Resp.success(datasourceEntity.getId());
    }

    @PostMapping("/delete/{code}")
    public Resp<Void> delete(@PathVariable("code") String code) {
        datasourceMapper.deleteByCode(code);
        return Resp.success(null);
    }
}