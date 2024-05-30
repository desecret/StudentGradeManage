package org.example.back;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan
@SpringBootApplication
public class BackApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner printMyBatisConfig(SqlSessionFactory sqlSessionFactory) {
//        return args -> {
//            System.out.println("MyBatis Configuration: " + sqlSessionFactory.getConfiguration());
//        };
//    }
}
