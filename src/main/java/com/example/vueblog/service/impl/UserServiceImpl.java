package com.example.vueblog.service.impl;

import com.example.vueblog.entity.User;
import com.example.vueblog.mapper.UserMapper;
import com.example.vueblog.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author paulro1991
 * @since 2021-01-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
