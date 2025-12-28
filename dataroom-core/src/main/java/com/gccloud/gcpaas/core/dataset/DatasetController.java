package com.gccloud.gcpaas.core.dataset;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gccloud.gcpaas.core.DataRoomConstant;
import com.gccloud.gcpaas.core.bean.Resp;
import com.gccloud.gcpaas.core.bizComponent.ComponentDefinition;
import com.gccloud.gcpaas.core.bizComponent.SysComponentService;
import com.gccloud.gcpaas.core.dataset.service.AbstractDatasetService;
import com.gccloud.gcpaas.core.dataset.service.DatasetService;
import com.gccloud.gcpaas.core.dataset.service.DatasetServiceFactory;
import com.gccloud.gcpaas.core.entity.DatasetEntity;
import com.gccloud.gcpaas.core.mapper.DatasetMapper;
import com.gccloud.gcpaas.core.util.CodeWorker;
import com.gccloud.gcpaas.core.util.JsonUtils;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 数据集
 */
@RestController
@Controller
@RequestMapping("/dataRoom/dataset")
public class DatasetController {

    private static final Logger log = LoggerFactory.getLogger(DatasetController.class);

    @Resource
    private DatasetMapper datasetMapper;
    @Resource
    private DatasetService datasetService;
    @Resource
    private SysComponentService componentCacheService;
    @Resource
    private DatasetServiceFactory datasetServiceFactory;

    @GetMapping("/list")
    public Resp<List<DatasetEntity>> list(@RequestParam(name = "name", required = false) String name) {
        LambdaQueryWrapper<DatasetEntity> queryWrapper = new LambdaQueryWrapper<>();
        // 排除的字段
        List<String> excludeFields = Lists.newArrayList("dataset","inputList","outputList");
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
    public Resp<DatasetEntity> detail(@PathVariable("code") String code) {
        DatasetEntity datasetEntity = datasetMapper.getByCode(code);
        return Resp.success(datasetEntity);
    }

    @GetMapping("/detailByCode/{code}")
    public Resp<DatasetEntity> detailByCode(@PathVariable("code") String code) {
        DatasetEntity datasetEntity = datasetService.getByCode(code);
        return Resp.success(datasetEntity);
    }

    @PostMapping("/insert")
    public Resp<String> insert(@RequestBody DatasetEntity datasetEntity) {
        datasetEntity.setCode(CodeWorker.generateCode(DataRoomConstant.Dataset.CODE_PREFIX));
        datasetMapper.insert(datasetEntity);
        return Resp.success(datasetEntity.getId());
    }

    @PostMapping("/update")
    public Resp<String> update(@RequestBody DatasetEntity datasetEntity) {
        datasetEntity.setUpdateDate(new Date());
        datasetMapper.updateById(datasetEntity);
        return Resp.success(datasetEntity.getId());
    }

    @PostMapping("/delete/{code}")
    public Resp<Void> delete(@PathVariable("code") String code) {
        datasetMapper.deleteByCode(code);
        return Resp.success(null);
    }

    @PostMapping("/run")
    public Resp<DatasetRunResponse> run(@RequestBody DatasetRunRequest datasetRunRequest) {
        if (StringUtils.isBlank(datasetRunRequest.getDatasetCode())) {
            // 直接获取图表默认数据
            ComponentDefinition compDef = componentCacheService.getComponentDefinitionByName(datasetRunRequest.getChartName());
            String path = compDef.getPath().substring(1);
            try (InputStream is = DatasetController.class.getClassLoader().getResourceAsStream(path + "/data.json")) {
                String dataStr = IOUtils.toString(is);
                Object data = JsonUtils.getInstance().readValue(dataStr, Object.class);
                DatasetRunResponse datasetRunResponse = new DatasetRunResponse();
                datasetRunResponse.setData(data);
                return Resp.success(datasetRunResponse);
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
            }
        }
        DatasetEntity datasetEntity = datasetService.getByCode(datasetRunRequest.getDatasetCode());
        Assert.isTrue(datasetEntity != null, "数据集不存在");
        AbstractDatasetService dataSetService = datasetServiceFactory.getDatasetService(datasetEntity.getDatasetType());
        DatasetRunResponse datasetRunResponse = dataSetService.run(datasetRunRequest, datasetEntity);
        return Resp.success(datasetRunResponse);
    }

    @PostMapping("/run/test")
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