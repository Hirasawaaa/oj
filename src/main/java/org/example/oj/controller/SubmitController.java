package org.example.oj.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.oj.entity.Submit;
import org.example.oj.mapper.SubmitMapper;
import org.example.oj.service.SubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/submit")
public class SubmitController {
    @Autowired
    private SubmitService submitService;

    @PostMapping("/submit")
    public String submit(@RequestBody Submit submit){
        submit.setStatus(0);
        submitService.save(submit);
        submitService.judge(submit);
        return "提交成功";
    }
    @GetMapping("{id}")
    public Submit getSubmit(@PathVariable Long id){
        return submitService.getById(id);
    }
    @GetMapping("/list")
    public IPage<Submit> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) Long problemId) {
        var query = submitService.lambdaQuery();
        if (problemId != null) query.eq(Submit::getProblemId, problemId);
        query.orderByDesc(Submit::getId);
        return query.page(new Page<>(page, pageSize));
    }
}
