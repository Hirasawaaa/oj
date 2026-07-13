package org.example.oj.controller;
import org.example.oj.entity.User;
import org.example.oj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController  //这个类是接口层，返回的数据转为json
@RequestMapping("/user") // /user/..
public class UserController {
    @Autowired  //让springboot自动把userservice注入
    private UserService userService;
    @PostMapping("/register") //处理POST请求，路径为/user/register
    public String register(@RequestParam String username,@RequestParam String password){
        User user= new User();
        user.setUsername(username);
        user.setPassword(password);
        userService.save(user);
        return "注册成功，用户id: "+user.getId();
    }
    @GetMapping("/{id}")  //处理GET请求，路径/user/{id}
    public User getUser(@PathVariable Long id){//从url里取值，/user/{id}里的id
        return userService.getById(id);
    }
    @GetMapping("/list")
    public List<User> list(){
        return userService.list();
    }
    @PostMapping("/login")
    public String login(@RequestParam String username,@RequestParam String password){
        User user=userService.getByUsername(username);
        if(user==null){
            return "用户不存在";
        }
        if(!user.getPassword().equals(password)){
            return "密码错误";
        }
        return "登录成功，欢迎 "+username;
    }
}