package com.gccloud.gcpaas.core.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gccloud.gcpaas.core.entity.UserEntity;
import com.gccloud.gcpaas.core.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, UserEntity> {

    public UserEntity getByUsername(String username) {
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getUsername, username);
        return this.getOne(queryWrapper, false);
    }

    public boolean deleteByUsername(String username) {
        Assert.isTrue(StringUtils.isNotBlank(username), "用户名不能为空");
        return this.remove(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, username));
    }

}
