package com.dj.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dj.common.Constants;
import com.dj.common.enums.EmailCodeEnum;
import com.dj.controller.domain.UserRequest;
import com.dj.entity.User;
import com.dj.exception.ServiceException;
import com.dj.mapper.UserMapper;
import com.dj.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.utils.EmailUtils;
import lombok.extern.slf4j.Slf4j;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dj
 * @since 2023-02-21
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    // key是code value是当前的时间戳
    public static final Map<String,Long> CODE_MAP = new ConcurrentHashMap<>();
    public static final long TIME_IN_MS5 = 5*60*1000;  //表示5分钟的毫秒数

    @Autowired
    EmailUtils emailUtils;

    @Override
    public User login(UserRequest user) {
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
    public User register(UserRequest user) {
        try{
            //校验邮箱
            String emailCode = user.getEmailCode();
            Long timestamp = CODE_MAP.get(emailCode);
            if (timestamp == null){
                throw new ServiceException("验证码错误");
            }
            if (timestamp + TIME_IN_MS5 < System.currentTimeMillis()){ //说明验证码过期
                throw new ServiceException("验证码已过期");
            }

            User saveUser = new User();
            BeanUtils.copyProperties(user,saveUser); //把请求数据的属性copy给存储数据库的属性
            //存储用户
            return saveUser(saveUser);
        }catch (Exception e){
            throw new RuntimeException("数据库异常",e);
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

    @Override
    public void sendEmail(String email, String type) {
        String code = RandomUtil.randomNumbers(6);
        log.info("本次验证的code是：{}",code);
        String context = "<b>尊敬的用户：</b><br><br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好，" +
                "Partner交友网提醒您本次的验证码是：<b>{}</b>，" + "有效期5分钟。<br><br><br><b>Partner交友网</b>";
        String html = StrUtil.format(context, code);//把生成的验证码填入
        if ("REGISTER".equals(type)){ //注册
            //校验邮箱是否已注册
            User user = getOne(new QueryWrapper<User>().eq("email", email));
            if (user!=null){
                throw new ServiceException("该邮箱已注册");
            }
            ThreadUtil.execAsync(() -> {  // 多线程执行异步请求
                emailUtils.sendHtml("【Partner交友网】邮箱注册验证", html, email);
            });
            CODE_MAP.put(code,System.currentTimeMillis());
        }

    }

}
