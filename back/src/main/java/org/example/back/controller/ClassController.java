package org.example.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.catalina.filters.AddDefaultCharsetFilter;
import org.example.back.entity.Admin;
import org.example.back.entity.Course;
import org.example.back.entity.Teacher;
import org.example.back.entity.TeachingClass;
import org.example.back.mapper.ClassMapper;
import org.example.back.mapper.CourseMapper;
import org.example.back.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static org.example.back.utils.QueryWrapperUtil.addLikeCondition;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ClassController {
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private CourseMapper courseMapper;

    //插入
    @PostMapping("/class/insert")
    public Integer insert(@RequestBody TeachingClass teachingClass){
        //前端给出班级名称，教师名称，持续学期，总学生数，授课课程名称
        //这里先根据教师名拿到教师id
        String teacherName = teachingClass.getTeacherName();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("name",teacherName);
        List<Teacher> list = teacherMapper.selectByMap(map1);
        //返回201代表无这个老师
        if(list.isEmpty()) return 201;
        teachingClass.setTeacherId(list.get(0).getTeacherId());

        //再根据授课课程名称拿到课程id
        String courseName = teachingClass.getCourseName();
        Map<String,Object> map = new HashMap<>();
        map.put("CourseName",courseName);
        List<Course> list1 = courseMapper.selectByMap(map);
        //返回201代表无此课程
        if(list1.isEmpty()) return 202;
        teachingClass.setCourseId(list1.get(0).getCourseId());

        //检查班级名称是否已存在
        String className = teachingClass.getClassName();
        Map<String,Object> map2 = new HashMap<>();
        map2.put("ClassName",className);
        List<TeachingClass> list2 = classMapper.selectByMap(map2);
        //返回204代表班级名称已存在
        if(!list2.isEmpty()) return 204;

        int ins = classMapper.insert(teachingClass);
        if(ins < 1) return 203;
            //mybatis-plus在插入后会自动将数据库生成的ID回填到实体对象中
        else return 200;
    }

    //删除
    @PostMapping("/class/delete")
    public Integer delete(@RequestBody String id){
        //前端传对应教学班id
        TeachingClass teachingClass1 = classMapper.selectById(id);
        //返回201代表没有此班级
        if(teachingClass1 == null) return 201;

        QueryWrapper<TeachingClass> courseQueryWrapper = new QueryWrapper<>();
        courseQueryWrapper.eq("ClassId",id);
        classMapper.delete(courseQueryWrapper);
        return 200;
    }

    //更新
    @PostMapping("/class/update")
    public Integer update(@RequestBody TeachingClass teachingClass){
        //前端传id
        Integer id = teachingClass.getClassId();
        TeachingClass teachingClass1 = classMapper.selectById(id);
        //返回202代表没有此班级
        if(teachingClass1 == null) return 202;

        //检查班级名称是否已存在
        String className = teachingClass.getClassName();
        Map<String,Object> map2 = new HashMap<>();
        map2.put("ClassName",className);
        List<TeachingClass> list2 = classMapper.selectByMap(map2);
        //返回204代表班级名称已存在，且不是当前正在更新的班级
        if(!list2.isEmpty() && !list2.get(0).getClassId().equals(id)) return 204;

        TeachingClass teachingClass2 = new TeachingClass();
        if(teachingClass.getClassName() != null) teachingClass2.setClassName(teachingClass.getClassName());
        //更改教师姓名的同时还要更改id
        if(teachingClass.getTeacherName() != null) {
            teachingClass2.setTeacherName(teachingClass.getTeacherName());
            String teacherName = teachingClass.getTeacherName();
            Map<String,Object> map1 = new HashMap<>();
            map1.put("name",teacherName);
            List<Teacher> list = teacherMapper.selectByMap(map1);
            //返回201代表无这个老师
            if(list.isEmpty()) return 201;
            teachingClass2.setTeacherId(list.get(0).getTeacherId());
        }
        if(teachingClass.getSemester() != null) teachingClass2.setSemester(teachingClass.getSemester());
        if(teachingClass.getTotalStudents() != null) teachingClass2.setTotalStudents(teachingClass.getTotalStudents());
        //更改课程名的同时还要更改id
        if(teachingClass.getCourseName() != null) {
            teachingClass2.setCourseName(teachingClass.getCourseName());
            String courseName = teachingClass.getCourseName();
            Map<String,Object> map = new HashMap<>();
            map.put("CourseName",courseName);
            List<Course> list1 = courseMapper.selectByMap(map);
            //没有此课程返回203
            if(list1.isEmpty()) return 203;
            teachingClass2.setCourseId(list1.get(0).getCourseId());
        }

        UpdateWrapper<TeachingClass> teachingClassUpdateWrapper = new UpdateWrapper<>();
        teachingClassUpdateWrapper.eq("ClassId",id);
        classMapper.update(teachingClass2,teachingClassUpdateWrapper);
        return 200;
    }

    @GetMapping("/class/all")
    public List<TeachingClass> selectAll(){
        return classMapper.selectList(null);
    }


    //分页查询
    @GetMapping("/class/page")
    public Page<TeachingClass> select(TeachingClass teachingClass,
                                      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
        Page<TeachingClass> page = new Page<>(pageNum,pageSize);
        QueryWrapper<TeachingClass> queryWrapper = new QueryWrapper<>();

        addLikeCondition(queryWrapper,"ClassName",teachingClass.getClassName());
        addLikeCondition(queryWrapper,"Semester",teachingClass.getSemester());
        addLikeCondition(queryWrapper,"CourseName",teachingClass.getCourseName());
        addLikeCondition(queryWrapper,"TeacherName",teachingClass.getTeacherName());
        addLikeCondition(queryWrapper,"totalStudents",teachingClass.getTotalStudents());

        return classMapper.selectPage(page,queryWrapper);
    }
//    public Page<TeachingClass> select(
//                                      @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                                      @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
//        Page<TeachingClass> page = new Page<>(pageNum,pageSize);
//        return classMapper.selectAll(page);
//    }
}
