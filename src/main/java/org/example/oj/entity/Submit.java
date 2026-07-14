package org.example.oj.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("submit")
public class Submit {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long problemId;

    private Long userId;

    private String code;

    private String language;

    private Integer status;

    private String result;

    private LocalDateTime createdAt;
}
