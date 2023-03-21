package com.dj.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.entity.Role;
import com.dj.entity.RolePermission;
import com.dj.exception.ServiceException;
import com.dj.mapper.RoleMapper;
import com.dj.mapper.RolePermissionMapper;
import com.dj.service.IRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Dj
 * @since 2023-01-16
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Transactional
    @Override
    public void savePermissions(Integer roleId, List<Integer> permissionIds) {
        if (CollUtil.isEmpty(permissionIds) || roleId == null) {
            throw new ServiceException("数据不能为空");
        }
        rolePermissionMapper.delete(new UpdateWrapper<RolePermission>()
                .eq("role_id", roleId));
        permissionIds.forEach(v -> {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(v);
            rolePermissionMapper.insert(rolePermission);
        });
    }
}
