package org.example.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.oj.entity.User;
@Mapper  //这是操作数据库的接口
public interface UserMapper extends BaseMapper<User> { //继承增删改查方法，操作的User类
    /*
userMapper.insert(user);           // 增
userMapper.selectById(1L);         // 查（根据id）
userMapper.selectList(null);       // 查（查全部）
userMapper.updateById(user);       // 改
userMapper.deleteById(1L);         // 删
*/
}