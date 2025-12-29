package com.gccloud.gcpaas.core.resources;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gccloud.gcpaas.core.bean.Resp;
import com.gccloud.gcpaas.core.config.DataRoomConfig;
import com.gccloud.gcpaas.core.config.bean.ResourceBean;
import com.gccloud.gcpaas.core.constant.DataRoomRole;
import com.gccloud.gcpaas.core.entity.ResourceEntity;
import com.gccloud.gcpaas.core.mapper.ResourceMapper;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 素材库
 */
@Tag(name = "素材库")
@ApiSort(value = 20)
@Slf4j
@RequestMapping("/dataRoom/resource")
@RestController
public class ResourceController {

    @Resource
    private ResourceMapper resourceMapper;
    @Resource
    private DataRoomConfig dataRoomConfig;

    @GetMapping("/list")
    @RequiresRoles(value = DataRoomRole.DEVELOPER)
    @Operation(summary = "列表查询", description = "根据名称查询")
    @Parameters({
            @Parameter(name = "name", description = "资源名称", in = ParameterIn.QUERY),
            @Parameter(name = "parentCode", description = "目录编码", in = ParameterIn.QUERY)
    })
    public Resp<List<ResourceEntity>> list(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "parentCode", required = false) String parentCode) {
        LambdaQueryWrapper<ResourceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(parentCode), ResourceEntity::getParentCode, parentCode);
        queryWrapper.like(StringUtils.isNotBlank(name), ResourceEntity::getName, name);
        queryWrapper.orderByDesc(ResourceEntity::getUpdateDate);
        List<ResourceEntity> list = resourceMapper.selectList(queryWrapper);
        return Resp.success(list);
    }

    @GetMapping("/detail/{id}")
    @RequiresRoles(value = DataRoomRole.DEVELOPER)
    @Operation(summary = "详情", description = "根据主键查询")
    @Parameters({@Parameter(name = "id", description = "资源id", in = ParameterIn.PATH)})
    public Resp<ResourceEntity> detail(@PathVariable("id") String id) {
        ResourceEntity resourceEntity = resourceMapper.selectById(id);
        return Resp.success(resourceEntity);
    }

    @PostMapping("/upload")
    @RequiresRoles(value = DataRoomRole.DEVELOPER)
    @Operation(summary = "上传", description = "上传素材")
    public Resp<ResourceEntity> upload(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        ResourceBean resource = dataRoomConfig.getResource();
        ResourceEntity resourceEntity = new ResourceEntity();
        resourceEntity.setName(file.getOriginalFilename());
        resourceEntity.setOriginalName(file.getOriginalFilename());
        String newFileName = UUID.randomUUID().toString().replace("-", "") + "." + FilenameUtils.getExtension(file.getOriginalFilename());
        resourceEntity.setPath(newFileName);
        resourceEntity.setUrl("/" + newFileName);
        FileUtils.copyInputStreamToFile(file.getInputStream(), new File(resource.getBasePath() + File.separator + newFileName));
        return Resp.success(resourceEntity);
    }

    @PostMapping("/insert")
    @RequiresRoles(value = DataRoomRole.DEVELOPER)
    @Operation(summary = "新增", description = "新增素材")
    public Resp<String> insert(@RequestBody ResourceEntity resourceEntity) {
        resourceEntity.setUpdateDate(new Date());
        resourceMapper.updateById(resourceEntity);
        return Resp.success(resourceEntity.getId());
    }

    @PostMapping("/update")
    @RequiresRoles(value = DataRoomRole.DEVELOPER)
    @Operation(summary = "更新", description = "更新素材")
    public Resp<String> update(@RequestBody ResourceEntity resourceEntity) {
        resourceEntity.setUpdateDate(new Date());
        resourceMapper.updateById(resourceEntity);
        return Resp.success(resourceEntity.getId());
    }

    @PostMapping("/delete/{id}")
    @RequiresRoles(value = DataRoomRole.DEVELOPER)
    @Operation(summary = "删除", description = "根据主键删除素材")
    @Parameters({@Parameter(name = "id", description = "素材ID", in = ParameterIn.PATH)})
    public Resp<Void> delete(@PathVariable("id") String id) {
        resourceMapper.deleteById(id);
        return Resp.success(null);
    }
}