package com.zephyr.mpbscommon.utils;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean转换工具类，支持对象和集合之间的属性复制转换。
 */
public class BeanConvertUtil {

    /**
     * 单个对象转换，将source对象属性复制到targetClass类型的新对象中。
     *
     * @param source      源对象
     * @param targetClass 目标对象的Class类型
     * @param <S>         源对象类型
     * @param <T>         目标对象类型
     * @return 目标对象实例，属性已复制；如果source为null则返回null
     * @throws RuntimeException 转换失败时抛出
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
     * List集合转换，将sourceList中每个元素转换为targetClass类型，返回转换后的新List。
     *
     * @param sourceList  源对象集合
     * @param targetClass 目标对象的Class类型
     * @param <S>         源对象类型
     * @param <T>         目标对象类型
     * @return 转换后的目标对象集合；如果sourceList为null则返回null
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
     * 复制属性到已有目标对象，source非空时将source属性复制到target。
     *
     * @param source 源对象
     * @param target 目标对象，不能为null
     * @param <S>    源对象类型
     * @param <T>    目标对象类型
     */
    public static <S, T> void copyProperties(S source, T target) {
        if (source == null || target == null) {
            return;
        }
        BeanUtils.copyProperties(source, target);
    }
}
