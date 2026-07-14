package org.example.oj.entity;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("problem")
public class Problem {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String title;

    private String description;

    private String inputDesc;

    private String outputDesc;

    private String sampleInput;

    private String sampleOutput;

    private Integer difficulty;

    private Integer timeLimit;

    private Integer memoryLimit;

    private LocalDateTime createdAt;

}
