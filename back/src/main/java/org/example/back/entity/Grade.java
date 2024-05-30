package org.example.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Getter
//@Setter

public class Grade implements BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer gradeId;
    private Integer regularScore;
    private Integer midScore;
    private Integer finalScore;
    private Integer totalScore;

    private Integer studentId;
//    private String studentName;

    private Integer courseId;   //对应的是course表的courseCode字段
//    private String courseName;
    private Integer classId;

}

