package com.dj.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dj.common.Constants;
import com.dj.entity.User;
import com.dj.exception.ServiceException;
import com.dj.mapper.UserMapper;
import com.dj.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Override
    public User register(User user) {
        try{
            //存储用户
            return saveUser(user);
        }catch (Exception e){
            throw new RuntimeException("数据库异常");
        }
    }

    private User saveUser(User user){
        User dbUser = getOne(new UpdateWrapper<User>().eq("username", user.getUsername()));
        if (dbUser!=null){
            throw new ServiceException("用户名已存在");
        }else {
            //设置昵称
            if (StrUtil.isBlank(user.getName())) {
                user.setName(Constants.USER_NAME_PREFIX + DateUtil.format(new Date(), Constants.DATAE_RULE_YYYYMMDD) + RandomUtil.randomString(4));
            }
            //设置默认密码
            if (StrUtil.isBlank(user.getPassword())) {
                user.setPassword("123");
            }
            //设置唯一表示
            user.setUid(IdUtil.fastSimpleUUID());
            boolean saveSuccess = save(user);
            if (!saveSuccess) {
                throw new ServiceException("注册失败");
            }
        }
        return user;
    }
}
