package com.zephyr.mpbsapp.mapper;

import com.zephyr.mpbsapp.entity.SidebarMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SidebarMenuMapper {

    /**
     * 根据角色查询侧边栏菜单列表
     *
     * @param role 用户角色标识
     * @return 该角色对应的菜单列表
     */
    List<SidebarMenuEntity> selectMenusByRole(@Param("role") String role);
}
