package org.example.oj.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.oj.common.BusinessException;
import org.example.oj.common.Result;
import org.example.oj.entity.Problem;
import org.example.oj.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;

@RestController
@RequestMapping("/problem")

public class ProblemController {
    @Autowired
    private ProblemService problemService;

    @PostMapping("/create")
    public Result<Void> createProblem(@RequestBody Problem problem){
        problemService.save(problem);
        return Result.success("创建成功",null);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteProblem(@PathVariable Long id) {
        if(!problemService.removeById(id)){
            throw new BusinessException(404,"题目不存在");
        }
        return Result.success("删除成功",null);

    }

    @PutMapping("/update")
    public Result<Void> updateProblem(@RequestBody Problem problem) {
        if (!problemService.updateById(problem)) {
            throw new BusinessException(404, "题目不存在");
        }
        return Result.success("修改成功", null);

    }

    @GetMapping("/list")
    public Result<IPage<Problem>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) Integer difficulty,
            @RequestParam(defaultValue = "id")String sortBy,
            @RequestParam(defaultValue = "asc")String sortOrder
            ){
        var query =problemService.lambdaQuery();

        //难度筛选
        if(difficulty !=null){
            query.eq(Problem::getDifficulty,difficulty);
        }
        //按照id排序
        if("id".equals(sortBy)) {
            if ("asc".equals(sortOrder)) {
                query.orderByAsc(Problem::getId);
            } else {
                query.orderByDesc(Problem::getId);
            }
        }
        else if("difficulty".equals(sortBy)){//按照难度排序
            if("asc".equals(sortOrder)) {
                query.orderByAsc(Problem::getDifficulty);
                query.orderByAsc(Problem::getId);//相同难度按照id升序
            }
            else{
                query.orderByDesc(Problem::getDifficulty);
                query.orderByAsc(Problem::getId);
            }
        }
        return Result.success(query.page(new Page<>(page,pageSize)));
    }
    @GetMapping("/{id}")
    public Result<Problem> getProblem(@PathVariable Long id){
        Problem problem=problemService.getById(id);
        if(problem==null){
            throw new BusinessException(404,"题目不存在");
        }
        return Result.success(problem);
    }


}
