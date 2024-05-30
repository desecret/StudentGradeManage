package org.example.back.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentScoreDTO {
    private Integer gradeId;
    private String courseCode;  //课程编号
    private String teacherName; //教师姓名
    private String classId;     //所属教学班号
    private String className;
    private Integer teacherId;


    private Integer studentId;
    private String name;
    private String gender;
    private Integer courseId;
    private String courseName;
    private Integer regularScore;
    private Integer midScore;
    private Integer finalScore;
    private Integer totalScore;
    //所有科目加起来的总成绩
    private Integer TotalAllCoursesScore;
}

