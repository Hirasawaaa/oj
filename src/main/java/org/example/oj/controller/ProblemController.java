package org.example.oj.controller;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.oj.entity.Problem;
import org.example.oj.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;

@RestController
@RequestMapping("/problem")

public class ProblemController {
    @Autowired
    private ProblemService problemService;

    @PostMapping("/create")
    public String createProblem(@RequestBody Problem problem){
        problemService.save(problem);
        return "创建成功";
    }

    @DeleteMapping("/{id}")
    public String deleteProblem(@PathVariable Long id) {
        problemService.removeById(id);
        return "删除成功";
    }

    @PutMapping("/update")
    public String updateProblem(@RequestBody Problem problem) {
        problemService.updateById(problem);
        return "修改成功";
    }

    @GetMapping("/list")
    public IPage<Problem> list(
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
        return query.page(new Page<>(page,pageSize));
    }
    @GetMapping("/{id}")
    public Problem getProblem(@PathVariable Long id){
        return problemService.getById(id);
    }


}
