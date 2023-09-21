package com.note;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages="com.note.dbdocs.mapper")
public class MyNoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyNoteApplication.class, args);
    }

}
