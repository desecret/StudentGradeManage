package org.example.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.back.entity.Student;
import org.example.back.entity.TeachingClass;
import org.example.back.mapper.ClassMapper;
import org.example.back.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import static org.example.back.utils.QueryWrapperUtil.addLikeCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class StudentController{

    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private ClassMapper teachingClassMapper;

    @PostMapping("/student/insert")
    public int insertStudent(@RequestBody Student student) {
        // 检查account字段是否已经存在
//        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("account", student.getAccount());
//        int count = studentMapper.selectCount(queryWrapper);
//        if (count > 0) {
//            // account字段已经存在
//            return 202;
//        }

        Student stu = new Student();
//        stu.setAccount(student.getAccount());
        stu.setName(student.getName());
        stu.setGender(student.getGender());
        stu.setBelongClassId((student.getBelongClassId()));
        int insert = studentMapper.insert(stu);

        // 根据belongClassId查找对应的teachingClass并更新TotalStudents
        TeachingClass teachingClass = teachingClassMapper.selectById(student.getBelongClassId());
        if (teachingClass != null) {
            int newTotalStudents = teachingClass.getTotalStudents() + 1;
            teachingClass.setTotalStudents(newTotalStudents);
            teachingClass.setClassId(student.getBelongClassId());
            teachingClassMapper.updateById(teachingClass);
        } else {
            // 处理找不到teachingClass的情况，可能是数据不一致或其他错误
            return 201;
        }
        return 200;
    }


    @PostMapping("/student/delete")
    public int delete(@RequestBody Integer id){
        Map<String,Object> map = new HashMap<>();
        map.put("StudentId",id);

        Student stu = studentMapper.selectById(id);
        // 根据belongClassId查找对应的teachingClass并更新TotalStudents
        TeachingClass teachingClass = teachingClassMapper.selectById(stu.getBelongClassId());
        if (teachingClass != null) {
            int newTotalStudents = teachingClass.getTotalStudents() - 1;
            teachingClass.setTotalStudents(newTotalStudents);
            teachingClass.setClassId(stu.getBelongClassId());
            teachingClassMapper.updateById(teachingClass);
        } else {
            // 处理找不到teachingClass的情况，可能是数据不一致或其他错误
            return 201;
        }
        int delete = studentMapper.deleteByMap(map);
        if(delete >= 0) return 200;
        return 202;
    }


    //更新
    @PostMapping("/student/update")
    public int update(@RequestBody Student student) {
        int oldClassId = studentMapper.selectById(student.getStudentId()).getBelongClassId();
        //先查询所属班级在不在数据库中
        int classId = student.getBelongClassId();
        TeachingClass teachingClass = teachingClassMapper.selectById(classId);
        if(teachingClass == null) return 201;
        UpdateWrapper<Student> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("StudentId",student.getStudentId());
        int update = studentMapper.update(student,updateWrapper);
        //更新成功后根据最开始查到的该学生所属班级判断他所属班级有没有被改变，要是被改变了还需要改变班级表的人数
        if(classId != oldClassId){
            //原班级人数减一
            TeachingClass teachingClass1 = teachingClassMapper.selectById(oldClassId);
            int newTotalStudents = teachingClass1.getTotalStudents() - 1;
            teachingClass1.setTotalStudents(newTotalStudents);
            teachingClass1.setClassId(oldClassId);
            teachingClassMapper.updateById(teachingClass1);
            //现班级人数加一
            TeachingClass teachingClass2 = teachingClassMapper.selectById(classId);
            int nowTotal = teachingClass2.getTotalStudents() + 1;
            teachingClass2.setTotalStudents(nowTotal);
            teachingClass2.setClassId(classId);
            teachingClassMapper.updateById(teachingClass2);
        }
        if(update >= 0) return 200;
        else return 202;
    }


    @GetMapping("/student/id")
    public Student selectStudentById(@RequestParam("id") int StudentId) {
        return studentMapper.selectById(StudentId);
    }

    @GetMapping("/student/name")
    public List selectStudentByName(@RequestParam("name") String name) {
        Map<String,Object> condition = new HashMap<>();
        condition.put("name",name);
        return studentMapper.selectByMap(condition);
    }

    @GetMapping("/student/gender")
    public List selectStudentByGender(@RequestParam("gender") String gender) {
        Map<String,Object> condition = new HashMap<>();
        condition.put("gender",gender);
        return studentMapper.selectByMap(condition);
    }

    //分页查询
    @GetMapping("/student/page")
    public Page<Student> select(Student student,
                                @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page<Student> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Student> studentQueryWrapper = new QueryWrapper<>();

        addLikeCondition(studentQueryWrapper, "Name",student.getName());
        addLikeCondition(studentQueryWrapper,"StudentId",student.getStudentId());

        return studentMapper.selectPage(page,studentQueryWrapper);
    }
}
