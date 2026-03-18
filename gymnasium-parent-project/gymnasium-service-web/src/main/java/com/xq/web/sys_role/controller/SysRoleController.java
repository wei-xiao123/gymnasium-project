package com.xq.web.sys_role.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xq.utils.ResultUtils;
import com.xq.utils.ResultVo;
import com.xq.web.sys_role.entity.RoleParam;
import com.xq.web.sys_role.entity.SysRole;
import com.xq.web.sys_role.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@RestController
@RequestMapping("/api/role")
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;
    //新增角色
    @PostMapping
    public ResultVo addRole(@RequestBody SysRole role){
        role.setCreateTime(new Date());
        boolean save = sysRoleService.save(role);
        if(save){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }
    //编辑角色
    @PutMapping
    public ResultVo editRole(@RequestBody SysRole role){
        role.setUpdateTime(new Date());
        boolean update = sysRoleService.updateById(role);
        if(update){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }
    //删除角色
    @DeleteMapping("/{roleId}")
    public ResultVo deleteRole(@PathVariable("roleId") Long roleId){
        boolean delete = sysRoleService.removeById(roleId);
        if(delete){
            return ResultUtils.success("删除成功!");
        }
        return ResultUtils.error("删除失败!");
    }
    //角色列表分页查询
    @GetMapping("/list")
    public ResultVo getList(RoleParam param){
        IPage<SysRole> list = sysRoleService.list(param);
        return ResultUtils.success("查询成功",list);
    }
}
