package org.example.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Teacher implements BaseEntity{
    @TableId(type = IdType.INPUT)
    private Integer teacherId;
    private Integer account;
    private String name;
    private String gender;
    private String password;

}
