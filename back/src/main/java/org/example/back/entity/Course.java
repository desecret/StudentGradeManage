package org.example.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Course implements BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer courseId;
    private String courseCode;  //课程编号
    private String courseName;
    private Integer courseCredit;
    private String coursePeriod;
}
