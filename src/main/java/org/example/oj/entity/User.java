package org.example.oj.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data  //Lombok的注解
@TableName("user") //这个类对应数据库中的user表

public class User {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private LocalDateTime createdAt;
}
