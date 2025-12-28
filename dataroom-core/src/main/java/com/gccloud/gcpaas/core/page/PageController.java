package com.gccloud.gcpaas.core.page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gccloud.gcpaas.core.bean.PageVo;
import com.gccloud.gcpaas.core.bean.Resp;
import com.gccloud.gcpaas.core.constant.PageStatus;
import com.gccloud.gcpaas.core.entity.PageEntity;
import com.gccloud.gcpaas.core.entity.PageStageEntity;
import com.gccloud.gcpaas.core.mapper.PageMapper;
import com.gccloud.gcpaas.core.page.dto.PageOfflineDto;
import com.gccloud.gcpaas.core.page.dto.PagePublishDto;
import com.gccloud.gcpaas.core.page.dto.PageStageSearchDto;
import com.gccloud.gcpaas.core.page.service.PageService;
import com.gccloud.gcpaas.core.page.service.PageStageService;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 页面
 */
@Slf4j
@RestController
@Controller
@RequestMapping("/dataRoom/page")
public class PageController {
    @Resource
    private PageService pageService;
    @Resource
    private PageMapper pageMapper;
    @Resource
    private PageStageService pageStageService;
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 列表查询
     *
     * @param name
     * @return
     */
    @GetMapping("/list")
    public Resp<List<PageEntity>> list(@RequestParam(name = "name") String name) {
        LambdaQueryWrapper<PageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(PageEntity::getUpdateDate);
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(PageEntity::getName, name);
        }
        List<PageEntity> list = pageService.list(queryWrapper);
        return Resp.success(list);
    }

    /**
     * 详情查询
     *
     * @param code
     * @return
     */
    @GetMapping("/detail/{code}")
    public Resp<PageEntity> detail(@PathVariable("code") String code) {
        PageEntity pageDesignEntity = pageMapper.getByCode(code);
        return Resp.success(pageDesignEntity);
    }

    /**
     * 新增
     *
     * @param pageDesignEntity
     * @return
     */
    @PostMapping("/insert")
    public Resp<String> insert(@RequestBody PageEntity pageDesignEntity) {
        log.info("新增页面 {}", pageDesignEntity);
        pageService.saveOrUpdate(pageDesignEntity);
        return Resp.success(pageDesignEntity.getId());
    }

    /**
     * 更新
     *
     * @param pageDesignEntity
     * @return
     */
    @PostMapping("/update")
    public Resp<String> update(@RequestBody PageEntity pageDesignEntity) {
        pageDesignEntity.setUpdateDate(new Date());
        pageService.updateById(pageDesignEntity);
        return Resp.success(pageDesignEntity.getId());
    }

    /**
     * 发布
     *
     * @param pagePublishDto
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/publish")
    public Resp<String> publish(@RequestBody PagePublishDto pagePublishDto) throws JsonProcessingException {
        // 修改发布状态
        LambdaUpdateWrapper<PageEntity> updateWrapper = new LambdaUpdateWrapper<PageEntity>()
                .set(PageEntity::getPageStatus, PageStatus.PUBLISHED)
                .set(PageEntity::getUpdateDate, new Date())
                .eq(PageEntity::getCode, pagePublishDto.getCode());
        pageService.update(updateWrapper);

        // 历史发布转为记录
        LambdaUpdateWrapper<PageStageEntity> pageStateUpdateWrapper = new LambdaUpdateWrapper<PageStageEntity>()
                .eq(PageStageEntity::getPageStatus, PageStatus.PUBLISHED)
                .eq(PageStageEntity::getPageCode, pagePublishDto.getCode())
                .set(PageStageEntity::getUpdateDate, new Date())
                .set(PageStageEntity::getRemark, "发布自动备份")
                .set(PageStageEntity::getPageStatus, PageStatus.HISTORY);
        pageStageService.update(pageStateUpdateWrapper);

        // 设计态生成发布
        PageStageEntity pageStage = pageStageService.getByCode(pagePublishDto.getCode(), PageStatus.DESIGN);
        PageStageEntity newPageStage = new PageStageEntity();
        BeanUtils.copyProperties(pageStage, newPageStage);
        newPageStage.setPageStatus(PageStatus.PUBLISHED);
        newPageStage.setRemark(pagePublishDto.getRemark());
        pageStageService.save(newPageStage);
        return Resp.success(pageStage.getId());
    }

    /**
     * 取消发布
     *
     * @param pageOfflineDto
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/offline")
    public Resp<Void> offline(@RequestBody PageOfflineDto pageOfflineDto) {
        LambdaUpdateWrapper<PageEntity> updateWrapper = new LambdaUpdateWrapper<PageEntity>()
                .set(PageEntity::getPageStatus, PageStatus.DESIGN)
                .set(PageEntity::getUpdateDate, new Date())
                .eq(PageEntity::getCode, pageOfflineDto.getCode());
        pageService.update(updateWrapper);

        // 历史发布转为记录
        LambdaUpdateWrapper<PageStageEntity> pageStateUpdateWrapper = new LambdaUpdateWrapper<PageStageEntity>()
                .eq(PageStageEntity::getPageStatus, PageStatus.PUBLISHED)
                .eq(PageStageEntity::getPageCode, pageOfflineDto.getCode())
                .set(PageStageEntity::getUpdateDate, new Date())
                .set(PageStageEntity::getRemark, pageOfflineDto.getRemark())
                .set(PageStageEntity::getPageStatus, PageStatus.HISTORY);
        pageStageService.update(pageStateUpdateWrapper);
        return Resp.success(null);
    }

    /**
     * 删除
     *
     * @param pageCode
     * @return
     */
    @PostMapping("/delete/{code}")
    public Resp<Void> delete(@PathVariable("code") String pageCode) {

        LambdaQueryWrapper<PageStageEntity> deleteStageWrapper = new LambdaQueryWrapper<>();
        deleteStageWrapper.eq(PageStageEntity::getCode, pageCode);
        pageStageService.remove(deleteStageWrapper);

        LambdaQueryWrapper<PageEntity> deleteDesignWrapper = new LambdaQueryWrapper<>();
        deleteDesignWrapper.eq(PageEntity::getCode, pageCode);
        pageService.remove(deleteDesignWrapper);

        return Resp.success(null);
    }

    /**
     * 更新页面配置、仅能更新设计态
     *
     * @param pageStage
     * @return
     */
    @PostMapping("/updatePageConfig")
    public Resp<Void> updatePageConfig(@RequestBody PageStageEntity pageStage) {
        LambdaUpdateWrapper<PageStageEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PageStageEntity::getPageConfig, pageStage.getPageConfig());
        updateWrapper.eq(PageStageEntity::getPageCode, pageStage.getPageCode());
        updateWrapper.eq(PageStageEntity::getPageStatus, PageStatus.DESIGN);
        updateWrapper.set(PageStageEntity::getUpdateDate, new Date());
        pageStageService.update(updateWrapper);
        return Resp.success(null);
    }

    /**
     * 获取页面中转状态列表
     *
     * @param stageSearch
     * @return
     * @throws ParseException
     */
    @GetMapping("/stage/list")
    public Resp<PageVo<PageStageEntity>> stagePage(PageStageSearchDto stageSearch) throws ParseException {
        Page<PageStageEntity> searchPage = new Page<>(stageSearch.getCurrent(), stageSearch.getSize());
        LambdaQueryWrapper<PageStageEntity> queryWrapper = new LambdaQueryWrapper<>();
        // 排除的字段
        List<String> excludeFields = Lists.newArrayList("pageConfig");
        queryWrapper.select(PageStageEntity.class, tableFieldInfo -> {
            if (excludeFields.contains(tableFieldInfo.getProperty())) {
                return false;
            }
            return true;
        });
        queryWrapper.eq(PageStageEntity::getPageCode, stageSearch.getCode());
        queryWrapper.eq(stageSearch.getPageStatus() != null, PageStageEntity::getPageStatus, stageSearch.getPageStatus());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        queryWrapper.ge(StringUtils.isNotBlank(stageSearch.getStartDate()), PageStageEntity::getCreateDate, sdf.parse(stageSearch.getStartDate()));
        queryWrapper.lt(StringUtils.isNotBlank(stageSearch.getEndDate()), PageStageEntity::getCreateDate, sdf.parse(stageSearch.getEndDate()));
        Page<PageStageEntity> page = pageStageService.page(searchPage, queryWrapper);
        return Resp.success(PageVo.build(page));
    }

    /**
     * 删除中转记录
     *
     * @param id
     * @return
     */
    @PostMapping("/stage/delete/{id}")
    public Resp<String> stageDelete(@PathVariable("id") String id) {
        pageStageService.removeById(id);
        return Resp.success(id);
    }

    /**
     * 清空历史、快照记录
     *
     * @return
     */
    @PostMapping("/stage/clear/{code}/{state}")
    public Resp<Void> stageClear(@PathVariable("code") String code, @PathVariable("state") String state) {
        PageStatus pageStatus = PageStatus.valueOf(state);
        if (!(pageStatus == PageStatus.HISTORY || pageStatus == PageStatus.SNAPSHOT)) {
            throw new RuntimeException("状态错误");
        }
        LambdaQueryWrapper<PageStageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PageStageEntity::getCode, code);
        queryWrapper.eq(PageStageEntity::getPageStatus, pageStatus);
        pageStageService.remove(queryWrapper);
        return Resp.success(null);
    }

    /**
     * 回退到某个历史、快照
     *
     * @param id
     * @return
     */
    @PostMapping("/stage/rollback/{id}")
    public Resp<String> stageRollback(@PathVariable("id") String id) throws JsonProcessingException {
        PageStageEntity pageStage = pageStageService.getById(id);

        // 当前设计态修改为历史
        LambdaUpdateWrapper<PageStageEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PageStageEntity::getPageCode, pageStage.getPageCode());
        updateWrapper.eq(PageStageEntity::getPageStatus, PageStatus.DESIGN);
        updateWrapper.set(PageStageEntity::getUpdateDate, new Date());
        updateWrapper.set(PageStageEntity::getPageStatus, PageStatus.HISTORY);
        pageStageService.update(updateWrapper);

        // 历史态转为设计态
        updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PageStageEntity::getId, id);
        updateWrapper.set(PageStageEntity::getUpdateDate, new Date());
        updateWrapper.set(PageStageEntity::getPageStatus, PageStatus.DESIGN);
        pageStageService.update(updateWrapper);

        return Resp.success(id);
    }

}