package org.example.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.back.entity.*;
import org.example.back.mapper.AdminMapper;
import org.example.back.mapper.StudentMapper;
import org.example.back.mapper.TeacherMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private AdminMapper adminMapper;

    @PostMapping("/student/login")
    public String studentLogin(@RequestBody Student student){
//        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("account",student.getAccount());
        Student stu = studentMapper.selectById(student.getAccount());
        if(stu == null){
            return "201";
        } else {
           if(stu.getPassword().equals(student.getPassword())){
               return stu.getName();
           } else {
               return "201";
           }
        }
    }

    @PostMapping("/teacher/login")
    public String teacherLogin(@RequestBody Teacher teacher){
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",teacher.getAccount());
        Teacher tea = teacherMapper.selectOne(queryWrapper);
        if(tea == null){
            return "201";
        } else {
            if(tea.getPassword().equals(teacher.getPassword())){
                return tea.getName();
            } else {
                return "201";
            }
        }
    }

    @PostMapping("/administration/login")
    public String teacherLogin(@RequestBody Admin admin1){
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",admin1.getAccount());
        Admin admin = adminMapper.selectOne(queryWrapper);
        if(admin == null){
            return "201";
        } else {
            if(admin.getPassword().equals(admin1.getPassword())){
                return admin.getName();
            } else {
                return "201";
            }
        }
    }
}
