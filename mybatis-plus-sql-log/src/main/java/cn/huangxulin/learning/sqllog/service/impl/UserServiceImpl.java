package cn.huangxulin.learning.sqllog.service.impl;

import cn.huangxulin.learning.sqllog.entity.User;
import cn.huangxulin.learning.sqllog.mapper.UserMapper;
import cn.huangxulin.learning.sqllog.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author hxl
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
