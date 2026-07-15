package org.example.oj.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.oj.common.BusinessException;
import org.example.oj.common.Result;
import org.example.oj.entity.Submit;
import org.example.oj.mapper.SubmitMapper;
import org.example.oj.service.SubmitService;
import org.example.oj.service.TokenService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submit")
public class SubmitController {
    @Autowired
    private SubmitService submitService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/submit")
    public Result<Long> submit(@RequestBody Submit submit,@RequestHeader("Authorization")
            String token){
        Long userId=tokenService.getUserId(token);
        submit.setUserId(userId);
        submit.setStatus(0);
        submitService.save(submit);
//        submitService.judge(submit);
        rabbitTemplate.convertAndSend("judge.queue",submit.getId());
        return Result.success("提交成功",submit.getId());
    }
    @GetMapping("/{id}")
    public Result<Submit> getSubmit(@PathVariable Long id){
        Submit submit=submitService.getById(id);
        if(submit==null)throw new BusinessException(404,"提交不存在");
        return Result.success(submit);
    }
    @GetMapping("/list")
    public Result<IPage<Submit>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) Long problemId) {
        var query = submitService.lambdaQuery();
        if (problemId != null) query.eq(Submit::getProblemId, problemId);
        query.orderByDesc(Submit::getId);
        return Result.success(query.page(new Page<>(page, pageSize)));
    }
}
