package org.example.oj.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.oj.entity.Problem;
import org.example.oj.mapper.ProblemMapper;
import org.springframework.stereotype.Service;
@Service   //业务逻辑类
public class ProblemService extends ServiceImpl<ProblemMapper,Problem> {

}
