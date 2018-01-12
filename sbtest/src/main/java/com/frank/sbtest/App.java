package com.frank.sbtest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Hello world!
 * 
 */
@SpringBootApplication
@ComponentScan({"com.frank.sbtest"})
@MapperScan({"com.frank.sbtest.**.dao"})
@EnableTransactionManagement
public class App 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(App.class, args);
    }
}
