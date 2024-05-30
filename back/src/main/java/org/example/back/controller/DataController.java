package org.example.back.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class DataController {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/java_course";
    static final String USER = "root";
    static final String PASS = "20020805jdy";

    @GetMapping("/data")
    public Integer randomData() {
        Connection conn = null;

        try {
            // 注册JDBC驱动
            Class.forName(JDBC_DRIVER);

            // 开启数据库连接
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected to the database.");

            // 清空表数据（如果需要）
            clearTables(conn);

            // 插入教师数据
            List<Integer> teacherIds = insertTeachers(conn, 6);
            // 插入课程数据
            List<Integer> courseIds = insertCourses(conn, 3);
            // 插入学生数据
            List<Integer> studentIds = insertStudents(conn, 100);
            // 插入教学班级数据，每个课程至少两个教师上课
            insertTeachingClasses(conn, teacherIds, courseIds);
            // 插入成绩数据
            insertGrades(conn, studentIds);

        } catch (Exception e) {
            e.printStackTrace();
            return 201;
        } finally {
            // 关闭数据库连接
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 200;
    }

    // 清空表数据
    private static void clearTables(Connection conn) throws Exception {
        String[] tables = { "course", "grade", "student", "teacher", "teachingclass"};
        for (String table : tables) {
            executeUpdate(conn, "TRUNCATE TABLE " + table);
        }
    }

    // 插入教师数据，并返回教师ID列表
    private static List<Integer> insertTeachers(Connection conn, int teacherCount) throws Exception {
        String sql = "INSERT INTO teacher (Name, Gender, password, account) VALUES (?, ?, ?, ?)";
        List<Integer> teacherIds = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Random random = new Random();
            for (int i = 0; i < teacherCount; i++) {
                String name = "Teacher" + (i + 1);
                String gender = random.nextBoolean() ? "Male" : "Female";
                stmt.setString(1, name);
                stmt.setString(2, gender);
                stmt.setString(3, String.valueOf(i + 1));
                stmt.setInt(4, i + 1);
                stmt.executeUpdate();

                // 从返回的键中获取教师ID
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        teacherIds.add(rs.getInt(1));
                    }
                }
            }
        }
        return teacherIds;
    }

    // 插入课程数据，并返回课程ID列表
    private static List<Integer> insertCourses(Connection conn, int courseCount) throws Exception {
        String sql = "INSERT INTO course (CourseName, CourseCredit, CoursePeriod, CourseCode) VALUES (?, ?, ?, ?)";
        List<Integer> courseIds = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Random random = new Random();
            for (int i = 0; i < courseCount; i++) {
                String courseName = "Course" + (i + 1);
                int credit = random.nextInt(10) + 1;
                String period = "2024-" + (i + 1);
                String code = "CS" + (i + 1);
                stmt.setString(1, courseName);
                stmt.setInt(2, credit);
                stmt.setString(3, period);
                stmt.setString(4, code);
                stmt.executeUpdate();

                // 从返回的键中获取课程ID
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        courseIds.add(rs.getInt(1));
                    }
                }
            }
        }
        return courseIds;
    }

    // 插入学生数据，并返回学生ID列表
    private static List<Integer> insertStudents(Connection conn, int studentCount) throws Exception {
        String sql = "INSERT INTO student (Name, Gender, BelongClassId, password, account) VALUES (?, ?, ?, ?, ?)";
        List<Integer> studentIds = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Random random = new Random();
            for (int i = 0; i < studentCount; i++) {
                String name = "Student" + (i + 1);
                String gender = random.nextBoolean() ? "Male" : "Female";
                int classId = (int)(Math.random() * 10) + 1; // 假设最多有10个班级
                stmt.setString(1, name);
                stmt.setString(2, gender);
                stmt.setInt(3, classId);
                stmt.setString(4, String.valueOf((i + 1)));
                stmt.setInt(5, i + 1);
                stmt.executeUpdate();

                // 从返回的键中获取学生ID
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        studentIds.add(rs.getInt(1));
                    }
                }
            }
        }
        return studentIds;
    }

    // 插入教学班级数据
    // 插入教学班级数据，每个课程至少两个教师上课
    private static void insertTeachingClasses(Connection conn, List<Integer> teacherIds, List<Integer> courseIds) throws Exception {
        String sql = "INSERT INTO teachingclass (TeacherID, Semester, ClassName, TotalStudents, CourseId, TeacherName, CourseName) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            Random random = new Random();
            int totalStudents = 20; // 每个班级至少20名学生
            for (Integer courseId : courseIds) {
                // 为每个课程分配至少两个教师
                for (int i = 0; i < 2; i++) {
                    int teacherId = teacherIds.get(i % teacherIds.size());
                    String teacherName = "Teacher" + teacherId;
                    String courseName = "Course" + courseId;
                    int classId = teacherId % 10 + 1; // 假设班级ID从1开始，最多10个班级
                    stmt.setInt(1, teacherId);
                    stmt.setString(2, "2024-2025");
                    stmt.setString(3, "Class" + classId);
                    stmt.setInt(4, totalStudents);
                    stmt.setInt(5, courseId);
                    stmt.setString(6, teacherName);
                    stmt.setString(7, courseName);
                    stmt.executeUpdate();
                }
            }
        }
    }

    // 插入成绩数据，每个学生至少选择3门课程
    private static void insertGrades(Connection conn, List<Integer> studentIds) throws Exception {
    String sql = "INSERT INTO grade (TotalScore, FinalScore, MidScore, RegularScore, CourseId, StudentId, classId) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        Random random = new Random();
        for (Integer studentId : studentIds) {
            // 为每个学生随机选择3门课程
            for (int i = 0; i < 3; i++) {
                int courseId = i + 1; // 假设课程ID从1开始
                int classId = (studentId + i) % 10 + 1; // 假设班级ID从1开始，最多10个班级
                int finalScore = random.nextInt(100) + 1;
                int midScore = random.nextInt(100) + 1;
                int regularScore = random.nextInt(100) + 1;
                int totalScore = (finalScore + midScore + regularScore) / 3;
                stmt.setInt(1, totalScore);
                stmt.setInt(2, finalScore);
                stmt.setInt(3, midScore);
                stmt.setInt(4, regularScore);
                stmt.setInt(5, courseId);
                stmt.setInt(6, studentId);
                stmt.setInt(7, classId);
                stmt.executeUpdate();
            }
        }
    }
}


    // 执行更新语句
    private static void executeUpdate(Connection conn, String sql) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}

