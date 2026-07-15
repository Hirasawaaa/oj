package org.example.oj.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.oj.entity.Problem;
import org.example.oj.entity.Submit;
import org.example.oj.mapper.SubmitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.File;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.regex.Matcher;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

@Service
public class SubmitService extends ServiceImpl<SubmitMapper, Submit> {
    @Autowired
    private ProblemService problemService;
    public void judge(Submit submit){
        String workDir=System.getProperty("user.dir")+"/judge/"+submit.getId();
        String filename,compileCmd,runCmd;
        switch(submit.getLanguage()){
            case "java":
                filename="Main.java";
                compileCmd="javac Main.java";
                runCmd="java Main";
                break;
            case "c":
                filename = "main.c";
                compileCmd = "gcc -O2 -o main main.c -lm";
                runCmd = "./main";
                break;
            case "cpp":
                filename = "main.cpp";
                compileCmd = "g++ -O2 -o main main.cpp -lm";
                runCmd = "./main";
                break;
            case "python":
                filename = "main.py";
                compileCmd = null;  // Python 不需要编译
                runCmd = "python3 main.py";
                break;
            case "pypy3":
                filename = "main.py";
                compileCmd = null;  // PyPy 也不需要编译
                runCmd = "pypy3 main.py";
                break;
            default:
                filename = "Main.java";
                compileCmd = "javac Main.java";
                runCmd = "java Main";
                break;
        }
        try {
            Files.createDirectories(Paths.get(workDir));
            Files.write(Paths.get(workDir, filename), submit.getCode().getBytes());
            if(compileCmd!=null) {
                Process compile = Runtime.getRuntime().exec(new String[]{
                        "docker", "run", "--rm", "--memory=512m",
                        "-v", workDir + ":/code",
                        "-w", "/code",
                        "oj-judge", "sh", "-c", "timeout 30 " + compileCmd
                });


                String error = new String(compile.getErrorStream().readAllBytes());
                compile.waitFor();
                if (compile.exitValue() != 0) {
                    //编译错误
//                String error = new String(compile.getErrorStream().readAllBytes());
                    submit.setStatus(3);
                    submit.setResult(error);
                    updateById(submit);
                    return;
                }
            }
            Problem problem= problemService.getById(submit.getProblemId());

         //   Process run=Runtime.getRuntime().exec("java Main");
        String memLimit = problem.getMemoryLimit()+"m";
        int timeoutSec=(problem.getTimeLimit()/1000)+3;
        Process run=Runtime.getRuntime().exec(new String[]{
                "docker","run","--rm","--memory="+memLimit,"-i",
                "-v",workDir+":/code",
                "-w","/code",
                "oj-judge","sh","-c",
                "/usr/bin/time -v timeout "+timeoutSec+" "+runCmd
        });

        if(problem.getSampleInput()!=null && !problem.getSampleInput().isEmpty()){
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(run.getOutputStream()));
                        writer.write(problem.getSampleInput());
                        writer.flush();
                        writer.close();
            }
            String output =new String(run.getInputStream().readAllBytes()).trim();

            //解析cpu时间
            String timeInfo =new String(run.getErrorStream().readAllBytes());

            run.waitFor();
            double cpuTime=0;
            double usedMemory=0;
            Matcher timeMatcher= Pattern.compile("User time \\(seconds\\): ([\\d.]+)").matcher(timeInfo);
            Matcher memMatcher=Pattern.compile("Maximum resident set size \\(kbytes\\): (\\d+)").matcher(timeInfo);

            if(timeMatcher.find())cpuTime=Double.parseDouble((timeMatcher.group(1)));

            if(memMatcher.find())usedMemory=Double.parseDouble((memMatcher.group(1)));

            if(cpuTime>problem.getTimeLimit()/1000.0){
                submit.setStatus(4);
                submit.setResult("Time limit exceeded");
                updateById(submit);
                return;
            }
            if(usedMemory>problem.getMemoryLimit()*1024L){
                submit.setStatus(5);
                submit.setResult("Memory limit exceeded");
                updateById(submit);
                return;
            }

            String expected = problem.getSampleOutput().trim();
            if(output.equals(expected)){
                submit.setStatus(1);
                submit.setResult("Accepted");
            }
            else{
                submit.setStatus(2);
                submit.setResult("Wrong Answer");
            }
            updateById(submit);
        }
        catch (Exception e){
            submit.setStatus(6);
            submit.setResult(e.getMessage());
            updateById(submit);
        }
        finally{
            try{
                Files.walk(Paths.get(workDir))
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
            catch(Exception ignored){

            }
        }
    }
}
