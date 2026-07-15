package org.example.oj.config;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.oj.common.Result;
import org.example.oj.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
//import com.fasterxml.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectMapper;

@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenService tokenService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)throws Exception{
        String token=request.getHeader("Authorization");

        if(!tokenService.isValid((token))){
            response.setStatus(401);
            response.setContentType("application/json;charset=utf-8");
            ObjectMapper objectMapper=new ObjectMapper();
            Result<Void> result=Result.error(401,"未登录或登录过期");

//          response.getWriter().write("未登录或登录过期");
            response.getWriter().write(objectMapper.writeValueAsString(result));
            return false;
        }
        return true;
    }
}
