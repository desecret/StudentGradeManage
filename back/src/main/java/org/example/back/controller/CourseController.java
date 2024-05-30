package org.example.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.back.DTO.CourseDTO;
import org.example.back.entity.Course;
import org.example.back.entity.Teacher;
import org.example.back.mapper.CourseMapper;
import org.example.back.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.example.back.utils.QueryWrapperUtil.addLikeCondition;

@RestController
public class CourseController {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    @PostMapping("/course/insert")
    public Integer insert(@RequestBody Course course){
        //根据老师名字先找到老师id
//        String teacherName = course.getTeacherName();
//        Map<String,Object> map1 = new HashMap<>();
//        map1.put("name",teacherName);
//        List<Teacher> list = teacherMapper.selectByMap(map1);
//        //返回空代表无这个老师
//        if(list.isEmpty()) return 202;

//        course.setTeacherId(list.get(0).getTeacherId());
        int ins = courseMapper.insert(course);
        if(ins < 1) return 201;
        //mybatis-plus在插入后会自动将数据库生成的ID回填到实体对象中
        else return 200;
    }

    @PostMapping("/course/delete")
    public Integer delete(@RequestBody String id){
        Course course1 = courseMapper.selectById(id);
        //返回-1代表没有此课程
        if(course1 == null) return 201;

        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("CourseId",id);
        return courseMapper.delete(courseQueryWrapper);
    }

    @PostMapping("/course/update")
    public Integer update(@RequestBody Course course){
        Integer id = course.getCourseId();
        Course course1 = courseMapper.selectById(id);
        //返回201代表没有此课程
        if(course1 == null) return 201;

        //根据老师名字先找到老师id
//        String teacherName = course.getTeacherName();
//        Map<String,Object> map1 = new HashMap<>();
//        map1.put("name",teacherName);
//        List<Teacher> list = teacherMapper.selectByMap(map1);
//        //返回202代表无这个老师
//        if(list.isEmpty()) return 202;

        UpdateWrapper<Course> courseUpdateWrapper = new UpdateWrapper<>();
        courseUpdateWrapper.eq("CourseId",id);
        courseMapper.update(course,courseUpdateWrapper);
        return 200;
    }

    //查所有
    @GetMapping("/course/all")
    public List<Course> selectAll(){
        return courseMapper.selectList(null);
    }

    @GetMapping("/course/page")
    public Page<Course> select(Course course,
                               @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page<Course> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();

        addLikeCondition(queryWrapper, "CourseName", course.getCourseName());
        addLikeCondition(queryWrapper, "CourseCode", course.getCourseCode());
        addLikeCondition(queryWrapper,"CoursePeriod",course.getCoursePeriod());
//        addLikeCondition(queryWrapper,"TeacherName",course.getTeacherName());

        return courseMapper.selectPage(page, queryWrapper);
    }
//        public Page<CourseDTO> select(
//                                      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                                      @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
//        Page<CourseDTO> page = new Page<>(pageNum,pageSize);
//        return courseMapper.selectAll(page);
//    }
}
