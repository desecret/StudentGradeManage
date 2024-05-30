package org.example.back.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDTO {

    private Integer courseId;
    private String courseCode;  //课程编号
    private String courseName;
    private Integer courseCredit;
    private String coursePeriod;
    private Integer teacherId;
    private String teacherName;
}
