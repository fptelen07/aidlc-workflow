package com.awsome.shop.auth.repository.mysql.mapper.user;

import com.awsome.shop.auth.repository.mysql.po.user.UserPO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<UserPO> {
}
