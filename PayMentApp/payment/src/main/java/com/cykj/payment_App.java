package com.cykj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableFeignClients
@EnableScheduling  // 开启定时任务支持
@EnableHystrix
public class payment_App
{
    public static void main( String[] args )
    {
        SpringApplication.run(payment_App.class,args);
    }
}
