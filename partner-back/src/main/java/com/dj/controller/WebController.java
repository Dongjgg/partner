package com.dj.controller;

import com.dj.common.Result;
import com.dj.controller.domain.LoginDTO;
import com.dj.controller.domain.UserRequest;
import com.dj.entity.User;
import com.dj.service.IUserService;
import com.dj.service.impl.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "无权限接口列表")
@RestController
@Slf4j
public class WebController {

    @Resource
    IUserService userService;

    @GetMapping(value = "/")
    @ApiOperation(value = "版本校验接口")
    public String version() {
        String ver = "partner-back-0.0.1-SNAPSHOT";  // 应用版本号
        Package aPackage = WebController.class.getPackage();
        String title = aPackage.getImplementationTitle();
        String version = aPackage.getImplementationVersion();
        if (title != null && version != null) {
            ver = String.join("-", title, version);
        }
        return ver;
    }

    @ApiOperation(value = "用户登录接口")
    @PostMapping("/login")
    public Result login(@RequestBody UserRequest user) {
        long startTime = System.currentTimeMillis();
        LoginDTO res = userService.login(user);
        log.info("登录花费时间 {}ms", System.currentTimeMillis() - startTime);
        return Result.success(res);
    }


    @ApiOperation(value = "用户退出登录")
    @GetMapping("/logout/{uid}")
    public Result logout(@PathVariable String uid) {
        userService.logout(uid);
        return Result.success("退出登录成功");
    }

//    @ApiOperation(value = "用户退出登录接口")
//    @GetMapping("/logout/{uid}")
//    public Result logout(@PathVariable String uid) {
//        userService.logout(uid);
//        return Result.success();
//    }

    @ApiOperation(value = "用户注册接口")
    @PostMapping("/register")
    public Result register(@RequestBody UserRequest user) {
        userService.register(user);
        return Result.success();
    }

    @ApiOperation(value = "邮箱验证接口")
    @GetMapping("/email")
    public Result sendEmail(@RequestParam String email, @RequestParam String type) {  //  ?email=xxx&type=xxx
        long start = System.currentTimeMillis();
        userService.sendEmail(email, type);
        log.info("发送邮件花费时间：{}", System.currentTimeMillis() - start);
        return Result.success();
    }

    @ApiOperation(value = "密码重置接口")
    @PostMapping("/password/reset")
    public Result passwordReset(@RequestBody UserRequest userRequest) {
        String newPass = userService.passwordReset(userRequest);
        return Result.success(newPass);
    }

}
