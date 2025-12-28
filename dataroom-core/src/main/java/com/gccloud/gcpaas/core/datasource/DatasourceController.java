package com.gccloud.gcpaas.core.datasource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gccloud.gcpaas.core.MaxvConstant;
import com.gccloud.gcpaas.core.SuperController;
import com.gccloud.gcpaas.core.bean.Resp;
import com.gccloud.gcpaas.core.entity.DataSourceEntity;
import com.gccloud.gcpaas.core.mapper.DatasourceMapper;
import com.gccloud.gcpaas.core.util.CodeWorker;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 页面
 */
@RestController
@Controller
@RequestMapping("/maxv/datasource")
public class DatasourceController extends SuperController {

    @Resource
    private DatasourceMapper datasourceMapper;

    @GetMapping("/list")
    public Resp<List<DataSourceEntity>> list(@RequestParam(name = "name", required = false) String name) {
        LambdaQueryWrapper<DataSourceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(DataSourceEntity::getId, DataSourceEntity::getCode, DataSourceEntity::getName, DataSourceEntity::getDataSourceType, DataSourceEntity::getUpdateDate);
        queryWrapper.orderByDesc(DataSourceEntity::getUpdateDate);
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(DataSourceEntity::getName, name);
        }
        List<DataSourceEntity> list = datasourceMapper.selectList(queryWrapper);
        return Resp.success(list);
    }

    @GetMapping("/detail/{id}")
    public Resp<DataSourceEntity> detail(@PathVariable("id") String id) {
        DataSourceEntity datasourceEntity = datasourceMapper.selectById(id);
        return Resp.success(datasourceEntity);
    }

    @PostMapping("/insert")
    public Resp<String> insert(@RequestBody DataSourceEntity datasourceEntity) {
        datasourceEntity.setCode(CodeWorker.generateCode(MaxvConstant.Datasource.CODE_PREFIX));
        datasourceMapper.insert(datasourceEntity);
        return Resp.success(datasourceEntity.getId());
    }

    @PostMapping("/update")
    public Resp<String> update(@RequestBody DataSourceEntity datasourceEntity) {
        datasourceEntity.setUpdateDate(new Date());
        datasourceMapper.updateById(datasourceEntity);
        return Resp.success(datasourceEntity.getId());
    }

    @PostMapping("/delete/{id}")
    public Resp<Void> delete(@PathVariable("id") String id) {
        datasourceMapper.deleteById(id);
        return Resp.success(null);
    }
}