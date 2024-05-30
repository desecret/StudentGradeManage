package org.example.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.example.back.DTO.CourseDTO;
import org.example.back.entity.Course;
import org.example.back.entity.TeachingClass;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
}
