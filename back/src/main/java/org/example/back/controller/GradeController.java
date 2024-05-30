package org.example.back.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.back.DTO.ScoreDistributionDTO;
import org.example.back.DTO.StudentScoreDTO;
import org.example.back.entity.Course;
import org.example.back.entity.Grade;
import org.example.back.entity.Student;
import org.example.back.entity.TeachingClass;
import org.example.back.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class GradeController{
    @Autowired
    private  GradeMapper gradeMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private ClassMapper classMapper;



//    某课程分数段分布
    @GetMapping("grade/dis/course")
    public List<ScoreDistributionDTO> getScoreDistributionByCourseId(@RequestParam("courseCode") String courseCode){
        return gradeMapper.countTotalScoreDistributionByCourseCode(courseCode);
    }

//    总成绩分数段分布
    @GetMapping("grade/dis")
    public List<ScoreDistributionDTO> countTotalScoreDistribution(){
        return gradeMapper.countTotalScoreDistribution();
    }

//    按照学号查所有课程成绩
    @GetMapping("grade/id")
    public Page<StudentScoreDTO> findAllScoresByStudentId(@RequestParam("studentId") int studentId,
                                                          @RequestParam("courseName") String courseName,
                                                          @RequestParam("courseCode") String courseCode,
                                                          @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page<StudentScoreDTO> page = new Page<>(pageNum, pageSize);
        return gradeMapper.findAllScoresByStudentId(page, studentId,courseName,courseCode);
    }

//    按照学号查总成绩
    @GetMapping("grade/total/id")
    public List<StudentScoreDTO> findTotalScoresByStudentId(@RequestParam("id") int studentId){
        return gradeMapper.findTotalScoresByStudentId(studentId);
    }

//    按照姓名查所有课程成绩
    @GetMapping("grade/name")
    public List<StudentScoreDTO> findAllScoresByStudentName(@RequestParam("name") String studentName){
        return gradeMapper.findAllScoresByStudentName(studentName);
    }

//    按照姓名查总成绩
    @GetMapping("grade/total/name")
    public List<StudentScoreDTO> findTotalScoresByStudentName(@RequestParam("name") String studentName){
        return gradeMapper.findTotalScoresByStudentName(studentName);
    }


    //依据学号和课程id插入成绩
    @PostMapping("/grade/insert")
    public Integer insertGrade(@RequestBody StudentScoreDTO studentScoreDTO){
        String courseCode = studentScoreDTO.getCourseCode();
        String inputCourseName = studentScoreDTO.getCourseName();
        String className = studentScoreDTO.getClassName();
        List<TeachingClass> list = new ArrayList<>();
        Grade grade = new Grade();
        //当教师插入成绩的时候就不会有className参数输入，只有当admin插入的时候才会要求输入班级名
        if(!Objects.equals(className, "")){
            // 根据className获取classId和courseName
            Map<String, Object> map = new HashMap<>();
            map.put("ClassName", className);
            list = classMapper.selectByMap(map);
            //返回203代表无此班级
            if (list.isEmpty()) return 203;

            String classCourseName = list.get(0).getCourseName();

            // 检查输入的courseName和className对应的courseName是否一致
            if(!inputCourseName.equals(classCourseName)) return 205; //返回205代表输入的courseName和className对应的courseName不一致
        }

        // 检查课程名和课程编号是否对应
        Map<String,Object> mapCourse = new HashMap<>();
        mapCourse.put("CourseCode",courseCode);
        List<Course> listCourse = courseMapper.selectByMap(mapCourse);
        //返回204代表无此课程
        if(listCourse.isEmpty() || !listCourse.get(0).getCourseName().equals(inputCourseName)) return 204;

        Student student = studentMapper.selectById(studentScoreDTO.getStudentId());
        //返回202代表无此学生
        if(student == null) return 202;

        Integer courseId = listCourse.get(0).getCourseId();
        Integer classId;
        //当传来的teacherId等于-1的时候说明是admin账号在插入成绩，这时就直接用前面的list来找到classId
        if(studentScoreDTO.getTeacherId() != -1) {
            //根据CourseId和TeacherId找到classId写回grade表
            Map<String, Object> mapClass = new HashMap<>();
            mapClass.put("CourseId", courseId);
            mapClass.put("TeacherId", studentScoreDTO.getTeacherId());
            List<TeachingClass> list1 = classMapper.selectByMap(mapClass);
            if (list1.isEmpty()) return 203;
            classId = list1.get(0).getClassId();
        } else {
            classId = list.get(0).getClassId();
        }

        grade.setClassId(classId);
        grade.setCourseId(courseId);
        grade.setStudentId(studentScoreDTO.getStudentId());
        grade.setRegularScore(studentScoreDTO.getRegularScore());
        grade.setMidScore(studentScoreDTO.getMidScore());
        grade.setFinalScore(studentScoreDTO.getFinalScore());

        //计算综合成绩
        Integer TotalScore = (studentScoreDTO.getRegularScore() + studentScoreDTO.getMidScore() + studentScoreDTO.getFinalScore()) / 3;
        grade.setTotalScore(TotalScore);

        gradeMapper.insert(grade);
        return 200;
    }

    //依据gradeId删除成绩
    @PostMapping("/grade/delete")
    public Integer deleteGrade(@RequestBody Integer gradeId){
        //返回201代表无此成绩记录
        if(gradeMapper.selectById(gradeId) == null) return 201;

        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("GradeId", gradeId);
        gradeMapper.delete(queryWrapper);
        return 200;
    }

    //依据gradeId修改成绩
    @PostMapping("/grade/update")
    public Integer updateGrade(@RequestBody StudentScoreDTO studentScoreDTO){
        Integer gradeId = studentScoreDTO.getGradeId();
        Grade existingGrade = gradeMapper.selectById(gradeId);
        //返回201代表无此成绩记录
        if(existingGrade == null) return 201;

        String courseCode = studentScoreDTO.getCourseCode();
        String inputCourseName = studentScoreDTO.getCourseName();
        String className = studentScoreDTO.getClassName();
        List<TeachingClass> list = new ArrayList<>();
        //当教师插入成绩的时候就不会有className参数输入，只有当admin插入的时候才会要求输入班级名
        if(className != null){
            // 根据className获取classId和courseName
            Map<String, Object> map = new HashMap<>();
            map.put("ClassName", className);
            list = classMapper.selectByMap(map);
            //返回203代表无此班级
            if (list.isEmpty()) return 203;
        } else {
            // 如果className为空，从grade表中获取classId
            list.add(classMapper.selectById(existingGrade.getClassId()));
        }
        Integer classId = list.get(0).getClassId();
        String classCourseName = list.get(0).getCourseName();

        // 检查输入的courseName和className对应的courseName是否一致
        if(!inputCourseName.equals(classCourseName)) return 205; //返回205代表输入的courseName和className对应的courseName不一致

        // 检查课程名和课程编号是否对应
        Map<String,Object> mapCourse = new HashMap<>();
        mapCourse.put("CourseCode",courseCode);
        List<Course> listCourse = courseMapper.selectByMap(mapCourse);
        //返回204代表无此课程
        if(listCourse.isEmpty() || !listCourse.get(0).getCourseName().equals(inputCourseName)) return 204;

        //检查学生是否存在
        Integer studentId = studentScoreDTO.getStudentId();
        if(studentMapper.selectById(studentId) == null) return 202; //返回202代表无此学生

        Grade grade = new Grade();
        grade.setRegularScore(studentScoreDTO.getRegularScore());
        grade.setMidScore(studentScoreDTO.getMidScore());
        grade.setFinalScore(studentScoreDTO.getFinalScore());

        //计算综合成绩
        Integer TotalScore = (studentScoreDTO.getRegularScore() + studentScoreDTO.getMidScore() + studentScoreDTO.getFinalScore()) / 3;
        grade.setTotalScore(TotalScore);

        UpdateWrapper<Grade> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("GradeId", gradeId);
        gradeMapper.update(grade, updateWrapper);
        return 200;
    }

    //分页查询(根据teacherId展示某登录教师的课程的所有成绩，搜索时根据studentId或者studentName)
    @GetMapping("/grade/page")
    public Page<StudentScoreDTO> selectPage(@RequestParam(value = "teacherId") String teacherId,
                                            @RequestParam(value = "studentId") String studentId,
                                            @RequestParam(value = "studentName") String studentName,
                                            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<StudentScoreDTO> page = new Page<>(pageNum,pageSize);
        return gradeMapper.selectPageWithTeacherId(page, teacherId, studentId, studentName);
    }

    @GetMapping("grade/all")
    public Page<StudentScoreDTO> selectAll(@RequestParam(value = "studentId") String studentId,
                                           @RequestParam(value = "studentName") String studentName,
                                           @RequestParam(value = "courseName") String courseName,
                                           @RequestParam(value = "courseCode") String courseCode,
                                           @RequestParam(value = "className") String className,
                                           @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        Page<StudentScoreDTO> page = new Page<>(pageNum,pageSize);
        return gradeMapper.selectPageAll(page,studentId,studentName,courseName,courseCode,className);
    }
}
