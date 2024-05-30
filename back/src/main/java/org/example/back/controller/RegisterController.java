package org.example.back.controller;
import org.example.back.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.example.back.entity.*;

@RestController
public class RegisterController {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private AdminMapper adminMapper;

    @PostMapping("/student/register")
    public String register(@RequestBody Student student){
        Student stu = new Student();
        stu.setAccount(student.getAccount());
        stu.setPassword(student.getPassword());
        int insert = studentMapper.insert(stu);
        if(insert != 0) return student.getName();
        else return "201";
    }

    @PostMapping("/teacher/register")
    public String register(@RequestBody Teacher teacher){
        Teacher teacher1 = new Teacher();
        teacher1.setAccount(teacher.getAccount());
        teacher1.setPassword(teacher.getPassword());
        int insert = teacherMapper.insert(teacher1);
        if(insert != 0) return teacher.getName();
        else return "201";
    }

    @PostMapping("/administration/register")
    public String register(@RequestBody Admin admin){
        Admin admin1 = new Admin();
        admin1.setAccount(admin.getAccount());
        admin1.setPassword(admin.getPassword());
        int insert = adminMapper.insert(admin1);
        if(insert != 0) return admin.getName();
        else return "201";
    }
}
