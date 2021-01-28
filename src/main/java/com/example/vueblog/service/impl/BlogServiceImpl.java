package com.example.vueblog.service.impl;

import com.example.vueblog.entity.Blog;
import com.example.vueblog.mapper.BlogMapper;
import com.example.vueblog.service.IBlogService;
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
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

}
