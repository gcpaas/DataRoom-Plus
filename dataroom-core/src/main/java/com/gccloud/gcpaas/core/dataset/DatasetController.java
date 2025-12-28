package com.gccloud.gcpaas.core.dataset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gccloud.gcpaas.core.constant.DataRoomConstant;
import com.gccloud.gcpaas.core.bean.Resp;
import com.gccloud.gcpaas.core.constant.DataRoomRole;
import com.gccloud.gcpaas.core.dataset.service.AbstractDatasetService;
import com.gccloud.gcpaas.core.dataset.service.DatasetServiceFactory;
import com.gccloud.gcpaas.core.entity.DatasetEntity;
import com.gccloud.gcpaas.core.mapper.DatasetMapper;
import com.gccloud.gcpaas.core.util.CodeWorker;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 数据集
 */
@Slf4j
@Tag(name = "数据集")
@ApiSort(value = 200)
@RestController
@Controller
@RequestMapping("/dataRoom/dataset")
public class DatasetController {

    @Resource
    private DatasetMapper datasetMapper;
    @Resource
    private DatasetServiceFactory datasetServiceFactory;

    @GetMapping("/list")
    @RequiresRoles(value = DataRoomRole.DEVELOPER)
    @Operation(summary = "列表查询", description = "根据名称查询")
    @Parameters({@Parameter(name = "name", description = "数据源名称", in = ParameterIn.QUERY)})
    public Resp<List<DatasetEntity>> list(@RequestParam(name = "name", required = false) String name) {
        LambdaQueryWrapper<DatasetEntity> queryWrapper = new LambdaQueryWrapper<>();
        // 排除的字段
        List<String> excludeFields = Lists.newArrayList("dataset", "inputList", "outputList");
        queryWrapper.select(DatasetEntity.class, tableFieldInfo -> {
            if (excludeFields.contains(tableFieldInfo.getProperty())) {
                return false;
            }
            return true;
        });
        queryWrapper.orderByDesc(DatasetEntity::getUpdateDate);
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(DatasetEntity::getName, name);
        }
        List<DatasetEntity> list = datasetMapper.selectList(queryWrapper);
        return Resp.success(list);
    }


    @GetMapping("/detail/{code}")
    @RequiresRoles(value = DataRoomRole.DEVELOPER)
    @Operation(summary = "详情", description = "根据编码查询")
    @Parameters({@Parameter(name = "code", description = "数据集编码", in = ParameterIn.PATH)})
    public Resp<DatasetEntity> detail(@PathVariable("code") String code) {
        DatasetEntity datasetEntity = datasetMapper.getByCode(code);
        return Resp.success(datasetEntity);
    }

    @PostMapping("/insert")
    @RequiresRoles(value = DataRoomRole.DEVELOPER)
    @Operation(summary = "新增", description = "新增数据集")
    public Resp<String> insert(@RequestBody DatasetEntity datasetEntity) {
        datasetEntity.setCode(CodeWorker.generateCode(DataRoomConstant.Dataset.CODE_PREFIX));
        datasetMapper.insert(datasetEntity);
        return Resp.success(datasetEntity.getId());
    }

    @PostMapping("/update")
    @RequiresRoles(value = DataRoomRole.DEVELOPER)
    @Operation(summary = "更新", description = "更新数据集")
    public Resp<String> update(@RequestBody DatasetEntity datasetEntity) {
        datasetEntity.setUpdateDate(new Date());
        datasetMapper.updateById(datasetEntity);
        return Resp.success(datasetEntity.getId());
    }

    @PostMapping("/delete/{code}")
    @RequiresRoles(value = DataRoomRole.DEVELOPER)
    @Operation(summary = "删除", description = "根据编码删除数据集")
    @Parameters({@Parameter(name = "code", description = "数据集编码", in = ParameterIn.PATH)})
    public Resp<Void> delete(@PathVariable("code") String code) {
        datasetMapper.deleteByCode(code);
        return Resp.success(null);
    }

    @PostMapping("/run")
    @RequiresRoles(value = DataRoomRole.SHARER)
    @Operation(summary = "执行", description = "执行数据集")
    public Resp<DatasetRunResponse> run(@RequestBody DatasetRunRequest datasetRunRequest) {
        DatasetEntity datasetEntity = datasetMapper.getByCode(datasetRunRequest.getDatasetCode());
        Assert.isTrue(datasetEntity != null, "数据集不存在");
        AbstractDatasetService dataSetService = datasetServiceFactory.getDatasetService(datasetEntity.getDatasetType());
        DatasetRunResponse datasetRunResponse = dataSetService.run(datasetRunRequest, datasetEntity);
        return Resp.success(datasetRunResponse);
    }

    @PostMapping("/run/test")
    @RequiresRoles(value = DataRoomRole.SHARER)
    @Operation(summary = "测试执行", description = "测试数据集")
    public Resp<DatasetRunResponse> runTest(@RequestBody DatasetTestRequest datasetTestRequest) {
        log.info("入参 {}", datasetTestRequest.getInputParam());
        DatasetEntity dataSetDefinition = datasetTestRequest.getDataset();
        Assert.isTrue(dataSetDefinition != null, "数据集不存在");
        AbstractDatasetService dataSetService = datasetServiceFactory.getDatasetService(dataSetDefinition.getDatasetType());
        DatasetRunRequest runDTO = new DatasetRunRequest();
        runDTO.setInputParam(datasetTestRequest.getInputParam());
        DatasetRunResponse datasetRunResponse = dataSetService.run(runDTO, dataSetDefinition);
        return Resp.success(datasetRunResponse);
    }
}