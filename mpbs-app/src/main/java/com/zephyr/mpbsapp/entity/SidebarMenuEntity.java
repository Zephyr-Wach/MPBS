package com.zephyr.mpbsapp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sidebar_menu")
public class SidebarMenuEntity {

    @Id
    private Long id;

    @Column(name = "parent_id")
    private Long parentId;

    private String name;

    private String path;

    private String icon;

    @Column(name = "permission_key")
    private String permissionKey;

    @Column(name = "order_num")
    private Integer orderNum;

    @Column(name = "is_visible")
    private String isVisible;

}