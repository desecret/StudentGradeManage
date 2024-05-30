package org.example.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.back.DTO.StudentScoreDTO;
import org.example.back.entity.TeachingClass;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClassMapper extends BaseMapper<TeachingClass> {
    //查找所有教学班
    Page<TeachingClass> selectAll(Page<TeachingClass> page);
}
