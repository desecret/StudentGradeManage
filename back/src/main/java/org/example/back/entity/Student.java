package org.example.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class Student implements BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer studentId;
    private Integer account;
    private String name;
    private String gender;
    private Integer belongClassId;
    private String password;

}
