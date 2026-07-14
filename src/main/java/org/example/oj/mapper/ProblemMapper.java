package org.example.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.oj.entity.Problem;
@Mapper  //这是操作数据库的接口
public interface ProblemMapper extends BaseMapper<Problem> {

}

