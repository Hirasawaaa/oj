package org.example.oj.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.oj.entity.User;
import org.example.oj.mapper.UserMapper;
import org.springframework.stereotype.Service;
@Service   //业务逻辑类
public class UserService extends ServiceImpl<UserMapper, User> {
    /*
userService.save(user);              // 保存用户
userService.getById(1L);             // 根据id查询
userService.list();                  // 查询全部
userService.updateById(user);        // 更新用户
userService.removeById(1L);          // 删除用户
*/
    public User getByUsername(String username){
        return lambdaQuery().eq(User::getUsername,username).one();
    }
}


