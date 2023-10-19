package com.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanMapUtils {

    public static <T> Map<String, Object> beanToMap(T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        Map<String, Object> map = new HashMap<>();

        beanMap.forEach((key, value) -> map.put(String.valueOf(key), value));
        return map;
    }

    public static <T> T mapToBean(Map<String, ?> map, Class<T> clazz) {
        T bean = BeanUtils.instantiateClass(clazz);
//        BeanMap beanMap = BeanMap.create(bean);

        map.forEach((key, value) -> {
            if (value != null) {
                Field field;
                try {
                    field = clazz.getDeclaredField(key);
                    field.setAccessible(true);
                    Object convertedValue = convertValue(value, field.getType());
                    // 将空值转换成null
                    if ("".equals(value)) {
                        field.set(bean, null);
                    } else {
                        field.set(bean, convertedValue);
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });


        return bean;
    }

    public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
        List<Map<String, Object>> list = new ArrayList<>();
        if (objList != null && objList.size() > 0) {
            Map<String, Object> map;
            T bean;
            for (T t : objList) {
                bean = t;
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (maps != null && maps.size() > 0) {
            Map<String, ?> map;
            for (Map<String, Object> stringObjectMap : maps) {
                map = stringObjectMap;
                T bean = mapToBean(map, clazz);
                list.add(bean);
            }
        }
        return list;
    }

    private static Object convertValue(Object value, Class<?> parameterType) {
        String stringValue = value.toString().trim();
        if (stringValue.isEmpty()) {
            // 空字符串处理逻辑，可以根据需求返回null或者抛出异常
            return null;
        }
        if (parameterType.equals(Integer.class)) {
            return Integer.parseInt(value.toString());
        } else if (parameterType.equals(Long.class)) {
            return Long.parseLong(value.toString());
        } else if (parameterType.equals(Double.class)) {
            return Double.parseDouble(value.toString());
        } else if (parameterType.equals(Float.class)) {
            return Float.parseFloat(value.toString());
        } else {
            return value;
        }
    }
}




