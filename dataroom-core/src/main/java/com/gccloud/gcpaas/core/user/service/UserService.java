package com.gccloud.gcpaas.core.user.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gccloud.gcpaas.core.config.DataRoomConfig;
import com.gccloud.gcpaas.core.entity.UserEntity;
import com.gccloud.gcpaas.core.mapper.UserMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Slf4j
@Service
public class UserService extends ServiceImpl<UserMapper, UserEntity> {

    @Resource
    private DataRoomConfig dataRoomConfig;

    public UserEntity getByUsername(String username) {
        List<UserEntity> userList = dataRoomConfig.getUserList();
        for (UserEntity user : userList) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean deleteByUsername(String username) {
        Assert.isTrue(StringUtils.isNotBlank(username), "用户名不能为空");
        return this.remove(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUsername, username));
    }

}
