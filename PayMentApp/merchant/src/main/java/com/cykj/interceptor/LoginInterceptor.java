package com.cykj.interceptor;

import com.alibaba.fastjson2.JSON;
import com.cykj.utils.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @Author: fichil
 * @Date: 2024-08-8 14:36
 * @Description: 管理员控制类
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 拦截请求
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //设置响应类型
        response.setContentType("application/json;charset=UTF-8");
        // 从请求头获取token
        String token = request.getHeader("admin_token");
        // 判断token是否为空
        if (token == null || token.isEmpty()) {
            // token为空
            ResponseDTO dto = new ResponseDTO(-186,"token不存在，未登陆",null);
            response.getWriter().write(JSON.toJSONString(dto));
            return false;
        }else {
            //从redis取出token
            if (redisTemplate.hasKey(token)) {
                Object s = redisTemplate.opsForValue().get(token);
                redisTemplate.opsForValue().set(token,s,30, TimeUnit.MINUTES);
                return true;
            }else {
                ResponseDTO dto = new ResponseDTO(-186,"token已过期",null);
                response.getWriter().write(JSON.toJSONString(dto));
                return false;
            }

        }
    }
}
