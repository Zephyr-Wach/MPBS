package com.zephyr.mpbsapp.mapper;

import com.zephyr.mpbsapp.entity.SidebarMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SidebarMenuMapper {

    List<SidebarMenuEntity> selectMenusByRole(@Param("role") String role);
}