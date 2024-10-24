package com.cykj;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * gateWay入口
 *
 */
@SpringBootApplication
public class gateway_App
{
    public static void main( String[] args )
    {

        SpringApplication.run(gateway_App.class,args);
    }
}
