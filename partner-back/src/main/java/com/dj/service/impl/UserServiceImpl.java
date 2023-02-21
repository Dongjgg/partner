package com.dj.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dj.entity.User;
import com.dj.exception.ServiceException;
import com.dj.mapper.UserMapper;
import com.dj.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dj
 * @since 2023-02-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User login(User user) {
        User dbUser = null;
        try {
            dbUser = getOne(new UpdateWrapper<User>().eq("username", user.getUsername()));
        }catch (Exception e){
            throw new RuntimeException("系统异常");
        }
        if (dbUser==null){
            throw new ServiceException("未找到用户");
        }
        if (!user.getPassword().equals(dbUser.getPassword())){
            throw new ServiceException("用户名或密码错误");
        }
        return dbUser;
    }
}
