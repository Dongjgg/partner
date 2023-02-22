package com.dj.service;

import com.dj.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dj
 * @since 2023-02-21
 */
public interface IUserService extends IService<User> {

    User login(User user);

    User register(User user);
}
