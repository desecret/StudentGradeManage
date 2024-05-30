package org.example.back.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public class QueryWrapperUtil {
    public static <T> void addLikeCondition(QueryWrapper<T> queryWrapper, String fieldName, Object fieldValue) {
        if (fieldValue != null) {
            queryWrapper.like(fieldName, fieldValue);
        }
    }
}