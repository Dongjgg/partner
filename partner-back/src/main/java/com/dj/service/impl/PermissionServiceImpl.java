package com.dj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dj.entity.Permission;
import com.dj.mapper.PermissionMapper;
import com.dj.service.IPermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author dj
 * @since 2023-01-16
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {

    @Override
    public List<Permission> tree() {
        List<Permission> allData = list();  //获取全部数据

        return childrenTree(null, allData); // 从第一级开始往下递归获取树
    }

    // 递归生成树
    private List<Permission> childrenTree(Integer pid, List<Permission> allData) {
        List<Permission> list = new ArrayList<>();
        for (Permission permission : allData) {
            if (Objects.equals(permission.getPid(), pid)) {  // null, 一级
                list.add(permission);
                List<Permission> childrenTree = childrenTree(permission.getId(), allData);  // 递归调用， 摘取二级节点、三级、四级...
                permission.setChildren(childrenTree);
            }
        }
        return list;
    }
}
