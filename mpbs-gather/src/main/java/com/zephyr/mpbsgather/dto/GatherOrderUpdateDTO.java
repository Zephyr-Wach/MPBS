package com.zephyr.mpbsgather.dto;

import lombok.Data;

import java.util.Map;

@Data
public class GatherOrderUpdateDTO {
    private String gatherId;
    private Map<Long, Integer> order;
}

