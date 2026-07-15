package org.example.oj.controller;
import org.example.oj.common.BusinessException;
import org.example.oj.common.Result;
import org.example.oj.entity.User;
import org.example.oj.service.TokenService;
import org.example.oj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController  //这个类是接口层，返回的数据转为json
@RequestMapping("/user") // /user/..
public class UserController {
    @Autowired  //让springboot自动把userservice注入
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/register") //处理POST请求，路径为/user/register
    public Result<Long> register(@RequestParam String username,@RequestParam String password){
        //判断是否重名
        if(userService.getByUsername(username)!=null){
            throw new BusinessException("用户名已存在");
        }

        User user= new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole("user");
        userService.register(user);
//        return "注册成功，用户id: "+user.getId();
        return Result.success("注册成功",user.getId());
    }
    @GetMapping("/{id}")  //处理GET请求，路径/user/{id}
    public Result<User> getUser(@PathVariable Long id){//从url里取值，/user/{id}里的id
        User user=userService.getById(id);
        if(user==null)throw new BusinessException(404,"用户不存在");
        return Result.success(user);
    }
    @GetMapping("/list")
    public Result<List<User>> list(){
        List<User> list=userService.list();
        return Result.success(list);
    }
    @GetMapping("/getByUsername")
    public Result<User> getByUsername(@RequestParam String username){
        User user=userService.getByUsername(username);
        if(user==null)throw new BusinessException(404,"用户不存在");
        return Result.success(user);
    }
    @PostMapping("/login")
    public Result<String> login(@RequestParam String username, @RequestParam String password){
        User user=userService.getByUsername(username);
        if(user==null){
            throw new BusinessException("用户名不存在");
        }
        if(!passwordEncoder.matches(password,user.getPassword())){
            {
                throw new BusinessException("密码错误");
            }
        }
        String token=tokenService.createToken(user.getId());
        return Result.success("登录成功",token);
    }
}