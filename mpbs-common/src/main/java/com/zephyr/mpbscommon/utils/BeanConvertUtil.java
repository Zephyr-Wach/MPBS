package com.zephyr.mpbscommon.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class BeanConvertUtil {

    /**
     * 单个对象转换
     */
    public static <S, T> T convert(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("Bean conversion failed", e);
        }
    }

    /**
     * List转换
     */
    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass) {
        if (sourceList == null) {
            return null;
        }
        List<T> targetList = new ArrayList<>();
        for (S source : sourceList) {
            targetList.add(convert(source, targetClass));
        }
        return targetList;
    }

    /**
     * 属性复制（已有目标对象）
     */
    public static <S, T> void copyProperties(S source, T target) {
        if (source == null || target == null) {
            return;
        }
        BeanUtils.copyProperties(source, target);
    }
}
