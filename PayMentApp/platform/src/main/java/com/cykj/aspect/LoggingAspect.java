package com.cykj.aspect;


import com.cykj.annotation.Logable;
import com.cykj.mapper.PlatformLogMapper;
import com.cykj.pojo.Log;
import com.cykj.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class LoggingAspect {

    private HttpServletRequest request;


    private PlatformLogMapper platformLogMapper;


    @Autowired
    public LoggingAspect(HttpServletRequest request, PlatformLogMapper platformLogMapper) {
        this.request = request;
        this.platformLogMapper = platformLogMapper;
    }

    @Pointcut("@annotation(com.cykj.annotation.Logable)")
    public void loggableMethods() {
    }


//    @After("execution(* com.controller..*.*(..))")
    @After("loggableMethods()")
    public void logAfter(JoinPoint joinPoint){
        String token = request.getHeader("admin_token");
//        登陆的日志从AdminServiceImpl中直接添加
        if (token == null){
            System.out.println("token不存在,日志记录失败;登陆的日志从AdminServiceImpl中直接添加,不走日志拦截器");
            return;
        }

        //获取用户id
//        Map<String, Object> stringObjectMap = JwtUtils.parseToken(token);
//        Map<String,Object> adminInfo = (Map<String, Object>)stringObjectMap.get("id");
//        Integer id = Integer.valueOf(adminInfo.get("id").toString());
        Claims claims = JwtUtils.parseToken(token);
        Integer id = (Integer)claims.get("id");

        // 获取被调用的方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取 @Logable 注解
        Logable logableAnnotation = method.getAnnotation(Logable.class);
        String logValue = logableAnnotation != null ? logableAnnotation.value() : null;

        // 获取操作方法名
//        String active_name = joinPoint.getSignature().getName();


        Log logs = new Log();
        logs.setAdminId(id);
//        logs.setLogs(active_name);
        logs.setLogs(logValue);
        platformLogMapper.insertSelective(logs);
    }
}
