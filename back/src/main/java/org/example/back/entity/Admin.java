package org.example.back.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin implements BaseEntity{
    @TableId(type = IdType.AUTO)
    private Integer adminId;
    private String account;
    private String password;
    private String name;
}
