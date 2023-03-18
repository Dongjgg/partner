package com.dj.service;

import com.dj.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dj
 * @since 2023-01-16
 */
public interface IPermissionService extends IService<Permission> {

    List<Permission> tree();
}
