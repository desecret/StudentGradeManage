package org.example.back.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.back.DTO.ScoreDistributionDTO;
import org.example.back.DTO.StudentScoreDTO;
import org.example.back.entity.Grade;
import org.apache.ibatis.annotations.Mapper;
import org.example.back.entity.Student;


import java.util.List;

@Mapper
public interface GradeMapper extends BaseMapper<Grade> {

    List<StudentScoreDTO> selectStudentScoresByClassIdByScore(@Param("classId") int classId);

    //按照学号排列
    List<StudentScoreDTO> selectStudentScoresByClassIdById(@Param("classId") int classId);

    List<ScoreDistributionDTO> countTotalScoreDistributionByCourseCode(@Param("courseCode") String courseCode);

    List<ScoreDistributionDTO> countTotalScoreDistribution();

    Page<StudentScoreDTO> findAllScoresByStudentId(Page<StudentScoreDTO> page, @Param("studentId") int studentId,
                                                   @Param("courseName") String courseName, @Param("courseCode") String courseCode);

    List<StudentScoreDTO> findTotalScoresByStudentId(@Param("studentId") int studentId);

    List<StudentScoreDTO> findAllScoresByStudentName(@Param("studentName") String studentName);

    List<StudentScoreDTO> findTotalScoresByStudentName(@Param("studentName") String studentName);

    List<Student> sortByStudentId();

    List<StudentScoreDTO> sortByCourse(@Param("courseId") int courseId);

    List<StudentScoreDTO> sortByTotalScore();

    //分页查询
    Page<StudentScoreDTO> selectPageWithTeacherId(Page<StudentScoreDTO> page, @Param("teacherId") String teacherId,
                                                  @Param("studentId") String studentId, @Param("studentName") String studentName);

    Page<StudentScoreDTO> selectPageAll(Page<StudentScoreDTO> page,@Param("studentId") String studentId,
                                        @Param("studentName") String studentName,
                                        @Param("courseName") String courseName,
                                        @Param("courseCode") String courseCode,
                                        @Param("className") String className);


}