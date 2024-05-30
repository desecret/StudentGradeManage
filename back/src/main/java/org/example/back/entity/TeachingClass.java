package org.example.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@TableName("teachingclass")
public class TeachingClass implements BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer classId;
    private Integer teacherId;
    private Integer courseId;
    private String className;
    private String teacherName;
    private String courseName;
    private String semester;
    private Integer totalStudents;

    @TableField(exist = false)
    private List<Grade> grade;
}
