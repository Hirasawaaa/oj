package org.example.oj.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.oj.entity.Problem;
import org.example.oj.entity.Submit;
import org.example.oj.mapper.SubmitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class SubmitService extends ServiceImpl<SubmitMapper, Submit> {
    @Autowired
    private ProblemService problemService;
    public void judge(Submit submit){

        try {
            Files.write(Paths.get("Main.java"), submit.getCode().getBytes());

            Process compile = Runtime.getRuntime().exec("javac Main.java");
            compile.waitFor();
            if (compile.exitValue() != 0) {
                //编译错误
                String error = new String(compile.getErrorStream().readAllBytes());
                submit.setStatus(3);
                submit.setResult(error);
                updateById(submit);
                return;
            }
            Problem problem= problemService.getById(submit.getProblemId());

            Process run=Runtime.getRuntime().exec("java Main");

            if(problem.getSampleInput()!=null && !problem.getSampleInput().isEmpty()){
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(run.getOutputStream()));
                        writer.write(problem.getSampleInput());
                        writer.flush();
                        writer.close();
            }
            run.waitFor();
            String output =new String(run.getInputStream().readAllBytes()).trim();


            String expected = problem.getSampleOutput().trim();
            if(output.equals(expected)){
                submit.setStatus(1);
                submit.setResult("通过");
            }
            else{
                submit.setStatus(2);
                submit.setResult("expected: "+expected+" Your answer: "+output);
            }
            updateById(submit);
        }
        catch (Exception e){
            submit.setStatus(6);
            submit.setResult(e.getMessage());
            updateById(submit);
        }
    }
}
