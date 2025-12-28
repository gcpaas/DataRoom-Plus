package com.gccloud.gcpaas.core.page;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gccloud.gcpaas.core.MaxvConstant;
import com.gccloud.gcpaas.core.SuperController;
import com.gccloud.gcpaas.core.bean.PageVo;
import com.gccloud.gcpaas.core.bean.Resp;
import com.gccloud.gcpaas.core.entity.PageEntity;
import com.gccloud.gcpaas.core.entity.PageReleaseEntity;
import com.gccloud.gcpaas.core.entity.PageStageEntity;
import com.gccloud.gcpaas.core.page.dto.PageOfflineDto;
import com.gccloud.gcpaas.core.page.dto.PagePublishDto;
import com.gccloud.gcpaas.core.page.dto.PageStageSearchDto;
import com.gccloud.gcpaas.core.page.service.PageDesignService;
import com.gccloud.gcpaas.core.page.service.PageReleaseService;
import com.gccloud.gcpaas.core.page.service.PageStageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/maxv/page")
public class PageController extends SuperController {
    @Resource
    private PageDesignService pageDesignService;
    @Resource
    private PageReleaseService pageReleaseService;
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
        queryWrapper.select(PageEntity::getId, PageEntity::getPageType, PageEntity::getName, PageEntity::getRemark, PageEntity::getState, PageEntity::getUpdateDate);
        queryWrapper.orderByDesc(PageEntity::getUpdateDate);
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(PageEntity::getName, name);
        }
        List<PageEntity> list = pageDesignService.list(queryWrapper);
        return Resp.success(list);
    }

    /**
     * 详情查询
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public Resp<PageEntity> detail(@PathVariable("id") String id) {
        PageEntity pageDesignEntity = pageDesignService.getById(id);
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
        pageDesignService.saveOrUpdate(pageDesignEntity);
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
        pageDesignService.updateById(pageDesignEntity);
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
                .set(PageEntity::getState, MaxvConstant.PageDesign.STATE.PUBLISHED)
                .set(PageEntity::getUpdateDate, new Date())
                .eq(PageEntity::getCode, pagePublishDto.getCode());
        pageDesignService.update(updateWrapper);
        // 发布到release表中
        PageReleaseEntity pageReleaseEntity = pageReleaseService.getByCode(pagePublishDto.getCode());
        // 新增一条历史记录
        if (pageReleaseEntity != null) {
            PageStageEntity pageStageEntity = new PageStageEntity();
            pageStageEntity.setCode(pagePublishDto.getCode());
            pageStageEntity.setRemark("发布前自动备份");
            pageStageEntity.setState(MaxvConstant.PageStage.STATE.HISTORY);
            pageStageEntity.setEntityConfig(objectMapper.writeValueAsString(pageReleaseEntity));
            pageStageService.save(pageStageEntity);
        }
        if (pageReleaseEntity == null) {
            pageReleaseEntity = new PageReleaseEntity();
        }
        // 更新发布记录
        PageEntity pageDesignEntity = pageDesignService.getByCode(pagePublishDto.getCode());
        pageReleaseEntity.setCode(pageReleaseEntity.getCode());
        pageReleaseEntity.setRemark(pagePublishDto.getRemark());
        pageReleaseEntity.setEntityConfig(pageDesignEntity);
        pageReleaseService.saveOrUpdate(pageReleaseEntity);
        return Resp.success(pageReleaseEntity.getId());
    }

    /**
     * 取消发布
     *
     * @param pageOfflineDto
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping("/offline")
    public Resp<Void> offline(@RequestBody PageOfflineDto pageOfflineDto) throws JsonProcessingException {
        LambdaUpdateWrapper<PageEntity> updateWrapper = new LambdaUpdateWrapper<PageEntity>()
                .set(PageEntity::getState, MaxvConstant.PageDesign.STATE.DESIGN)
                .set(PageEntity::getUpdateDate, new Date())
                .eq(PageEntity::getCode, pageOfflineDto.getCode());
        pageDesignService.update(updateWrapper);
        // 发布到release表中
        PageReleaseEntity pageReleaseEntity = pageReleaseService.getByCode(pageOfflineDto.getCode());
        // 新增一条历史记录
        if (pageReleaseEntity != null) {
            PageStageEntity pageStageEntity = new PageStageEntity();
            pageStageEntity.setCode(pageOfflineDto.getCode());
            pageStageEntity.setRemark("取消发布前自动备份");
            pageStageEntity.setState(MaxvConstant.PageStage.STATE.HISTORY);
            pageStageEntity.setEntityConfig(objectMapper.writeValueAsString(pageReleaseEntity));
            pageStageService.save(pageStageEntity);
        }
        // 删除
        LambdaQueryWrapper<PageReleaseEntity> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(PageReleaseEntity::getCode, pageOfflineDto.getCode());
        pageReleaseService.remove(deleteWrapper);
        return Resp.success(null);
    }

    /**
     * 删除
     *
     * @param code
     * @return
     */
    @PostMapping("/delete/{code}")
    public Resp<Void> delete(@PathVariable("code") String code) {
        LambdaQueryWrapper<PageReleaseEntity> deleteReleaseWrapper = new LambdaQueryWrapper<>();
        deleteReleaseWrapper.eq(PageReleaseEntity::getCode, code);
        pageReleaseService.remove(deleteReleaseWrapper);

        LambdaQueryWrapper<PageStageEntity> deleteStageWrapper = new LambdaQueryWrapper<>();
        deleteStageWrapper.eq(PageStageEntity::getCode, code);
        pageStageService.remove(deleteStageWrapper);

        LambdaQueryWrapper<PageEntity> deleteDesignWrapper = new LambdaQueryWrapper<>();
        deleteDesignWrapper.eq(PageEntity::getCode, code);
        pageDesignService.remove(deleteDesignWrapper);

        return Resp.success(null);
    }

    @PostMapping("/updatePageConfig")
    public Resp<Void> updatePageConfig(@RequestBody PageEntity pageDefinition) {
        LambdaUpdateWrapper<PageEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PageEntity::getPageConfig, pageDefinition.getPageConfig());
        updateWrapper.eq(PageEntity::getId, pageDefinition.getId());
        updateWrapper.set(PageEntity::getUpdateDate, new Date());
        updateWrapper.set(PageEntity::getName, pageDefinition.getName());
        pageDesignService.update(updateWrapper);
        return Resp.success(null);
    }

    /**
     * 获取页面中转状态列表
     *
     * @param stageSearch
     * @return
     * @throws ParseException
     */
    @GetMapping("/stage")
    public Resp<PageVo<PageStageEntity>> stagePage(PageStageSearchDto stageSearch) throws ParseException {
        Page<PageStageEntity> searchPage = new Page<>(stageSearch.getCurrent(), stageSearch.getSize());
        LambdaQueryWrapper<PageStageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(PageStageEntity::getId, PageStageEntity::getCode, PageStageEntity::getRemark, PageStageEntity::getState, PageStageEntity::getCreateDate, PageStageEntity::getCreateUser);
        queryWrapper.eq(PageStageEntity::getCode, stageSearch.getCode());
        queryWrapper.eq(StringUtils.isNotBlank(stageSearch.getState()), PageStageEntity::getState, stageSearch.getState());
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
     * 清空历史记录
     *
     * @return
     */
    @PostMapping("/stage/clear/{code}/{state}")
    public Resp<Void> stageClear(@PathVariable("code") String code, @PathVariable("state") String state) {
        LambdaQueryWrapper<PageStageEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PageStageEntity::getCode, code);
        queryWrapper.eq(PageStageEntity::getState, state);
        pageStageService.remove(queryWrapper);
        return Resp.success(null);
    }

    /**
     * 回退到设计态
     *
     * @param id
     * @return
     */
    @PostMapping("/stage/rollback/{id}")
    public Resp<String> stageRollback(@PathVariable("id") String id) throws JsonProcessingException {
        PageStageEntity pageStageEntity = pageStageService.getById(id);
        PageEntity pageDesignEntity = pageDesignService.getByCode(pageStageEntity.getCode());
        // 当前设计态转为历史记录
        PageStageEntity pageHistoryEntity = new PageStageEntity();
        pageHistoryEntity.setCode(pageStageEntity.getCode());
        pageHistoryEntity.setRemark("回滚前自动备份");
        pageHistoryEntity.setState(MaxvConstant.PageStage.STATE.HISTORY);
        pageHistoryEntity.setEntityConfig(objectMapper.writeValueAsString(pageDesignEntity));
        pageStageService.save(pageHistoryEntity);
        // 回退历史为当前设计态
        PageEntity rollbackPageDesignEntity = null;
        String stateTarget = pageStageEntity.getStateTarget();
        if (MaxvConstant.PageStage.TARGET.PAGE_RELEASE.equals(stateTarget)) {
            PageReleaseEntity pageReleaseEntity = objectMapper.readValue(pageStageEntity.getEntityConfig(), PageReleaseEntity.class);
            rollbackPageDesignEntity = pageReleaseEntity.getEntityConfig();
        } else if (MaxvConstant.PageStage.TARGET.PAGE_DESIGN.equals(stateTarget)) {
            rollbackPageDesignEntity = objectMapper.readValue(pageStageEntity.getEntityConfig(), PageEntity.class);
        }
        // 覆盖回退
        pageDesignEntity.setPageConfig(rollbackPageDesignEntity.getPageConfig());
        pageDesignService.updateById(pageDesignEntity);
        return Resp.success(pageDesignEntity.getId());
    }

}